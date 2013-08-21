package qgrs.output;

import java.util.List;

import qgrs.compute.GeneSequencePair;
import qgrs.data.QgrsHomology;
import qgrs.data.mongo.operations.DbSave;

public class MongoResultProcessor extends ResultProcessor {
	DbSave mongo;
	
	
	public MongoResultProcessor() {
		try {
			mongo = new DbSave();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public void handleResults(List<GeneSequencePair> pairs,
			List<QgrsHomology> similarityResults) {
		mongo.push(pairs.get(0), similarityResults);
		
	}
	@Override
	public void cleanup() {
		mongo.close();
	}
	
	

}
