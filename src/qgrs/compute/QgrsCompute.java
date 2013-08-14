package qgrs.compute;

import java.util.Date;

import qgrs.compute.gscore.QgrsFinder;
import qgrs.compute.interfaces.GeneralAligner;
import qgrs.compute.interfaces.QgrsHomologyScorer;
import qgrs.data.Base;
import qgrs.data.GeneSequence;
import qgrs.data.InputType;
import qgrs.job.JobStage;
import qgrs.job.StatusHolder;
import framework.diagnostic.MemoryReporter;

public class QgrsCompute{

	private GeneralAligner aligner;
	private QgrsFinder gIdentifier;
	private QgrsHomologyScorer gAligner;
	private InputType inputType;
	private final StatusHolder statusHolder;
	
	public enum SeqTag { Principle, Comparison };
	private boolean principleQuadruplexIdCached = false;
	private boolean comparisonQuadruplexIdCached = false;
	private boolean alignmentCached = false;
	private boolean homologyResultsCached = false;
	
	public QgrsCompute(StatusHolder statusHolder) {
		this.statusHolder = statusHolder;
	}
	

	public void compute(GeneSequencePair pair) throws Exception {
		try {
			this.identifyQgrs(pair.getComparison(), SeqTag.Comparison);
			this.doAlignment(pair);
			this.computeGappedIndexes(pair);
			pair.getPrinciple().groupQuadruplexesIntoFamilies();
			pair.getPrinciple().filterQuadruplexesForBestFamilyRepresentatives();
			pair.getComparison().groupQuadruplexesIntoFamilies();
			pair.getComparison().filterQuadruplexesForBestFamilyRepresentatives();
			this.computeHomologyScores(pair);
		} catch (java.lang.OutOfMemoryError mex) {
			Runtime runtime = Runtime.getRuntime();  
			this.statusHolder.setStatus(JobStage.Error, -1, "Internal memory error, sequence too large for analysis (" + runtime.totalMemory()/(1024*1024) + "mb available).  Please contact the site administrator");
			mex.printStackTrace();
			throw mex;
		
		}
		MemoryReporter.memoryReport();
		return;
	}
	
	void computeHomologyScores(GeneSequencePair pair) throws Exception {
		this.statusHolder.setStatus(JobStage.QGRS_Homology, -1, null);
		this.gAligner.computeQgrsHomology(pair);
	}

	void computeGappedIndexes(GeneSequencePair pair) {
		int i = 0;
		for (Base b:pair.getPrinciple().getBases()){
			b.setIndexWithGaps(i++);
		}
		i = 0;
		for (Base b:pair.getComparison().getBases()){
			b.setIndexWithGaps(i++);
		}
	}
	
	
	
	
	public void identifyQgrs(GeneSequence seq, SeqTag tag) throws Exception{
		this.statusHolder.setStatus(JobStage.QGRS_ID, -1, null);
		this.gIdentifier.populateQgrs(seq);
	}
	
	
	
	
	
	void doAlignment(GeneSequencePair pair) throws Exception {
		this.statusHolder.setStatus(JobStage.Alignment_Sync, -1, null);
		this.aligner.align(pair, this.statusHolder);
		this.alignmentCached = pair.isWasCached();
		pair.setDateAligned(new Date());
	}
	
	
	
	public GeneralAligner getAligner() {
		return aligner;
	}
	public void setAligner(GeneralAligner aligner) {
		this.aligner = aligner;
	}
	public QgrsFinder getgIdentifier() {
		return gIdentifier;
	}
	public void setgIdentifier(QgrsFinder gIdentifier) {
		this.gIdentifier = gIdentifier;
	}
	public QgrsHomologyScorer getgAligner() {
		return gAligner;
	}
	public void setgAligner(QgrsHomologyScorer gAligner) {
		this.gAligner = gAligner;
	}

	public void setInputType(InputType inputType) {
		this.inputType = inputType;
	}


	public InputType getInputType() {
		return inputType;
	}


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
