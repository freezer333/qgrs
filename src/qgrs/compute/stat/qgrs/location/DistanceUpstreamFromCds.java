package qgrs.compute.stat.qgrs.location;

import qgrs.data.GeneSequence;
import qgrs.data.records.GQuadruplexRecord;

public class DistanceUpstreamFromCds extends QgrsLocationAccumulator {

	final int maxDistance;
	final int minDistance;
	
	public DistanceUpstreamFromCds(int order, int distance) {
		super(order, distance + "nt upstream from Cds");
		this.maxDistance = distance;
		this.minDistance = 0;
	}
	public DistanceUpstreamFromCds(int order, int minDistance, int maxDistance) {
		super(order, minDistance + "-" + maxDistance + "nt upstream from Cds");
		this.maxDistance = maxDistance;
		this.minDistance = minDistance;
	}
	
	@Override
	public boolean isWithin(GQuadruplexRecord qgrs, GeneSequence sequence) {
		int cdsStart = sequence.getCds().getStart();
		int qpos = qgrs.getTetrad1();
		
		if ( qpos > cdsStart ) {
			return false;
		}
		
		int distance = cdsStart - qpos;
		return (distance >= minDistance && distance <= maxDistance);
		
	}


	@Override
	public boolean isApplicable(GeneSequence sequence) {
		if ( sequence.getCds() == null ) return false;
		return sequence.getCds().getStart() >= maxDistance;
	}

	

	@Override
	public double numXNucleotidesInLocation(double x, GeneSequence sequence) {
		return 1; // doesn't make sense to normalize here - use the raw count (no divisor).
		
		// For example, if there are 2 QGRS within 10nt of CDS, it makes no sense to say there are 20 / 100nt
	}


	
	
}
