package qgrs.compute.stat.qgrs.location;

import qgrs.data.GeneSequence;
import qgrs.data.records.GQuadruplexRecord;

public class Region3Prime  extends QgrsLocationAccumulator{

	public Region3Prime(int order) {
		super(order, "3'UTR");
	}

	@Override
	public boolean isWithin(GQuadruplexRecord qgrs, GeneSequence sequence) {
		return qgrs.isIn3Prime();
	}

	@Override
	public boolean isApplicable(GeneSequence sequence) {
		return sequence.getCds() != null && sequence.getCds().getEnd() < sequence.getSequenceLength();
	}

	@Override
	public double numXNucleotidesInLocation(double x, GeneSequence sequence) {
		if ( isApplicable(sequence)) {
			return (sequence.getSequenceLength() - sequence.getCds().getEnd())/x;
		}
		return 1;
	}

	
}
