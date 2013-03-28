package qgrs.compute.stat.qgrs.location;

import qgrs.compute.stat.qgrs.QgrsLocationResults;
import qgrs.data.GeneSequence;
import qgrs.data.records.GQuadruplexRecord;

public abstract class QgrsLocationAccumulator implements Comparable {

	public final QgrsLocationResults results = new QgrsLocationResults();
	public final QgrsLocationResults normalizedResults = new QgrsLocationResults();
	
	private int numApplicableGenes = 0;
	private int numGenesWithQgrs = 0;
	
	private int count;
	private int order;
	final private String label;
	
	public QgrsLocationAccumulator(int order, String label) {
		this.order = order;
		this.label = label;
		this.count = 0;
	}
	
	
	

	

	public int getNumApplicableGenes() {
		return numApplicableGenes;
	}


	public void setNumApplicableGenes(int numApplicableGenes) {
		this.numApplicableGenes = numApplicableGenes;
	}


	public int getNumGenesWithQgrs() {
		return numGenesWithQgrs;
	}


	public void setNumGenesWithQgrs(int numGenesWithQgrs) {
		this.numGenesWithQgrs = numGenesWithQgrs;
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


	public void startGene() {
		this.count = 0;
	}
	
	public void finishGene(GeneSequence seq) {
		results.addValue(count);
		double nt100Units = this.numXNucleotidesInLocation(100.0, seq);
		normalizedResults.addValue(count / nt100Units);
		this.numApplicableGenes++;
		if ( count > 0 ) {
			this.numGenesWithQgrs++;
		}
	}
	
	public void offer(GQuadruplexRecord qgrs, GeneSequence sequence) {
		if ( isWithin(qgrs, sequence)) {
			count++;
		}
	}
	
	public abstract boolean isWithin(GQuadruplexRecord qgrs, GeneSequence sequence);
	public abstract boolean isApplicable(GeneSequence sequence);
	public abstract double numXNucleotidesInLocation(double x, GeneSequence sequence);
	
}
