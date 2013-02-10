package qgrs.compute.stat.qgrs.location.sets;

import java.util.LinkedList;

import qgrs.compute.stat.qgrs.location.QgrsLocationAccumulator;
import qgrs.data.GeneSequence;
import qgrs.data.records.GQuadruplexRecord;

public class QgrsLocationSet extends LinkedList<QgrsLocationAccumulator> {

	
	public void startAccumulators() {
		for ( QgrsLocationAccumulator loc : this ) {
			loc.startAccumulator();
		}
	}
	
	public void record(GQuadruplexRecord qgrs, GeneSequence seq) {
		for ( QgrsLocationAccumulator loc : this ) {
			loc.offer(qgrs, seq);
		}
	}
	
	public void finishAccumulators() {
		for ( QgrsLocationAccumulator loc : this ) {
			loc.finishAccumulation();
		}
	}
	
}
