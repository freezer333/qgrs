package qgrs.compute.stat.qgrs.location;

import qgrs.compute.stat.qgrs.QgrsLocationResults;
import qgrs.data.GeneSequence;
import qgrs.data.records.GQuadruplexRecord;

public abstract class QgrsLocationAccumulator implements Comparable {

	public final QgrsLocationResults results = new QgrsLocationResults();
	private int count;
	private int order;
	final private String label;
	
	public QgrsLocationAccumulator(int order, String label) {
		this.order = order;
		this.label = label;
		this.count = 0;
	}
	
	
	


	@Override
	public int compareTo(Object o) {
		QgrsLocationAccumulator other = (QgrsLocationAccumulator) o;
		return new Integer(this.order).compareTo(new Integer(other.order));
	}





	public String getLabel() {
		return label;
	}


	public void setOrder(int order) {
		this.order = order;
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
