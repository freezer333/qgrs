package qgrs.compute.stat.qgrs.location;

import qgrs.compute.stat.qgrs.QgrsLocationResults;
import qgrs.data.GeneSequence;
import qgrs.data.records.GQuadruplexRecord;

public abstract class QgrsLocationAccumulator {

	public final QgrsLocationResults results = new QgrsLocationResults();
	private int count;
	final private int order;
	final private String label;
	
	public QgrsLocationAccumulator(int order, String label) {
		this.order = order;
		this.label = label;
		this.count = 0;
	}
	
	
	public String getLabel() {
		return label;
	}


	public int getOrder() {
		return order;
	}


	public void startAccumulator() {
		this.count = 0;
	}
	
	public void finishAccumulation() {
		results.addValue(count);
	}
	
	public void offer(GQuadruplexRecord qgrs, GeneSequence sequence) {
		if ( isWithin(qgrs, sequence)) {
			count++;
		}
	}
	
	public abstract boolean isWithin(GQuadruplexRecord qgrs, GeneSequence sequence);
	
}
