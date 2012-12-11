package qgrs.data.query.genehomology;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import qgrs.data.GQuadruplex;
import qgrs.data.query.PageableQuery;
import qgrs.data.query.QueryUtils;
import qgrs.data.query.WhereClause;
import qgrs.input.QParam;
import qgrs.model.DbCriteria;

public class GeneHomologyQuery extends WhereClause 
implements PageableQuery {
	private String principleGeneId;
	private String principleGeneSymbol;
	private String principleGeneSpecies;
	private String comparsionGeneSpecies;
	private float minimumGeneAlignmentPercentage;
	
	private String qgrsId;
	private int qgrsMinGScore;
	private int qgrsMinTetrads;
	private float qgrsMinHomologyScore;
	private boolean in5Prime;
	private boolean inCds;
	private boolean in3Prime;
	
	private int pageLimit;
	private int pageOffset;
	
	/**
	 * This is still wrong.  
	 * 
	 * 	If query is based on QGRS_H, it needs to be left joined with gene-a to include the alignments for which no qgrs
	 * appear.  The problem is that gene-a doesn't have species and symbol, so those dangling tuples never are included in result set.
	 * 
	 * One solution is to add species and symbol to gene-a.  This seems to make sense
	 * 
	 * Another solution is to try the 3-way join on mysql, which would make the query hit gene_a directly and use 
	 * qgrs and qgrs-h only for correlated queries.  This doesn't include grouping.
	 * 
	 * 
	 * Lean towards option 1 though - there are some really nice things about H2 (portability).
	 */
	
	private final String selectClauseResults = 
			"select alignmentId, P_AccessionNumber, C_Accessionnumber , P_GENESYMBOL, C_GENESYMBOL, P_SPECIES, C_SPECIES, ALIGNMENTSCORE, " +  
			"(SELECT COUNT(DISTINCT QGRS.ID) FROM QGRS WHERE GENEID=P_ACCESSIONNUMBER #) as P_QgrsCount,  " +
			"COUNT (DISTINCT QGRS_H.GQ1ID) as HCOUNT ";

	private final String fromClause = " FROM QGRS_H ";
	private final String orderClause = " ORDER BY P_ACCESSIONNUMBER ";
	private final String groupClause = " GROUP BY P_ACCESSIONNUMBER, C_AccessionNumber ";
	
	
	private String alignment() {
		return "ALIGNMENTSCORE >= " + p(minimumGeneAlignmentPercentage/100);
	}
	private String qgrsHomology() {
		return "OVERALLSCORE >= " + p(qgrsMinHomologyScore);
	}
	
	private String tetrad() {
		return "P_TETRADS >= " + p(qgrsMinTetrads);
	}
	private String gScore() {
		return "P_GSCORE >= " + p(qgrsMinGScore);
	}
	
	private String tetradQgrs() {
		return "QGRS.NUMTETRADS >= " + p(qgrsMinTetrads);
	}
	private String gScoreQgrs() {
		return "QGRS.GSCORE >= " + p(qgrsMinGScore);
	}
	
	
	private String buildWhereClause() {
		LinkedList<String> criteria = new LinkedList<String>();
		if ( qgrsMinTetrads > GQuadruplex.MINIMUM_TETRAD ) criteria.add(this.tetrad());
		if ( qgrsMinGScore > GQuadruplex.MINIMUM_SCORE ) criteria.add(this.gScore());
		criteria.add(QueryUtils.buildRegionConstraint(in5Prime, inCds, in3Prime, QueryUtils.qgrs_h_regions_cols));
		criteria.add(this.stringConstraint("GQ1ID", this.qgrsId));
		criteria.add(this.stringConstraint("P_AccessionNUmber ", this.principleGeneId));
		criteria.add(this.stringConstraint("P_GENESYMBOL", this.principleGeneSymbol));
		criteria.add(this.stringConstraint("P_SPECIES", this.principleGeneSpecies));
		criteria.add(this.stringConstraint("C_SPECIES", this.comparsionGeneSpecies));
		
		if ( this.minimumGeneAlignmentPercentage > 0.001 ) {
			criteria.add(this.alignment());
		}
		if ( this.qgrsMinHomologyScore > 0.3 ) {
			criteria.add(this.qgrsHomology());
		}
		return  where(criteria);
	}
	
	
	
	
	@Override
	public String toCountSql() {
		return "SELECT COUNT(*) FROM (" + this.toSql() + ")";
	}

	@Override
	public String toResultSetSql() {
		return toSql() +  
				" LIMIT " + this.pageLimit + " OFFSET " + this.pageOffset;
	}

	@Override
	public void setParameters(DbCriteria dbCriteria) {
		this.qgrsId = dbCriteria.get(QParam.Db_QgrsId1);
		this.qgrsMinTetrads = Integer.parseInt( dbCriteria.get(QParam.Db_MinTetrads1));
		this.qgrsMinGScore = Integer.parseInt(dbCriteria.get(QParam.Db_GScore1));
		this.in5Prime = dbCriteria.readBoolean(dbCriteria.get(QParam.Db_Region15UTR));
		this.inCds = dbCriteria.readBoolean(dbCriteria.get(QParam.Db_Region1CDS));
		this.in3Prime = dbCriteria.readBoolean(dbCriteria.get(QParam.Db_Region13UTR));
		
		
		this.minimumGeneAlignmentPercentage = Float.parseFloat(dbCriteria.get(QParam.Db_MinAlignmentScore));
		this.qgrsMinHomologyScore = Float.parseFloat(dbCriteria.get(QParam.Db_OverallSimilarity)) ;
		this.principleGeneId = dbCriteria.get(QParam.Db_GeneId1);
		this.principleGeneSpecies =  dbCriteria.get(QParam.Db_Species1);
		this.principleGeneSymbol = dbCriteria.get(QParam.Db_GeneSymbol1);
		this.comparsionGeneSpecies =  dbCriteria.get(QParam.Db_Species2);

	}

	@Override
	public void setPagingParameters(int pageLimit, int computedOffset) {
		this.pageLimit = pageLimit;
		this.pageOffset = computedOffset;
	}
	
	
	
	private String transformSelect() {
		LinkedList<String> criteria = new LinkedList<String>();
		if ( qgrsMinTetrads > GQuadruplex.MINIMUM_TETRAD ) criteria.add(this.tetradQgrs());
		if ( qgrsMinGScore > GQuadruplex.MINIMUM_SCORE ) criteria.add(this.gScoreQgrs());
		criteria.add(QueryUtils.buildRegionConstraint(this.in5Prime, this.inCds, this.in3Prime, QueryUtils.qgrs_regions_cols));
		
		String w = continuedWhere(criteria);
		return this.selectClauseResults.replace("#", w);
		
	}
	@Override
	public String toSql() {
		String q = 	transformSelect() + 
				fromClause + 
				this.buildWhereClause() + this.groupClause + this.orderClause ;
		return q;
	}
	@Override
	public void set(PreparedStatement ps) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
