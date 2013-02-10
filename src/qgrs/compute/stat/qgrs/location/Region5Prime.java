package qgrs.compute.stat.qgrs.location;

import qgrs.data.GeneSequence;
import qgrs.data.records.GQuadruplexRecord;

public class Region5Prime extends QgrsLocationAccumulator {

	public Region5Prime(int order) {
		super(order, "5'UTR");
	}

	@Override
	public boolean isWithin(GQuadruplexRecord qgrs, GeneSequence sequence) {
		return qgrs.isIn5Prime();
	}

}
