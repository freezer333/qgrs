package qgrs.data.analysis;

import java.io.PrintWriter;
import java.text.DecimalFormat;

import qgrs.data.Range;

public class AggregateReporter implements Reporter {

	CountingAnalysis analysis = null;
	
	public AggregateReporter(CountingAnalysis ca ) {
		this.analysis = ca;
	}
	
	CountingSet find(Range tetrad, Region region, String organism) {
		for ( CountingSet cs : this.analysis.getCountingSet() ) {
			if ( cs.getConserved().getTetradMax() != tetrad.getEnd() ) continue;
			if ( cs.getConserved().getTetradMin() != tetrad.getStart() ) continue;
			if ( !cs.getMrnaFilter().getHomologList().contains(organism)) continue;
			if ( cs.getG4Filter().getRegion() != region ) continue;
			return cs;
		}
		return null;
		
	}
	
	@Override
	public void report(PrintWriter writer) {
		Range [] tetradRanges = { new Range(2, Integer.MAX_VALUE), new Range(2, 2), new Range(3, 3), new Range(4, Integer.MAX_VALUE)};
		Region [] regions = { Region.Any, Region.FivePrime, Region.Cds, Region.ThreePrime};
		String [] organisms = { 
				"Pan troglodytes", 
				"Canis lupus familiaris", 
				"Mus musculus", 
				"Danio rerio", 
				"Drosophila melanogaster", 
				"Caenorhabditis elegans", 
				"Kluyveromyces lactis NRRL Y-1140"};
		
		for ( String organisim : organisms) {
			writer.print(organisim + "(" + find(tetradRanges[0], regions[0], organisim).totalMrna + ")\t"); for ( Range tetrad : tetradRanges ) { writer.print(tetrad.toString() + "\t");}
			writer.println();
			for (Region region: regions ) { 
				writer.print(region + "\t");
				for ( Range tetrads : tetradRanges ) {
					CountingSet cs = find(tetrads, region, organisim);
					writer.print(cs.totalConservedG4 + "\t"  );
				}
				writer.println();
			}
			
		}
		writer.println("-------------------------------------------------------------");
		for ( String organisim : organisms) {
			writer.print(organisim + "(" + find(tetradRanges[0], regions[0], organisim).totalMrna + ")\t"); for ( Range tetrad : tetradRanges ) { writer.print(tetrad.toString() + "\t");}
			writer.println();
			for (Region region: regions ) { 
				writer.print(region + "\t");
				for ( Range tetrads : tetradRanges ) {
					CountingSet cs = find(tetrads, region, organisim);
					writer.print(new DecimalFormat("0.0%").format(((double)cs.totalConservedG4) / cs.totalG4)+ "\t"  );
				}
				writer.println();
			}
			
		}
		
		writer.close();
	}
	
	

}
