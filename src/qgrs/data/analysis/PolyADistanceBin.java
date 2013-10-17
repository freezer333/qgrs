package qgrs.data.analysis;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.MRNA;

public class PolyADistanceBin extends DistanceBin {

	public PolyADistanceBin(DistanceDirection direction, int minBases, int maxBases, G4Filter g4Filter) {
		super(direction, minBases, maxBases, g4Filter);
	}
	
	@Override
	protected boolean hasInterval(MRNA mrna, Range range) {
		if (mrna.getCds() == null)
			return false;

		if (direction == DistanceDirection.Upstream) {
			return (range.getStart() - this.minBases >= mrna.getCds().getEnd());
		} else {
			return range.getEnd() + this.minBases <= mrna.getSequenceLength();
		}
	}
}
