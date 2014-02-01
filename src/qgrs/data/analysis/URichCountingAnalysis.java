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
	private final DescriptiveStatistics statsLimit = new DescriptiveStatistics();
	private final DescriptiveStatistics statsAll = new DescriptiveStatistics();
	private Counts all = new Counts();
	private Counts limit = new Counts();
	
	Jongo jongo;
	
	class Counts{
		public int numPolyWith0 = 0;
		public int numPolyWith1 = 0;
		public int numPolyWithMany = 0;
	}
	
	public static void main(String[] args) throws UnknownHostException {
		DB db = new MongoClient().getDB("qgrs");
		Jongo jongo = new Jongo(db);
		MongoCollection principals = jongo.getCollection("principals");
		MongoCollection uRichCollection = jongo.getCollection("uRich");
		Iterable<MRNA> all = principals.find().as(MRNA.class);
		
		URichCountingAnalysis analysis = new URichCountingAnalysis(jongo, all, uRichCollection);
		analysis.run();
	}
	
	public URichCountingAnalysis(Jongo jongo, Iterable<MRNA> mrna, MongoCollection uRichCollection) {
		this.mrnaList = mrna;
		this.uRichCollection = uRichCollection;
		this.jongo = jongo;
	}
	
	public void run() {
		int i = 0;
		for ( MRNA mrna : mrnaList ) {
			try {
				runMrna(mrna, i++);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("\n\n-------------------------------------------------------");
		System.out.println("U-Rich sequence stats - up to 50nt downstream of polyA");
		report(statsLimit, limit);
		System.out.println("\n\n-------------------------------------------------------");
		System.out.println("\n\n-------------------------------------------------------");
		System.out.println("U-Rich sequence stats - up to next polyA (or end of sequence)");
		report(statsAll, all);
		System.out.println("\n\n-------------------------------------------------------");
		
	}
	
	private void report(DescriptiveStatistics stats, Counts counts) {
		System.out.println("PolyA Sites examined:  " + stats.getN());
		System.out.println("Max number U-rich:  " +stats.getMax());
		System.out.println("Number PolyA with 0 U-rich:   " + counts.numPolyWith0);
		System.out.println("Number PolyA with exactly one 1 U-rich:   " + counts.numPolyWith1);
		System.out.println("Number PolyA with multiple U-rich sequences:   " + counts.numPolyWithMany);
		System.out.println("Average number of U-rich:  " + stats.getMean());
		System.out.println("Average number of U-rich:  " + stats.getPercentile(50));
	}
	
	private void runMrna(MRNA mrna, int i) throws Exception {
		
		// Only consider mrna with multiple poly A signlls.
		if (mrna.getPolyASites().size() < 2 ) { return; };
		
		MongoSequenceProvider p = new MongoSequenceProvider(jongo);
		
		String sequence = p.getPrincipalByAccession(mrna.getAccessionNumber()); 
		sequence = sequence.replaceAll("T", "U");

		
		Range [] sites = new Range[mrna.getPolyASites().size()];
		int c = 0;
		for (Range site : mrna.getPolyASites() )  {
			sites[c++] = site;
		}
		
		System.out.println("Counting (" + i + ") -> "  + mrna.getAccessionNumber());
		// note - we are skipping the last polyA site, because we don't have 3' tail sequence data (yet)
		countUs(mrna, sequence, sites, null, statsAll, all);
		countUs(mrna, sequence, sites, 50, statsLimit, limit);
	}

	private void countUs(MRNA mrna, String sequence, Range[] sites, Integer limit, DescriptiveStatistics stats, Counts counts) {
		for ( int i = 0; i < sites.length -1; i++ ) {
			int start = sites[i].getEnd()+5;
			boolean isLast = i == sites.length-1;
			int end = getEnd(isLast ? null : sites[i+1], mrna.getSequenceLength(), start, limit, isLast);
			if ( end - start < 5) continue;
			String slice = sequence.substring(start, end);
			
			Collection<URich> us = new URichFinder(slice).getAll(3);
			if ( us.size() == 0 ) counts.numPolyWith0++;
			else if ( us.size() == 1 ) counts.numPolyWith1++;
			else { counts.numPolyWithMany++; }
			stats.addValue(us.size());
			System.out.println("\tSite " + (i+1) + " has " + us.size() + " U-rich sequences");
		}
	}
	
	
	
	int getEnd(Range nextSite, int totalSequenceLength, int start, Integer limit, boolean isLast) {
		int hardLimit = isLast ? totalSequenceLength : nextSite.getStart();
		if ( limit != null ) {
			return getEndWithLimit(start, limit, hardLimit);
		}
		else {
			return hardLimit;
		}
	}
	int getEndWithLimit(int start, int limit, int hardLimit) {
		int end = start + limit;
		if ( end >= hardLimit ) end = hardLimit;
		return end;
	}
	

}
