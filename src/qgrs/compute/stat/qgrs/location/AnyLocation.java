package qgrs.compute.stat.qgrs.location;

import qgrs.data.GeneSequence;
import qgrs.data.records.GQuadruplexRecord;

public class AnyLocation extends QgrsLocationAccumulator {

	public AnyLocation(int order) {
		super(order, "Any Location");
	}

	@Override
	public boolean isWithin(GQuadruplexRecord qgrs, GeneSequence sequence) {
		return true;
	}

	@Override
	public boolean isApplicable(GeneSequence sequence) {
		return true;
	}

	@Override
	public double numXNucleotidesInLocation(double x, GeneSequence sequence) {
		return sequence.getSequenceLength() / x;
	}

	
	
	

}
