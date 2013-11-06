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
	
	@Override
	public void evaluate(MRNA mrna) {
		
		
		for ( CountingSet count : this.countingSet ) {
			int totalConservedG4 = 0;
			for (G4 g4 : mrna.getG4s() ) {
				if ( count.conserved.acceptable(mrna, g4)) totalConservedG4++;
			}
			count.pop_statistics.addValue(totalConservedG4);
			
			
			if ( count.mrnaFilter.acceptable(mrna)) {
				count.totalMrna++;
				for (G4 g4 : mrna.getG4s() ) {
					if ( count.g4Filter.acceptable(mrna, g4)) count.totalG4++;
					if ( count.conserved.acceptable(mrna, g4)) count.totalConservedG4++;
				}
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
						"population mean\tpopulation std\tz-score\tsignficant (alpha = 0.05)");
			writer.println("\t-----------------------------------");
			for ( CountingSet set : this.countingSet ) {
				writer.print(set.mrnaFilter.getName()+"\t");
				writer.print(set.conserved.getHomologyLabel() + "\t");
				writer.print(set.conserved.getRegionLabel() + "\t");
				writer.print(set.conserved.getTetradLabel() + "\t");
				writer.print(set.conserved.getScore3Range() + "\t");
				writer.print(set.totalMrna + "\t" + set.totalG4 + "\t" + set.totalConservedG4 + "\t" + 
						new DecimalFormat("0.0%").format(set.totalConservedG4 / set.totalG4) + "\t");
				
				
				writer.print(set.meanConserved() +"\t" + set.pop_statistics.getMean() + "\t"  + set.pop_statistics.getStandardDeviation() + "\t");
				writer.println(+ set.zScore() + "\t" +  (set.isSignificant() ? "YES" : "NO"));
				
			}
			writer.println("-----------------------------------");
			System.out.println("Output written to " + filename);
			writer.close();
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
