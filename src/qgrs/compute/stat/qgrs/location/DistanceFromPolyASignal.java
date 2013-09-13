package qgrs.compute.stat.qgrs.location;

import java.util.List;

import qgrs.data.GeneSequence;
import qgrs.data.Range;
import qgrs.data.records.GQuadruplexRecord;

public class DistanceFromPolyASignal extends QgrsLocationAccumulator {
	final int maxDistance;
	final int minDistance;
	
	public DistanceFromPolyASignal(int order, int distance) {
		super(order, distance + "nt from PolyA Signal");
		this.maxDistance = distance;
		this.minDistance = 0;
	}
	public DistanceFromPolyASignal(int order, int minDistance, int maxDistance) {
		super(order, minDistance + "-" + maxDistance + "nt from PolyA Signal");
		this.maxDistance = maxDistance;
		this.minDistance = minDistance;
	}
	
	@Override
	public boolean isWithin(GQuadruplexRecord qgrs, GeneSequence sequence) {
		
		List<Range> polyASignals = sequence.getPolyASignals();
		int gqStartpos = qgrs.getTetrad1();
		int gqEndpos = qgrs.getGQEnd();
		
		int distance = -101;
		for(int i=0;i < polyASignals.size();i++)
		{
			if (gqStartpos < polyASignals.get(i).getStart()){
				distance = gqStartpos - polyASignals.get(i).getStart();
			}
			if (gqEndpos >= polyASignals.get(i).getStart()){
				distance = polyASignals.get(i).getEnd() - gqEndpos;
			}
			
			return (distance >= minDistance && distance <= maxDistance);
		}
		
		return false;
	}


	@Override
	public boolean isApplicable(GeneSequence sequence) {
		if ( sequence.getPolyASignals() == null ) return false;
		List<Range> polyASignals = sequence.getPolyASignals();
			for (int i = 0; i < polyASignals.size();i++){
				return polyASignals.get(i).getStart() >= maxDistance;
			}
		return false;
	}

	

	@Override
	public double numXNucleotidesInLocation(double x, GeneSequence sequence) {
		return 1; // doesn't make sense to normalize here - use the raw count (no divisor).
		
		// For example, if there are 2 QGRS within 10nt of polyASignal, it makes no sense to say there are 20 / 100nt
	}


	
	
}
