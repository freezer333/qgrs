package qgrs.data.analysis;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.G4;
import qgrs.data.mongo.primitives.jongo.MRNA;

public class DistanceBin {

	private DistanceDirection direction;
	private int minBases;
	private int maxBases;
	private G4Filter g4Filter;
	
	private BinCount count = new BinCount();
	
	
	
	
	public BinCount getCount() {
		return count;
	}

	public void setCount(BinCount count) {
		this.count = count;
	}

	public int getMinBases() {
		return minBases;
	}

	public int getMaxBases() {
		return maxBases;
	}

	public DistanceBin(DistanceDirection direction, int minBases,
			int maxBases, G4Filter g4Filter) {
		super();
		this.direction = direction;
		this.minBases = minBases;
		this.maxBases = maxBases;
		this.g4Filter = g4Filter;
	}

	private boolean hasInterval(MRNA mrna, Range range) {
		if ( mrna.getCds() == null ) return false;
		
		if ( direction == DistanceDirection.Upstream ) {
			return ( range.getStart() - this.minBases >= mrna.getCds().getEnd() );
		}
		else {
			return range.getEnd() + this.minBases <= mrna.getSequenceLength();
		}
	}
	
	private boolean within(Range range, G4 g4) {
		Range interval;
		if ( direction == DistanceDirection.Downstream) {
			interval = new Range(range.getStart() - maxBases, range.getStart() - minBases);
		}
		else {
			interval = new Range(range.getEnd() + minBases, range.getEnd() + maxBases);
		}
		return interval.overlapsWith(new Range(g4.getTetrad1(), g4.getTetrad4() + g4.getNumTetrads()));
	}
	
	public void tally(MRNA mrna, Range range) {
		if ( ! hasInterval(mrna, range)) return;
		for ( G4 g4 : mrna.getG4s() ) {
			if ( g4Filter.acceptable(mrna, g4)) {
				if (within(range, g4)) {
					count.signal(true);
					return;
				}
			}
		}
		count.signal(false);
	}
}
