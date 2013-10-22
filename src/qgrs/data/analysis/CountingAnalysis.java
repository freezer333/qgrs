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
			writer.println("-----------------------------------");
			for ( CountingSet set : this.countingSet ) {
				writer.print(set.mrnaFilter.getName()+"\t");
				writer.print(set.mrnaFilter.getHomologString() + "\t");
				writer.print(set.conserved.getHomologyLabel() + "\t");
				writer.print(set.conserved.getRegionLabel() + "\t");
				writer.print(set.conserved.getTetradLabel() + "\t");
				writer.print(set.conserved.getScore3Range() + "\t");
				writer.println(set.totalMrna + "\t" + set.totalG4 + "\t" + set.totalConservedG4 + "\t" + 
						new DecimalFormat("0.0%").format(set.totalConservedG4 / set.totalG4));
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
