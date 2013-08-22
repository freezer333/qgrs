package qgrs.data.mongo.operations;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import qgrs.compute.GeneSequencePair;
import qgrs.data.GQuadruplex;
import qgrs.data.GeneSequence;
import qgrs.data.QgrsHomology;
import qgrs.data.mongo.primitives.G4;
import qgrs.data.mongo.primitives.G4H;
import qgrs.data.mongo.primitives.MRNA;
import qgrs.data.mongo.primitives.Range;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class DbSave {
	DB db;
	DBCollection principals;
	MongoClient mongoClient;
	
	private static final int MIN_GSCORE = 13;
	private static final double MIN_QGRSH_SCORE = 0.75;
	private static final double MIN_MRNA_ALIGNMENT = 0.5;
	
		
	public DbSave() throws UnknownHostException {
		mongoClient = new MongoClient();
		db = mongoClient.getDB( "qgrs" );
		principals =  db.getCollection("principals");
	}
	
	public void close() {
		mongoClient.close();
	}
	
	
	public static void main(String [] strings) throws UnknownHostException {
		new DbSave().resetHomology();
	}
	public void resetHomology() {
		principals.setObjectClass(MRNA.class);
		long count = principals.count();
		long completed = 0;
		
		
		DBCursor cursor = principals.find();
		try {
		   while(cursor.hasNext()) {
			   boolean updated = false;
		       MRNA mrna = (MRNA) cursor.next();
		       ArrayList<BasicDBObject> g4s = (ArrayList<BasicDBObject>) mrna.get("g4s");
		       for ( BasicDBObject bg4 : g4s )  {
		    	   ArrayList<BasicDBObject> g4hs = (ArrayList<BasicDBObject>) bg4.get("conservedG4s");
		    	   for ( BasicDBObject bg4h : g4hs) {
		    		   double o = bg4h.getDouble("overallAbsoluteScore");
		    		   if ( o <= 1) {
		    			   int newo = (short)Math.round(o*100);
		    			   bg4h.put("overallAbsoluteScore", newo);
		    			   updated = true;
		    		   }
		    	   }
		       }
		       completed++;
		       if ( updated) {
		    	   principals.save(mrna);
		    	  
		       }
		       System.out.println("Processed - " + completed + " / " + count + " -> " + updated);
		       
		       
		   }
		} finally {
		   cursor.close();
		   
		}
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
		seq.setSequenceLength(gene.getSequenceLength());
		seq.setGiNumber(gene.getGiNumber());
		seq.setSymbol(gene.getGeneSymbol());
		seq.set5UTR(new Range(gene.getUtr5()));
		seq.set3UTR(new Range(gene.getUtr3()));
		seq.setCds(new Range(gene.getCds()));
		
		for ( qgrs.data.Range r : gene.getPolyASignals()) {
			seq.getPolyASignals().add(new Range(r));
		}
		for ( qgrs.data.Range r : gene.getPolyASites()) {
			seq.getPolyASites().add(new Range(r));
		}
		
		return seq;
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
		
		if ( q.getOverlaps() != null ) {
			ArrayList<G4> overlaps = new ArrayList<G4>();
			for (GQuadruplex o : q.getOverlaps() ) {
				if ( o.getScore() >= MIN_GSCORE ) {
					overlaps.add(buildG4(gene, o).asComparison());
				}
			}
			g4.setOverlappingMotifs(overlaps);
		}
		
		return g4;
	}
	
	private G4H buildG4H(GeneSequencePair pair, QgrsHomology qh) {
		G4H g4h = new G4H();
		g4h.setAlignmentPercentage(pair.getSimilarityPercentage());
		g4h.setAvgLoopScore(qh.getAvgLoopScore());
		g4h.setG4(buildG4(pair.getComparison(), qh.getGq2()).asComparison());
		g4h.setRecordId(qh.getGq1().getId() + "x" + qh.getGq2().getId());
		g4h.setMrna(buildFromGene(pair.getComparison()).asComparison());
		// The overall score will be an integer, to allow easier searching (and indexing)
		g4h.setOverallAbsoluteScore(Math.round(qh.getOverallScore()*100));
		g4h.setOverlapScore(qh.getOverlapScore());
		g4h.setTetradScore(qh.getTetradScore());
		g4h.setTotalLengthScore(qh.getTotalLengthScore());
		return g4h;
	}
	
	private void buildAndInsertFullRecord(GeneSequencePair pair,List<QgrsHomology> similarityResults) {
		if ( pair.getSimilarityPercentage() < MIN_MRNA_ALIGNMENT) return;
		
		MRNA principal = buildFromGene(pair.getPrinciple());
		HashMap<String, G4> principalG4s = new HashMap<String, G4>();
		// first save all the primary G4s
		for ( GQuadruplex g : pair.getPrinciple().getgQuads()) {
			if ( g.getScore() >= MIN_GSCORE ) {
				G4 pG4 = buildG4(pair.getPrinciple(), g);
				if ( !principalG4s.containsKey(pG4.getG4Id())) {
					principalG4s.put(pG4.getG4Id(), pG4);
					principal.getG4s().add(pG4);
				}
			}
		}
		// now link all the homologies
		for ( QgrsHomology h : similarityResults) {
			if ( h.getOverallScore() >= MIN_QGRSH_SCORE ) {
				G4 pG4 = principalG4s.get(h.getGq1().getId());
				if ( pG4 != null ) {
					G4H g4h = buildG4H(pair, h);
					pG4.getConservedG4s().add(g4h);
				}
			}
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
