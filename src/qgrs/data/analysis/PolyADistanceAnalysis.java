package qgrs.data.analysis;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.MRNA;

public class PolyADistanceAnalysis extends Analysis {

	DistanceDirection direction;
	int min;
	int max;
	int increment;
	ArrayList<DistanceBin> bins;
	boolean distalOnly = false;
	boolean proximateOnly = false;
	G4Filter g4Filter;
	
	public PolyADistanceAnalysis(MrnaFilter mrnaFilter, G4Filter g4Filter, int min, int max, int increment, DistanceDirection direction) {
		super("polyA", mrnaFilter);
		this.min = min;
		this.max = max;
		this.increment = increment;
		this.direction = direction;
		this.bins = new ArrayList<DistanceBin>();
		this.g4Filter = g4Filter;
		for ( int i = min; i < max; i+= increment) {
			bins.add(new PolyADistanceBin(direction, i, i+increment, g4Filter));
		}
	}
	
	public boolean isDistalOnly() {
		return distalOnly;
	}

	public PolyADistanceAnalysis setDistalOnly(boolean distalOnly) {
		this.distalOnly = distalOnly;
		return this;
	}

	public boolean isProximateOnly() {
		return proximateOnly;
	}

	public PolyADistanceAnalysis setProximateOnly(boolean proximateOnly) {
		this.proximateOnly = proximateOnly;
		return this;
	}

	@Override
	public void evaluate(MRNA mrna) {
		if ( this.mrnaFilter.acceptable(mrna)) {
			for ( DistanceBin bin : bins) {
				if ( distalOnly && mrna.getPolyASignals().size() > 1) {
					bin.tally(mrna, (Range)mrna.getPolyASignals().toArray()[mrna.getPolyASignals().size()-1]);
				}
				else if ( proximateOnly  && mrna.getPolyASignals().size() > 1) {
					bin.tally(mrna, (Range)mrna.getPolyASignals().toArray()[0]);
				}
				else {
					for ( Range poly : mrna.getPolyASignals())  {
						bin.tally(mrna, poly);
					}
				}
			}
		}
	}
	
	private String polyEndString() {
		if ( this.distalOnly ) return " Distal ";
		if ( this.proximateOnly ) return " Proximate ";
		return "";
	}
	protected void writeTableHeading1(PrintWriter writer) {
		writer.println("nt. interval    % polyA signals with at least 1 qgrs\t# poly with 1 qgrs / #poly with valid interval");
		
	}
	protected void writeTableHeading2(PrintWriter writer) {
		writer.println("nt. interval    Avg. # Qgrs\tMedian Qgrs\tQgrsCount\tTotal PolyA");
		
	}
	
	@Override
	public void report() {
		
		PrintWriter writer;
		try {
			mkdirs();
			String filename = dirPrefix() + "PolyADistanceAnalysis - " + this.polyEndString() + this.direction + "-" + this.mrnaFilter.toString() + this.g4Filter.toString() + ".txt";
			writer = new PrintWriter(filename, "UTF-8");
			writer.println("-----------------------------------");
			writer.println("PolyA Signal analyis - " + this.polyEndString() + this.direction.toString());
			writer.println("Criteria summary");
			writer.println(this.mrnaFilter.toString());
			this.writeTableHeading1(writer);
			writer.println("-----------------------------------");
			DecimalFormat f = new DecimalFormat("0.00%");
			for ( DistanceBin bin : bins) {
				writer.println(String.format("%3d", bin.getMinBases()) + "-" + String.format("%3d", bin.getMaxBases()) + 
						":\t" + f.format(bin.getCount().measure()) + "\t\t\t" + bin.getCount().getTotalWithG4() + "\t" + bin.getCount().getTotal() + "");
			}
			writer.println("-----------------------------------");
			
			
			this.writeTableHeading2(writer);
			writer.println("-----------------------------------");
			f = new DecimalFormat("0.000");
			for ( DistanceBin bin : bins) {
				writer.println(String.format("%3d", bin.getMinBases()) + "-" + String.format("%3d", bin.getMaxBases()) + 
						":\t" + f.format(bin.getCount().mean()) + "\t\t\t" + bin.getCount().median() + "\t" + bin.getCount().sum() + "\t" + bin.getCount().getTotal() + "");
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
