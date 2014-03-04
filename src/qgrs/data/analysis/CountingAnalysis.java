package qgrs.data.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.LinkedList;

import qgrs.data.mongo.primitives.jongo.G4;
import qgrs.data.mongo.primitives.jongo.MRNA;

public class CountingAnalysis extends Analysis{
	
	LinkedList<CountingSet> countingSet = new LinkedList<CountingSet>();
	Reporter reporter = null;
	String name = "Counting Analysis";
	public CountingAnalysis() {
		super("counting", null);
	}
	public CountingAnalysis(String name) {
		super("counting", null);
		this.name = name;
	}
	
	public void addCountingSet(CountingSet set) {
		countingSet.add(set);
	}
	
	
	private double per100Nt(int count, G4Filter filter, MRNA mrna) {
		return count / filter.getValidNtLength(mrna) * 10;
	}
	@Override
	public void evaluate(MRNA mrna) {

		for ( CountingSet count : this.countingSet ) {
			int totalConservedG4 = 0;
			
			for (G4 g4 : mrna.getG4s() ) {
				if ( count.conserved.acceptable(mrna, g4)) totalConservedG4++;
			}
			count.raw_pop_statistics.addValue(totalConservedG4);
			count.normed_pop_statistics.addValue (per100Nt(totalConservedG4, count.g4Filter, mrna));
			
			int matchedG4 = 0;
			int matchedConservedG4 = 0;
			if ( count.mrnaFilter.acceptable(mrna)) {
				count.totalMrna++;
				for (G4 g4 : mrna.getG4s() ) {
					if ( count.g4Filter.acceptable(mrna, g4)) matchedG4++;
					if ( count.conserved.acceptable(mrna, g4)) matchedConservedG4++;
				}
				count.totalG4 += matchedG4;
				count.totalConservedG4 += matchedConservedG4;
				count.totalG4Per100nt += per100Nt(matchedG4, count.g4Filter, mrna);
				count.totalConservedG4Per100nt += per100Nt(matchedConservedG4, count.g4Filter, mrna);
			}
		}
	}
	

	public LinkedList<CountingSet> getCountingSet() {
		return countingSet;
	}
	public Reporter getReporter() {
		return reporter;
	}
	public void setReporter(Reporter reporter) {
		this.reporter = reporter;
	}
	@Override
	public void report() {
		
		PrintWriter writer;
		try {
			mkdirs();
			String filename = dirPrefix() + name + ".txt";
			writer = new PrintWriter(filename, "UTF-8");
			if ( this.reporter != null ) {
				this.reporter.report(writer);
				return;
			}
			writer.print("Label\tqgrs conservation\tregion\ttetrads\tgscores\ttotal mrna\ttotal qgrs\ttotal conserved qgrs\t%conserved\taverage conserved qgrs\t" + 
						"population mean\tpopulation std\tz-score\tsignficant (alpha = 0.05)\t");
			writer.print("Avg. qgrs/100nt (sample)\tAvg. qgrs/100nt (pop)\tpop std\tz-score\tsignificant (alpha = 0.05\t");
			writer.println("\t-----------------------------------");
			for ( CountingSet set : this.countingSet ) {
				writer.print(set.mrnaFilter.getName()+"\t");
				writer.print(set.conserved.getHomologyLabel() + "\t");
				writer.print(set.conserved.getRegionLabel() + "\t");
				writer.print(set.conserved.getTetradLabel() + "\t");
				writer.print(set.conserved.getScore3Range() + "\t");
				writer.print(set.totalMrna + "\t" + set.totalG4 + "\t" + set.totalConservedG4 + "\t" + 
						new DecimalFormat("0.0%").format(set.totalConservedG4 / set.totalG4) + "\t");
				
				
				writer.print(set.meanConserved() +"\t" + set.raw_pop_statistics.getMean() + "\t"  + set.raw_pop_statistics.getStandardDeviation() + "\t");
				writer.print(+ set.zScore() + "\t" +  (set.isSignificant() ? "YES" : "NO") + "\t");
				
				writer.print(set.meanConservedPer100nt() +"\t" + set.normed_pop_statistics.getMean() + "\t"  + set.normed_pop_statistics.getStandardDeviation() + "\t");
				writer.println(+ set.zScoreNormalized() + "\t" +  (set.isSignificantNormalized() ? "YES" : "NO") + "\t");
				
				
				
			}
			writer.println("-----------------------------------");
			System.out.println("Output written to " + filename);
			writer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
