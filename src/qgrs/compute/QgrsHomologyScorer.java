package qgrs.compute;

import java.util.List;

import qgrs.data.QgrsHomology;
import qgrs.job.Cancellable;

/*
 * Implementations so far:SemiGlobalGotohAligner,NWGlobalAligner 
 */
public interface QgrsHomologyScorer extends Cancellable {
	void alignGQuadruplexes(GeneSequencePair pair) throws Exception;
	List<QgrsHomology> getSimilarityResults() ;
}
