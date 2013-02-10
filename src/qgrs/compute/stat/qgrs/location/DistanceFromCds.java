package qgrs.compute.stat.qgrs.location;

import qgrs.data.GeneSequence;
import qgrs.data.records.GQuadruplexRecord;

public class DistanceFromCds extends QgrsLocationAccumulator {

	final int distance;
	
	public DistanceFromCds(int order, int distance) {
		super(order, distance + "nt from Cds");
		this.distance = distance;
	}
	
	
	@Override
	public boolean isWithin(GQuadruplexRecord qgrs, GeneSequence sequence) {
		int cdsStart = sequence.getCds().getStart();
		return ( (cdsStart - qgrs.getGQEnd()) <= distance  && (cdsStart - qgrs.getGQEnd()) > 0);
	}

}
