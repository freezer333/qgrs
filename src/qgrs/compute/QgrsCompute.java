package qgrs.compute;

import java.util.Date;
import java.util.List;

import qgrs.data.Base;
import qgrs.data.GQuadruplex;
import qgrs.data.GeneSequence;
import qgrs.data.InputType;
import qgrs.data.QgrsHomology;
import qgrs.data.cache.Cache;
import qgrs.data.records.AlignmentRecord;
import qgrs.data.records.QgrsHomologyRecord;
import qgrs.job.JobStage;
import qgrs.job.StatusHolder;
import framework.diagnostic.MemoryReporter;

public class QgrsCompute{

	private GeneralAligner aligner;
	private IQgrsIdentifier gIdentifier;
	private QgrsHomologyScorer gAligner;
	private InputType inputType;
	private final StatusHolder statusHolder;
	private final Cache cache;
	
	public enum SeqTag { Principle, Comparison };
	private boolean principleQuadruplexIdCached = false;
	private boolean comparisonQuadruplexIdCached = false;
	private boolean alignmentCached = false;
	private boolean homologyResultsCached = false;
	
	public QgrsCompute(StatusHolder statusHolder, Cache cache) {
		this.statusHolder = statusHolder;
		this.cache = cache;
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
			
			// QGRS Structures can't be cached until after all the computations are complete,
			// as some of the information that is cached would cause aspects of the structure to 
			// be computed before they typically should be (after alignment) due to internal caching
			// within GQuadruplex.
			this.writeQgrsCache(pair);
			this.writeHomologyCache(pair, this.getgAligner().getSimilarityResults());
		
		} catch (java.lang.OutOfMemoryError mex) {
			Runtime runtime = Runtime.getRuntime();  
			this.statusHolder.setStatus(JobStage.Error, -1, "Internal memory error, sequence too large for analysis (" + runtime.totalMemory()/(1024*1024) + "mb available).  Please contact the site administrator");
			mex.printStackTrace();
			throw mex;
		
		}
		//System.out.println("Job Completed");
		MemoryReporter.memoryReport();
		return;
	}
	
	void computeHomologyScores(GeneSequencePair pair) throws Exception {
		this.statusHolder.setStatus(JobStage.QGRS_Homology, -1, null);
		
		
		List<QgrsHomologyRecord> records = null;
		if ( this.alignmentCached && cache != null ) {
			records = this.cache.getHomologyRecords(pair.getAlignmentRecord());
		}
		
		
		if (records != null ) {
			this.gAligner = new CachedHomologyScorer(pair, records);
			this.homologyResultsCached = true;
		}
		else {
			this.gAligner.alignGQuadruplexes(pair);
		}
		
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
	
	
	private void writeHomologyCache(GeneSequencePair pair, List<QgrsHomology> results) {
		// note we don't care if it was originally cached or not, always rewrite in case we are dealing with a hybrid cache with
		// different read/write sub-caches.
		if ( cache != null ) {
			this.cache.putHomologyResults(pair.getAlignmentRecord(), results, pair.getPrinciple(), pair.getComparison());
		}
	}
	
	
	private void writeQgrsCache(GeneSequencePair pair) {
		writeQgrsCache(pair.getPrinciple());
		writeQgrsCache(pair.getComparison());
	}
	private void writeQgrsCache(GeneSequence seq) {
		if ( cache == null ) return;
		List<GQuadruplex> cachedQgrs = this.cache.getQuadruplexes(seq);
		if ( cachedQgrs == null || cachedQgrs.size() < 1) {
			cache.putQuadruplexes(seq);
		}
	}
	
	public void identifyQgrs(GeneSequence seq, SeqTag tag) throws Exception{
		this.statusHolder.setStatus(JobStage.QGRS_ID, -1, null);
		List<GQuadruplex> cachedQgrs = null;
		if ( !seq.isDirectInput() && cache != null) {
			cachedQgrs = this.cache.getQuadruplexes(seq);
		}
		if ( cachedQgrs == null || cachedQgrs.size() < 1) {
			this.gIdentifier.findGQuadruplexes(seq);
		}
		else {
			if ( tag == SeqTag.Principle ) this.principleQuadruplexIdCached = true;
			else this.comparisonQuadruplexIdCached = true;
			seq.setgQuads(cachedQgrs);
		}
	}
	
	
	
	
	void cachePair(GeneSequencePair pair) {
		if (pair.isDirectInput() || cache == null) return;
		cache.put(pair.getAlignmentRecord());
		cache.put(pair.getAlignmentRecord(), pair.getPrinciple());
		cache.put(pair.getAlignmentRecord(), pair.getComparison());
	}
	void doAlignment(GeneSequencePair pair) throws Exception {
		this.statusHolder.setStatus(JobStage.Alignment_Sync, -1, null);
		
		/*if ( this.cachedAlignment(pair)) {
			if ( this.processCachedAlignment(pair)) {
				this.alignmentCached = true;
				// write to cache in any case, as some caches are hybrids (reading from a different sub-cache than writing to)
				cachePair(pair);
				return;
			}
		}*/
		
		MemoryReporter.memoryReport();
		this.aligner.align(pair, this.statusHolder);
		this.alignmentCached = pair.isWasCached();
		//System.out.println("Alignment Complete");
		MemoryReporter.memoryReport();
		pair.setAlignmentBuildKey(BuildKey.Alignment);
		pair.setDateAligned(new Date());
		cachePair(pair);
	}
	
	private boolean processCachedAlignment(GeneSequencePair pair) {
		String principleSequence = this.cache.getAlignedSequence(pair.getAlignmentRecord(), pair.getPrinciple());
		String comparisonSequence = this.cache.getAlignedSequence(pair.getAlignmentRecord(), pair.getComparison());
		if ( principleSequence != null && comparisonSequence != null ) {
			pair.getPrinciple().buildBases(principleSequence);
			pair.getComparison().buildBases(comparisonSequence);
			return true;
		}
		else {
			return false;
		}
	}
	private boolean cachedAlignment(GeneSequencePair pair) {
		
		AlignmentRecord ar = cache == null ? null : cache.getAlignmentRecord(pair.getPrinciple(), pair.getComparison(), BuildKey.Alignment);
		if ( ar == null ) return false;
		pair.setAlignmentRecord(ar);
		return true;
	}
	
	
	public GeneralAligner getAligner() {
		return aligner;
	}
	public void setAligner(GeneralAligner aligner) {
		this.aligner = aligner;
	}
	public IQgrsIdentifier getgIdentifier() {
		return gIdentifier;
	}
	public void setgIdentifier(IQgrsIdentifier gIdentifier) {
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
