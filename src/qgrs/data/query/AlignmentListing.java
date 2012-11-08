package qgrs.data.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Element;

import qgrs.controllers.PageHelper;
import qgrs.data.AlignmentRecord;
import qgrs.data.GeneSequence;
import qgrs.db.AlignmentRecordDb;
import qgrs.db.DatabaseConnection;
import qgrs.db.GeneSequenceDb;
import qgrs.input.QParam;
import qgrs.model.DbCriteria;

public class AlignmentListing {

	private int homologyCount;
	private float similarityPercentage;
	private String alignmentId;
	private String principleGeneName;
	private String principleSpeciesName;
	private Integer principleQgrsCount;
	private String principleId;
	private String comparisonGeneName;
	private String comparisonSpeciesName;
	private Integer comparisonQgrsCount;
	private String comparisonId;
	private static final DecimalFormat format = new DecimalFormat("0.00%");
	
	public  AlignmentListing(AlignmentRecord r, 
			HashMap<String, AlignmentGeneInfo> gMap, 
			HashMap<String, Integer> principleQgrsMap, HashMap<String, Integer> comparisonQgrsMap, HashMap<String, Integer> hMap
			) {
		this.alignmentId = r.getId();
		
		AlignmentGeneInfo principle = gMap.get(r.getPrinciple());
		AlignmentGeneInfo comparison = gMap.get(r.getComparison());
		this.principleGeneName = principle.geneSymbol;
		this.comparisonGeneName = comparison.geneSymbol;
		this.principleSpeciesName = principle.speciesName;
		this.comparisonSpeciesName = comparison.speciesName;
		this.principleId = principle.geneId;
		this.comparisonId = comparison.geneId;
		this.principleQgrsCount = principleQgrsMap.containsKey(r.getPrinciple()) ? principleQgrsMap.get(r.getPrinciple()) : 0;
		this.comparisonQgrsCount = comparisonQgrsMap.containsKey(r.getComparison()) ?  comparisonQgrsMap.get(r.getComparison()) : 0;
		this.homologyCount = hMap.get(r.getId());
		this.similarityPercentage = r.getSimilarityPercentage();
	}
	public String getGeneLabel() {
		return principleGeneName.trim().equalsIgnoreCase(comparisonGeneName.trim()) ? principleGeneName : (principleGeneName + " / " + comparisonGeneName);
	}
	public Element getXmlElement() {
		Element retval = new Element("alignment_listing");
		retval.addContent(new Element("alignmentId").setText(this.alignmentId));
		retval.addContent(new Element("principleGeneName").setText(this.principleGeneName));
		retval.addContent(new Element("principleSpeciesName").setText(this.principleSpeciesName));
		retval.addContent(new Element("principleId").setText(this.principleId));
		retval.addContent(new Element("principleQgrsCount").setText(String.valueOf(this.principleQgrsCount)));
		retval.addContent(new Element("comparisonGeneName").setText(this.comparisonGeneName));
		retval.addContent(new Element("comparisonSpeciesName").setText(this.comparisonSpeciesName));
		retval.addContent(new Element("comparisonId").setText(this.comparisonId));
		retval.addContent(new Element("comparisonQgrsCount").setText(String.valueOf(this.comparisonQgrsCount)));
		retval.addContent(new Element("geneLabel").setText(getGeneLabel()));
		retval.addContent(new Element("homologyCount").setText(String.valueOf(homologyCount)));
		retval.addContent(new Element("similarityPercentage").setText(format.format(this.similarityPercentage)));
		return retval;
	}
	
