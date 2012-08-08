package qgrs.output;

import java.util.List;

import qgrs.compute.GeneSequencePair;
import qgrs.data.QgrsHomology;

public abstract class ResultProcessor {

	private boolean principleQuadruplexIdCached = false;
	private boolean comparisonQuadruplexIdCached = false;
	private boolean alignmentCached = false;
	private boolean homologyResultsCached = false;
	
	public abstract void handleResults(List<GeneSequencePair> pairs, List<QgrsHomology> similarityResults) ;

	public boolean isPrincipleQuadruplexIdCached() {
		return principleQuadruplexIdCached;
	}

	public void setPrincipleQuadruplexIdCached(boolean principleQuadruplexIdCached) {
		this.principleQuadruplexIdCached = principleQuadruplexIdCached;
	}

	public boolean isComparisonQuadruplexIdCached() {
		return comparisonQuadruplexIdCached;
	}

	public void setComparisonQuadruplexIdCached(boolean comparisonQuadruplexIdCached) {
		this.comparisonQuadruplexIdCached = comparisonQuadruplexIdCached;
	}

	public boolean isAlignmentCached() {
		return alignmentCached;
	}

	public void setAlignmentCached(boolean alignmentCached) {
		this.alignmentCached = alignmentCached;
	}

	public boolean isHomologyResultsCached() {
		return homologyResultsCached;
	}

	public void setHomologyResultsCached(boolean homologyResultsCached) {
		this.homologyResultsCached = homologyResultsCached;
	}
	
	
}
