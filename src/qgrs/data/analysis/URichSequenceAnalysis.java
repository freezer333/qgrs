package qgrs.data.analysis;

import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.MRNA;
import qgrs.data.providers.MongoSequenceProvider;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class URichSequenceAnalysis {
	private final Iterable<MRNA> mrnaList ;
	private final MongoCollection uRichCollection;
	
	
	public static void main(String[] args) throws UnknownHostException {
		DB db = new MongoClient().getDB("qgrs");
		Jongo jongo = new Jongo(db);
		MongoCollection principals = jongo.getCollection("principals");
		MongoCollection uRichCollection = jongo.getCollection("uRich");
		Iterable<MRNA> all = principals.find().as(MRNA.class);
		
		URichSequenceAnalysis analysis = new URichSequenceAnalysis(all, uRichCollection);
		analysis.run();
		
			
	}
	
	public URichSequenceAnalysis(Iterable<MRNA> mrna, MongoCollection uRichCollection) {
		this.mrnaList = mrna;
		this.uRichCollection = uRichCollection;
	}
	
	public void run() {
		for ( MRNA mrna : mrnaList ) {
			try {
				runMrna(mrna);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void runMrna(MRNA mrna) throws Exception {
		MongoSequenceProvider p = new MongoSequenceProvider();
		
		String sequence = p.getPrincipalByAccession(mrna.getAccessionNumber()); 
		sequence = sequence.replaceAll("T", "U");
		
		Range [] sites = new Range[mrna.getPolyASites().size()];
		int c = 0;
		for (Range site : mrna.getPolyASites() )  {
			sites[c++] = site;
		}
		
		for ( int i = 0; i < sites.length; i++ ) {
			int start = sites[i].getEnd()+5;
			int end = (i == sites.length-1) ? mrna.getSequenceLength()-1 : sites[i+1].getStart();
			if ( end - start < 5) continue;
			String slice = sequence.substring(start, end);
			URich u = new URichFinder(slice).getUs(3);  // modified to return BEST u rich sequence.
			if ( u != null ){
				u.startNt = u.distanceFromPolyASite + sites[i].getEnd()+5;
				u.accessionNumber = mrna.getAccessionNumber();
				u.polyASiteNumber = i+1;
				uRichCollection.insert(u);
				System.out.println("Recording U");
			}
		}
	}

}
