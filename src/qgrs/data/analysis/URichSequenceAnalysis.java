package qgrs.data.analysis;

import java.net.UnknownHostException;
import java.util.Collection;

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
	private int mrnaCount = 0;
	Jongo jongo;
	
	public static void main(String[] args) throws UnknownHostException {
		URichSequenceAnalysis analysis = new URichSequenceAnalysis();
		analysis.run();
	}
	
	public URichSequenceAnalysis() throws UnknownHostException {
		DB db = new MongoClient().getDB("qgrs");
		jongo = new Jongo(db);
		MongoCollection principals = jongo.getCollection("principals");
		this.uRichCollection = jongo.getCollection("uRich");
		this.mrnaList = principals.find().as(MRNA.class);
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
		MongoSequenceProvider p = new MongoSequenceProvider(jongo);
		mrnaCount++;
		int ucount = 0;
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
			Collection<URich> us = new URichFinder(slice).getAll(3);  // modified to return BEST u rich sequence.
			for ( URich u : us ) {
				ucount++;
				u.startNt = u.distanceFromPolyASite + sites[i].getEnd()+5;
				u.accessionNumber = mrna.getAccessionNumber();
				u.polyASiteNumber = i+1;
				uRichCollection.insert(u);
			}
		}
		
		System.out.println("mrna [" + mrnaCount + "] -> sites [" + sites.length + "] -> u-rich recorded [" + ucount + "]");
	}

}
