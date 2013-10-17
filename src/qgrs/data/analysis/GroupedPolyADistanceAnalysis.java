package qgrs.data.analysis;

import java.io.PrintWriter;

import qgrs.data.mongo.primitives.jongo.MRNA;

public class GroupedPolyADistanceAnalysis extends PolyADistanceAnalysis {
	
	
	public GroupedPolyADistanceAnalysis(MrnaFilter mrnaFilter,
			G4Filter g4Filter, int min, int max, int increment,
			DistanceDirection direction) {
		super(mrnaFilter, g4Filter, min, max, increment, direction);
		group = "mrna_polyA";
	}
	@Override
	protected void writeTableHeading1(PrintWriter writer) {
		writer.println("nt. interval    % mRNA signals with at least 1 qgrs\t# mRNA with 1 qgrs / #mRNA with valid interval");
		
	}
	@Override
	protected void writeTableHeading2(PrintWriter writer) {
		writer.println("nt. interval    Avg. # Qgrs\tMedian Qgrs\tQgrsCount\tTotal mRNA");
		
	}
	@Override
	public void evaluate(MRNA mrna) {
		if ( this.mrnaFilter.acceptable(mrna)) {
			for ( DistanceBin bin : bins) {
				bin.multiTally(mrna, mrna.getPolyASignals());
			}
		}
	}
}
