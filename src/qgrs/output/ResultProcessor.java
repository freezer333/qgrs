package qgrs.output;

import java.util.List;

import qgrs.compute.GeneSequencePair;
import qgrs.data.QgrsHomology;

public abstract class ResultProcessor {

	
	
	public abstract void handleResults(List<GeneSequencePair> pairs, List<QgrsHomology> similarityResults) ;

	
	
}
