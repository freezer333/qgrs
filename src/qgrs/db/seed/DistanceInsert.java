package qgrs.db.seed;

import java.util.HashSet;
import java.util.LinkedList;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.DistanceFromPolyA;
import qgrs.data.mongo.primitives.jongo.G4;
import qgrs.data.mongo.primitives.jongo.MRNA;

import com.mongodb.DB;
import com.mongodb.MongoClient;


public class DistanceInsert {
	
	
	public static boolean hasLocationMarkers(MRNA mrna) {
		if ( mrna.getCds() != null ) return true;
		if ( mrna.getPolyASignals() != null && mrna.getPolyASignals().size() > 0) return true;
		return false;
	}
	
	
	public static void main(String[] args) throws Exception {
		DB db = new MongoClient().getDB("qgrs");

		Jongo jongo = new Jongo(db);
		MongoCollection principals = jongo.getCollection("principals");
		int j =0;
		Iterable<MRNA> all = principals.find().projection("{accessionNumber: 1, cds:1, polyASignals:1}").as(MRNA.class);
		HashSet<String> mrnas = new HashSet<String>();
		for (MRNA mrna : all ) {
			if ( ! hasLocationMarkers(mrna)) continue;
			mrnas.add(mrna.getAccessionNumber());
			System.out.println(j++);
		}
		int i = 1;
		
		for ( String s : mrnas){
			System.out.println("Upgrading mrna " + i++ + " of " + mrnas.size());
			
			MRNA mrna = principals.findOne("{accessionNumber:#}", s).as(MRNA.class);
			for ( G4 g4 : mrna.getG4s() ) {
				if ( mrna.getPolyASignals() != null && mrna.getPolyASignals().size() > 0) {
					LinkedList<DistanceFromPolyA> distances = new LinkedList<DistanceFromPolyA>();
					for ( Range r : mrna.getPolyASignals() ) {
						distances.add(new DistanceFromPolyA(g4, r));
					}
					update(principals, mrna, g4, distances);
				}
				
				update (principals, mrna, g4, 
						getDistanceTo(mrna.getCds().getStart(), g4), 
						getDistanceTo(mrna.getCds().getEnd(), g4));
				
				
			}
		}
	}

	public static int getDistanceTo(int loc, G4 g4) {
		int start = g4.getTetrad1();
		int end = g4.getTetrad4() + g4.getNumTetrads();
		if ( end <= loc ) {
			//up stream
			return loc - end;
		}
		else if ( start >= loc) {
			// downstream
			return start - loc;
		}
		else {
			return 0;
		}
	}

	public static void update(MongoCollection principals, MRNA mrna, G4 g4, LinkedList<DistanceFromPolyA> distances) {
		principals.update(
				"{accessionNumber: #, g4s.g4Id : #}", mrna.getAccessionNumber(), g4.getG4Id())
				.with("{$set: {g4s.$.distancesFromPolyASignals: #}}", distances);
		//System.out.println("Updated poly distances for " + g4.getG4Id());
	}
	public static void update(MongoCollection principals, MRNA mrna, G4 g4, 
			int distanceToStart, int distanceToStop) {
		principals.update(
				"{accessionNumber: #, g4s.g4Id : #}", mrna.getAccessionNumber(), g4.getG4Id())
				.with("{$set: {g4s.$.distanceToStartCodon: #, g4s.$.distanceToStopCodon:#}}", distanceToStart, distanceToStop);
	
		//System.out.println("Updated CDS distances for " + g4.getG4Id());
	}
	
	
	
	
}
