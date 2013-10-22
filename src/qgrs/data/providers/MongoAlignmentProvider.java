package qgrs.data.providers;

import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import qgrs.compute.GeneSequencePair;
import qgrs.data.mongo.primitives.jongo.Alignment;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoAlignmentProvider extends AlignmentProvider {

	private final MongoCollection alignments;
	
	public MongoAlignmentProvider () throws UnknownHostException {
		DB db = new MongoClient().getDB("qgrs");
		Jongo jongo = new Jongo(db);
		alignments = jongo.getCollection("alignments");
	}
	
	@Override
	protected AlignmentProviderResult getAlignmentFromCache(GeneSequencePair pair) {
		System.out.println("Finding alignment for " + pair.getPrinciple().getAccessionNumber() + " x " + pair.getComparison().getAccessionNumber());
		Alignment alignment = alignments.findOne("{$and: [{principal.accessionNumber:#}, {comparison.accessionNumber:#}]}", 
				pair.getPrinciple().getAccessionNumber(), pair.getComparison().getAccessionNumber()).as(Alignment.class);
		if ( alignment == null ) return null;
		return new AlignmentProviderResult(alignment.getPrincipal().getAlignedSequence(), alignment.getComparison().getAlignedSequence(), false);
	}

	
	
}
