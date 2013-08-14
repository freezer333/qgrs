package qgrs.compute.interfaces;

import java.util.List;

import qgrs.compute.GeneSequencePair;
import qgrs.data.QgrsHomology;
import qgrs.job.Cancellable;


public interface QgrsHomologyScorer extends Cancellable {
	void computeQgrsHomology(GeneSequencePair pair) throws Exception;
	List<QgrsHomology> getQgrsHomologyResults() ;
}