	public static void fillQgrsMap(DatabaseConnection dc, QgrsQuery clause, HashMap<String, Integer> map) {
		try {
			String query = "SELECT geneId, COUNT(id) AS total FROM QGRS " + clause.toSql() + " GROUP BY geneId ";
			//System.out.println("QUERY DEBUG:  " + query);
			long start = System.nanoTime();
			PreparedStatement s = dc.getConnection().prepareStatement(query);
			clause.set(s);
			ResultSet rs = s.executeQuery();
			while ( rs.next()) {
				map.put(rs.getString("geneId"), rs.getInt("total"));
			}
			double  elapsed = System.nanoTime() - start;
			//System.out.println("QUERY TIME:  " + new DecimalFormat("0.000").format(elapsed /1000000000) + " sec");
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	public static List<AlignmentListing> list(DatabaseConnection dc, DbCriteria dbCriteria) {
		HashMap<String, Integer> principleQgrsMap = new HashMap<String, Integer>();
		HashMap<String, Integer> comparisonQgrsMap = new HashMap<String, Integer>();
		HashMap<String, Integer> hMap = new HashMap<String, Integer>();
		
		GeneQuery principleGeneQuery = dbCriteria.buildPrincipleGeneQuery();
		GeneQuery comparisonGeneQuery = dbCriteria.buildComparisonGeneQuery();
		
		QgrsQuery principleQgrsQuery = dbCriteria.buildPrincipleQgrsClause();
		QgrsQuery comparisonQgrsQuery = dbCriteria.buildComparisonQgrsClause();
		
		principleQgrsQuery.setGeneClause(principleGeneQuery);
		comparisonQgrsQuery.setGeneClause(comparisonGeneQuery);
		
		
		// Build a map of all QGRS fitting criteria (do we need two maps - p/c?)
		fillQgrsMap(dc, principleQgrsQuery, principleQgrsMap);
		if ( principleQgrsQuery.equals(comparisonQgrsQuery)) {
			// no need to go get another map
			comparisonQgrsMap = principleQgrsMap;
		}
		else {
			fillQgrsMap(dc, comparisonQgrsQuery,comparisonQgrsMap);
		}
		
		AlignmentQuery alignmentQuery = new AlignmentQuery(
				Float.parseFloat(dbCriteria.get(QParam.Db_MinAlignmentScore)), 
				principleGeneQuery, comparisonGeneQuery);
		AlignmentRecordDb db = new AlignmentRecordDb (dc);
		List<AlignmentRecord> alignments = db.get(alignmentQuery);
		
		HomologyQuery homologyQuery = dbCriteria.buildQgrsHomologyQuery();
		homologyQuery.setPrincipleQgrsQuery(principleQgrsQuery);
		homologyQuery.setComparisonQgrsQuery(comparisonQgrsQuery);
		homologyQuery.setAlignmentQuery(alignmentQuery);
		try {
			String c_query = "SELECT alignmentId, COUNT(id) AS total FROM QGRS_H " + homologyQuery.toSql() + " GROUP BY alignmentId"; 
			PreparedStatement s = dc.getConnection().prepareStatement(c_query);
			ResultSet rs = s.executeQuery();
			int count = 0;
			while ( rs.next() ) {
				count++;
			}
			PageHelper pager = new PageHelper(dbCriteria, count);
			String query = "SELECT alignmentId, COUNT(id) AS total FROM QGRS_H " + homologyQuery.toSql() + " GROUP BY alignmentId";  
			query += " ORDER BY alignmentId LIMIT " + dbCriteria.getPageLimit() + " OFFSET " + pager.getComputedOffset();
			//System.out.println("QUERY DEBUG:  " + query);
			long start = System.nanoTime();
			s = dc.getConnection().prepareStatement(query);
			rs = s.executeQuery();
			while ( rs.next()) {
				hMap.put(rs.getString("alignmentId"), rs.getInt("total"));
			}
			//double  elapsed = System.nanoTime() - start;
			//System.out.println("QUERY TIME:  " + new DecimalFormat("0.000").format(elapsed /1000000000) + " sec");
			//System.out.println("RETURNED " + hMap.size() + " QGRH_H Alignments");
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
		
		
		// ----  Build map with all gene info in the result set
		HashSet<String> geneIds = new HashSet<String>();
		for ( AlignmentRecord s : alignments) {
			if ( hMap.get(s.getId()) != null)  {
				geneIds.add(s.getPrinciple());
				geneIds.add(s.getComparison());
			}
		}
		System.out.println("Getting gene information for " + geneIds.size() + " genes");
		HashMap<String, AlignmentGeneInfo> gMap = new HashMap<String, AlignmentGeneInfo>();
		GeneSequenceDb gDb = new GeneSequenceDb(dc);
		List<GeneSequence> genes = gDb.getIn(geneIds);
		for ( GeneSequence g : genes) {
			gMap.put(g.getAccessionNumber(), new AlignmentGeneInfo(g));
		}
		//------------------------------------
		
	    List<AlignmentListing> listing = new LinkedList<AlignmentListing>();
	    for ( AlignmentRecord s : alignments) {
	    	if ( hMap.get(s.getId()) != null)  {
	    		listing.add(new AlignmentListing(s, gMap, principleQgrsMap, comparisonQgrsMap, hMap));
	    	}
	    }
		return listing;
		
	}
	
	

	public int getHomologyCount() {
		return homologyCount;
	}

	public float getSimilarityPercentage() {
		return similarityPercentage;
	}

	public String getAlignmentId() {
		return alignmentId;
	}

	public String getPrincipleGeneName() {
		return principleGeneName;
	}

	public String getPrincipleSpeciesName() {
		return principleSpeciesName;
	}

	public Integer getPrincipleQgrsCount() {
		return principleQgrsCount;
	}

	public String getComparisonGeneName() {
		return comparisonGeneName;
	}

	public String getComparisonSpeciesName() {
		return comparisonSpeciesName;
	}

	public Integer getComparisonQgrsCount() {
		return comparisonQgrsCount;
	}
	
	
	
}
