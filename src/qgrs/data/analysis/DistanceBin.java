package qgrs.data.analysis;

import java.util.Collection;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.G4;
import qgrs.data.mongo.primitives.jongo.MRNA;

public abstract class DistanceBin {

	protected DistanceDirection direction;
	protected int minBases;
	protected int maxBases;
	protected G4Filter g4Filter;

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

	public DistanceBin(DistanceDirection direction, int minBases, int maxBases,	G4Filter g4Filter) {
		super();
		this.direction = direction;
		this.minBases = minBases;
		this.maxBases = maxBases;
		this.g4Filter = g4Filter;
	}

	
	protected abstract boolean hasInterval(MRNA mrna, Range range);

	private boolean within(Range range, G4 g4) {
		Range interval;
		if (direction == DistanceDirection.Upstream) {
			interval = new Range(range.getStart() - maxBases, range.getStart()- minBases);
		} else {
			interval = new Range(range.getEnd() + minBases, range.getEnd() + maxBases);
		}
		return interval.overlapsWith(new Range(g4.getTetrad1(), g4.getTetrad4()	+ g4.getNumTetrads()));
	}

	public void tally(MRNA mrna, Range range) {
		if (!hasInterval(mrna, range))
			return;
		int num = 0;
		for (G4 g4 : mrna.getG4s()) {
			if (g4Filter.acceptable(mrna, g4)) {
				if (within(range, g4)) {
					num++;
				}
			}
		}
		count.record(num);
	}
	public void multiTally(MRNA mrna, Collection<Range> ranges) {
		boolean hasInterval = false;
		for ( Range range : ranges) {
			if (hasInterval(mrna, range))
				hasInterval = true;
		}
		if ( !hasInterval ) return;
		int num = 0;
		for (G4 g4 : mrna.getG4s()) {
			if (g4Filter.acceptable(mrna, g4)) {
				boolean found = false;
				for ( Range range : ranges) {
					if (within(range, g4)) {
						found = true;
					}
				}
				if (found ) num++;
			}
		}
		count.record(num);
	}
}
