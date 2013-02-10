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

}
