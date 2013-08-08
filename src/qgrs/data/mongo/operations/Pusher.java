package qgrs.data.mongo.operations;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import qgrs.compute.GeneSequencePair;
import qgrs.data.GQuadruplex;
import qgrs.data.GeneSequence;
import qgrs.data.QgrsHomology;
import qgrs.data.mongo.primitives.Alignment;
import qgrs.data.mongo.primitives.G4;
import qgrs.data.mongo.primitives.G4H;
import qgrs.data.mongo.primitives.MRNA;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Pusher {
	DB db;
	DBCollection principals;
	
		
	public Pusher() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient();
		db = mongoClient.getDB( "qgrs" );
		principals =  db.getCollection("principals");
	}
	
	
	public void push(GeneSequencePair pair,List<QgrsHomology> similarityResults) {
		/*
		 * 2 states
		 * 
		 *   (1) the principle mrna is not yet in db
		 *   	 - we build the complete record and push immediately
		 *   
		 *   (2) this principle mrna is present, but there are no G4H associated with the comparison
		 *       - we must now cycle through each similarity result and add
		 *       - note that if the principal mRNA is present already,  then by definition, all principal G4 
		 *         are already there.
		 * 
		 * 	 --- note, there is no way for the mrna to be present, and a corresponding alignment - since
		 *       we save all homology records at the same time...
		 */
		
		if ( !principalInDb(pair.getPrinciple())) {
			this.buildAndInsertFullRecord(pair, similarityResults);
		}
		else if (!isHomologPresent(pair) ) {
			for ( QgrsHomology qgrsh : similarityResults) {
				buildAndInsertG4H(pair, qgrsh);
			}
		}
		else {
			System.out.println("G4 conservation already persisted for pair");
		}
	}
	
	private MRNA buildFromGene(GeneSequence gene) {
		MRNA seq = new MRNA();
		seq.setAccessionNumber(gene.getAccessionNumber());;
		seq.setName(gene.getGeneName());
		seq.setOntology(gene.getOntologyData());
		seq.setSpecies(gene.getSpecies());
		return seq;
	}
	private Alignment buildAlignment(GeneSequencePair pair) {
		Alignment a = new Alignment();
		a.setSimilarityPercentage(pair.getSimilarityPercentage());
		a.setSimilarityScore(pair.getSimilarityScore());
		return a;	
	}
	private G4 buildG4(GeneSequence gene, GQuadruplex q) {
		G4 g4 = new G4();
		g4.setDistanceFromPolyASignal(q.getDistanceFromPolyASignal());
		g4.setGeneAccessionNumber(gene.getAccessionNumber());
		g4.setG4Id(q.getId());
		g4.setIn3Prime(q.in3UTR());
		g4.setIn5Prime(q.in5UTR());
		g4.setInCds(q.inCDS());
		g4.setNumTetrads(q.getNumTetrads());
		g4.setLoop1Length(q.getLoop1Length());
		g4.setLoop2Length(q.getLoop2Length());
		g4.setLoop3Length(q.getLoop3Length());
		g4.setScore(q.getScore());
		g4.setSequenceSlice(q.getSequenceSlice());
		g4.setTetrad1(q.getStart().getIndexWithoutGaps());
		g4.setTetrad2(q.getTetrad2Start().getIndexWithoutGaps());
		g4.setTetrad3(q.getTetrad3Start().getIndexWithoutGaps());
		g4.setTetrad4(q.getTetrad4Start().getIndexWithoutGaps());
		g4.setTotalLength(q.getLength());
		return g4;
	}
	private G4H buildG4H(GeneSequencePair pair, QgrsHomology qh) {
		G4H g4h = new G4H();
		g4h.setAlignment(buildAlignment(pair));
		g4h.setAvgLoopScore(qh.getAvgLoopScore());
		g4h.setG4(buildG4(pair.getComparison(), qh.getGq2()));
		g4h.setRecordId(qh.getGq1().getId() + "x" + qh.getGq2().getId());
		g4h.setMrna(buildFromGene(pair.getComparison()));
		g4h.setOverallAbsoluteScore(qh.getOverallScore());
		g4h.setOverlapScore(qh.getOverlapScore());
		g4h.setTetradScore(qh.getTetradScore());
		g4h.setTotalLengthScore(qh.getTotalLengthScore());
		return g4h;
	}
	private void buildAndInsertFullRecord(GeneSequencePair pair,List<QgrsHomology> similarityResults) {
		MRNA principal = buildFromGene(pair.getPrinciple());
		HashMap<String, G4> principalG4s = new HashMap<String, G4>();
		// first save all the primary G4s
		for ( QgrsHomology h : similarityResults) {
			G4 pG4 = buildG4(pair.getPrinciple(), h.getGq1());
			if ( !principalG4s.containsKey(pG4.getG4Id())) {
				principalG4s.put(pG4.getG4Id(), pG4);
				principal.getG4s().add(pG4);
			}
		}
		// now link all the homologies
		for ( QgrsHomology h : similarityResults) {
			G4 pG4 = principalG4s.get(h.getGq1().getId());
			G4H g4h = buildG4H(pair, h);
			pG4.getConservedG4s().add(g4h);
		}
		
		
		principals.insert(principal);
	}
	
	
	private boolean principalInDb(GeneSequence p) {
		BasicDBObject query = new BasicDBObject("accessionNumber", p.getAccessionNumber());
		return principals.count(query) > 0;
		
	}
	
	private boolean isHomologPresent(GeneSequencePair pair) {
		// Search for mrna (p) with g4 with g4H with mrna (c)
		BasicDBObject pMatch = new BasicDBObject("accessionNumber", pair.getPrinciple().getAccessionNumber());
		BasicDBObject cMatch = new BasicDBObject("mrna.accessionNumber", pair.getComparison().getAccessionNumber());
		BasicDBObject c_elemMatch = new BasicDBObject();
		c_elemMatch.put("$elemMatch", cMatch);
		BasicDBObject conserved = new BasicDBObject();
		conserved.put("conservedG4s", c_elemMatch);
		BasicDBObject g_elemMatch = new BasicDBObject();
		g_elemMatch.put("$elemMatch", conserved);
		BasicDBObject g4s = new BasicDBObject();
		g4s.put("g4s", g_elemMatch);
		BasicDBObject and = new BasicDBObject();
		List<BasicDBObject> ops = new LinkedList<BasicDBObject>();
		ops.add(g4s);
		ops.add(pMatch);
		
		and.put("$and", ops);
		
		return principals.count(and) > 0;
		
	}
	
	private void buildAndInsertG4H(GeneSequencePair pair,QgrsHomology similarityResult) {
		G4H g4h = buildG4H(pair, similarityResult);
		DBObject match = BasicDBObjectBuilder.start("accessionNumber", pair.getPrinciple().getAccessionNumber()).add("g4s.g4Id", similarityResult.getGq1().getId()).get();
		BasicDBObject update = new BasicDBObject();
		BasicDBObject g4 = new BasicDBObject("g4s.$.conservedG4s", g4h);
		update.put("$push", g4);
		principals.update(match, update);
	}
	
	
}
