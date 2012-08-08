package qgrs.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import qgrs.data.GQuadruplexRecord;
import qgrs.data.GeneSequence;
import qgrs.data.QgrsHomologyRecord;
import qgrs.data.query.AlignmentQuery;
import qgrs.data.query.GeneQuery;
import qgrs.data.query.HomologyQuery;
import qgrs.data.query.QgrsQuery;
import qgrs.db.GeneSequenceDb;
import qgrs.db.HomologyRecordDb;
import qgrs.db.QgrsDb;
import qgrs.db.GeneSequenceDb.HomologSide;
import qgrs.input.QParam;
import qgrs.model.DbCriteria;

public class ResultHelper {

	public Map<String, Integer> buildQgrsCountMap(Collection<GeneSequence> genes, GeneSequenceDb seqDb, QgrsQuery qgrsQuery) {
		Collection<String> al = new HashSet<String>();
		for ( GeneSequence g : genes) {
			al.add(g.getAccessionNumber());
		}
		return seqDb.getQgrsCountsInGenes(al, qgrsQuery);
	}
	public Map<String, Integer> buildHomologCountMap(Collection<GeneSequence> genes, HomologyRecordDb db, HomologSide side, DbCriteria dbCriteria) {
		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		for ( GeneSequence g : genes) {
			counts.put(g.getAccessionNumber(), 0);
		}
		
		// Have a gene list
		// Create map with key = alignment ID and value = # of QGRS-H that meet the homology query threshold
			// Build the homology query using QGRS Queries which use mrnaQueries.
		/* Build Query Object */
	    GeneQuery principleGeneQuery = dbCriteria.buildPrincipleGeneQuery();
		GeneQuery comparisonGeneQuery = dbCriteria.buildComparisonGeneQuery();
		
		QgrsQuery principleQgrsQuery = dbCriteria.buildPrincipleQgrsClause();
		QgrsQuery comparisonQgrsQuery = dbCriteria.buildComparisonQgrsClause();
		
		principleQgrsQuery.setGeneClause(principleGeneQuery);
		comparisonQgrsQuery.setGeneClause(comparisonGeneQuery);
		
		AlignmentQuery alignmentQuery = new AlignmentQuery(Float.parseFloat(dbCriteria.get(QParam.Db_MinAlignmentScore)), 
				principleGeneQuery, comparisonGeneQuery);
	    
		HomologyQuery homologyQuery = dbCriteria.buildQgrsHomologyQuery();
		homologyQuery.setPrincipleQgrsQuery(principleQgrsQuery);
		homologyQuery.setComparisonQgrsQuery(comparisonQgrsQuery);
		homologyQuery.setAlignmentQuery(alignmentQuery);
		
		// Now go through the map and tear up the keys so you have principlal and comparison, and bin them.
				// Look up the principal in al, if present, increment the count.   Do the exact same thing with comparison
		HashMap<String, Integer> rawHomologCounts = db.getCountByAlignmentId(homologyQuery); 
		for ( String alignmentId : rawHomologCounts.keySet() ) {
			String p = alignmentId.split("x")[0];
			String c = alignmentId.split("x")[1];
			if ( side == HomologSide.principle && counts.containsKey(p)) {
				int pcount = counts.get(p);
				counts.put(p, ++pcount);
			}
			else if ( side == HomologSide.comparison && counts.containsKey(c)) {
				int ccount = counts.get(c);
				counts.put(c, ++ccount);
			}
		}
		
		return counts;
	}
	
	public Map<String, GQuadruplexRecord> buildQuadruplexList( Collection<QgrsHomologyRecord> pairs, QgrsDb db) {
		HashSet<String> qs = new HashSet<String>();
		for (QgrsHomologyRecord qrs : pairs) {
			qs.add(qrs.getGq1Id());
			qs.add(qrs.getGq2Id());
		}
		return db.getIn(qs);
	}
	public HashMap<String, GeneSequence> buildGeneMapFromQuadruplexes( Collection<GQuadruplexRecord> quads, GeneSequenceDb seqDb) {
		HashMap<String, GeneSequence> geneMap = new HashMap<String, GeneSequence>();
	    Collection<String> acc = new HashSet<String>();
	    for ( GQuadruplexRecord q : quads ) {
	    	acc.add(q.getGeneAccessionNumber());
	    }
	    Collection<GeneSequence> sl = seqDb.getIn(acc);
	    for ( GeneSequence g : sl ) {
	    	geneMap.put(g.getAccessionNumber(), g);
	    }
	    return geneMap;
	}
	
	
}
