package qgrs.data.analysis;

import java.net.UnknownHostException;
import java.util.Collection;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.MRNA;
import qgrs.data.providers.MongoSequenceProvider;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class URichCountingAnalysis {
	private final Iterable<MRNA> mrnaList ;
	private final MongoCollection uRichCollection;
	private final DescriptiveStatistics stats = new DescriptiveStatistics();
	private int numPolyWith0 = 0;
	private int numPolyWith1 = 0;
	private int numPolyWithMany = 0;
	
	public static void main(String[] args) throws UnknownHostException {
		DB db = new MongoClient().getDB("qgrs");
		Jongo jongo = new Jongo(db);
		MongoCollection principals = jongo.getCollection("principals");
		MongoCollection uRichCollection = jongo.getCollection("uRich");
		Iterable<MRNA> all = principals.find().as(MRNA.class);
		
		URichCountingAnalysis analysis = new URichCountingAnalysis(all, uRichCollection);
		analysis.run();
	}
	
	public URichCountingAnalysis(Iterable<MRNA> mrna, MongoCollection uRichCollection) {
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
		
		System.out.println("PolyA Sites examined:  " + this.stats.getN());
		System.out.println("Max number U-rich:  " + this.stats.getMax());
		System.out.println("Number PolyA with 0 U-rich:   " + this.numPolyWith0);
		System.out.println("Number PolyA with exactly one 1 U-rich:   " + this.numPolyWith1);
		System.out.println("Number PolyA with multiple U-rich sequences:   " + this.numPolyWithMany);
		System.out.println("Average number of U-rich:  " + this.stats.getMean());
	}
	
	
	private void runMrna(MRNA mrna) throws Exception {
		
		// Only consider mrna with multiple poly A signlls.
		if (mrna.getPolyASites().size() < 2 ) { return; };
		
		System.out.println("Searching " + mrna.getAccessionNumber() + " => " + mrna.getName());
		MongoSequenceProvider p = new MongoSequenceProvider();
		
		String sequence = p.getPrincipalByAccession(mrna.getAccessionNumber()); 
		sequence = sequence.replaceAll("T", "U");

		
		Range [] sites = new Range[mrna.getPolyASites().size()];
		int c = 0;
		for (Range site : mrna.getPolyASites() )  {
			sites[c++] = site;
		}
		
		// note - we are skipping the last polyA site, because we don't have 3' tail sequence data (yet)
		for ( int i = 0; i < sites.length -1; i++ ) {
			int start = sites[i].getEnd()+5;
			int end = (i == sites.length-1) ? mrna.getSequenceLength()-1 : sites[i+1].getStart();
			if ( end - start < 5) continue;
			String slice = sequence.substring(start, end);
			
			Collection<URich> us = new URichFinder(slice).getAll(3);
			if ( us.size() == 0 ) this.numPolyWith0++;
			else if ( us.size() == 1 ) this.numPolyWith1++;
			else { this.numPolyWithMany++; }
			this.stats.addValue(us.size());
		}
	}

}
