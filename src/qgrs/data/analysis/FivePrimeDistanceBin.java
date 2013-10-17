package qgrs.data.analysis;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.MRNA;

public class FivePrimeDistanceBin extends DistanceBin {

	public FivePrimeDistanceBin(int minBases, int maxBases,	G4Filter g4Filter, DistanceDirection direction) {
		super(direction, minBases, maxBases, g4Filter);
	}


	
	@Override
	protected boolean hasInterval(MRNA mrna, Range range) {
		if (mrna.getCds() == null)
			return false;
		if ( this.direction == DistanceDirection.Upstream ) {
			return (range.getStart() - this.minBases >= 0);
		}
		else {
			return (mrna.getCds().getStart() + this.minBases) <= mrna.getCds().getEnd();
		}
	}

}
