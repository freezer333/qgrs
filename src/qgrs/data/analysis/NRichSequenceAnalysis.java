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

public class NRichSequenceAnalysis {
	private final Iterable<MRNA> mrnaList ;
	private final MongoCollection nRichCollection;
	private int mrnaCount = 0;
	Jongo jongo;
	int [] polyADataCount = new int [100];
	public static int test =0;
	
	
	public static void main(String[] args) throws UnknownHostException {
		NRichSequenceAnalysis analysis = new NRichSequenceAnalysis();
		analysis.run();
		System.out.println ("**************************************");
		System.out.println (" "+test);
	    int [] pA = analysis.getPA();
		for (int i = 0; i < 100; i ++)
			System.out.println (i + "\t" + pA [i]);
	}
	
	public NRichSequenceAnalysis() throws UnknownHostException {
		DB db = new MongoClient().getDB("qgrs");
		jongo = new Jongo(db);
		MongoCollection principals = jongo.getCollection("principals");
		this.nRichCollection = jongo.getCollection("nRich");
		this.mrnaList = principals.find().as(MRNA.class);
	}
	
	public void run() {
		int i = 0;
		for ( MRNA mrna : mrnaList ) {
			try {
				runMrna(mrna);
				i++;
				
				//if ( i > 100 ) return;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private int [] getPA ()
	{
		return polyADataCount;
	}
	
	private void runMrna(MRNA mrna) throws Exception {
		MongoSequenceProvider p = new MongoSequenceProvider(jongo);
		mrnaCount++;
		String sequence = p.getPrincipalByAccession(mrna.getAccessionNumber()); 
		sequence = sequence.replaceAll("T", "U");
		
		
		Range [] sites = new Range[mrna.getPolyASites().size()];
		int c = 0;
		for (Range site : mrna.getPolyASites() )  {
			sites[c++] = site;
		}
		
		char [] ns = {'U', 'G', 'A', 'C'};
		
		// don't examine the last site
		for ( int i = 0; i < sites.length-1; i++ ) {
			int start = sites[i].getEnd()+5;
			int end = (i == sites.length-1) ? mrna.getSequenceLength()-1 : sites[i+1].getStart();
			if ( end - start < 5) continue;
			String slice = sequence.substring(start, end);
			
			int length = slice.length ();
			
			if (length > 500)
				length = 500;
			
			int amt = length/5 -1;
			
			if (amt == -1)
				polyADataCount[0]++;
			
			for (int k = 0; k < amt; k ++)
				polyADataCount [k] ++;
			
			
			for ( char n :ns ) {
				Collection<NRich> us = new NRichFinder(n, slice).getAll(3);  
				for ( NRich u : us ) {
					u.startNt = u.distanceFromPolyASite + sites[i].getEnd()+5;
					u.accessionNumber = mrna.getAccessionNumber();
					u.polyASiteNumber = i+1;
					nRichCollection.insert(u);
				}
			}
		}
		
		System.out.println("mrna [" + mrnaCount + "] -> sites [" + sites.length + "] ");
		if (sites.length != 0)
			test ++;
			
		
	
		
	}
}
