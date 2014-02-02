package qgrs.data.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.MRNA;

public class FivePrimeAnalysis extends Analysis {

DistanceDirection direction;
	
	int min;
	int max;
	int increment;
	ArrayList<DistanceBin> bins;
	
	public FivePrimeAnalysis(MrnaFilter mrnaFilter, G4Filter g4Filter, int min, int max, int increment, DistanceDirection direction) {
		super("5Prime", mrnaFilter);
		this.min = min;
		this.max = max;
		this.increment = increment;
		this.direction = direction;
		this.bins = new ArrayList<DistanceBin>();
		
		for ( int i = min; i < max; i+= increment) {
			bins.add(new FivePrimeDistanceBin(i, i+increment, g4Filter, direction));
		}
		
	}
	
	@Override
	public void evaluate(MRNA mrna) {
		if ( this.mrnaFilter.acceptable(mrna)) {
			for ( DistanceBin bin : bins) {
				bin.tally(mrna, new Range(mrna.getCds().getStart()));
			}
		}
	}

	@Override
	public void report() {
		PrintWriter writer;
		try {
			mkdirs();
			String filename = dirPrefix() + "5' Analysis - " + mrnaFilter.toString() + " - " + this.direction + ".txt";
			writer = new PrintWriter(filename, "UTF-8");
			writer.println("-----------------------------------");
			writer.println("MRNA Criteria:"  );
			writer.println(this.mrnaFilter.toString());
			writer.println("nt. interval    % mrna with at least 1 qgrs\t# mrna with 1 qgrs / #mrna with valid interval");
			writer.println("-----------------------------------");
			DecimalFormat f = new DecimalFormat("0.00%");
			for ( DistanceBin bin : bins) {
				writer.println(String.format("%3d", bin.getMinBases()) + "-" + String.format("%3d", bin.getMaxBases()) + 
						":\t" + f.format(bin.getCount().measure()) + "\t\t\t" + bin.getCount().getTotalWithG4() + "\t" + bin.getCount().getTotal() + "");
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
