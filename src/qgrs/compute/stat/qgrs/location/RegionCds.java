package qgrs.compute.stat.qgrs.location;

import qgrs.data.GeneSequence;
import qgrs.data.records.GQuadruplexRecord;

public class RegionCds extends QgrsLocationAccumulator {

	public RegionCds(int order) {
		super(order, "CDS");
	}

	@Override
	public boolean isWithin(GQuadruplexRecord qgrs, GeneSequence sequence) {
		return qgrs.isInCds();
	}

	@Override
	public boolean isApplicable(GeneSequence sequence) {
		return sequence.getCds() != null && sequence.getCds().getEnd() > sequence.getCds().getStart();
	}
	
	@Override
	public double numXNucleotidesInLocation(double x, GeneSequence sequence) {
		if ( isApplicable(sequence)) {
			return (sequence.getCds().getEnd() - sequence.getCds().getStart())/x;
		}
		return 1;
	}

	
}
