package qgrs.data.providers;

import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import qgrs.compute.GeneSequencePair;
import qgrs.data.mongo.primitives.jongo.Alignment;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoAlignmentProvider extends AlignmentProvider {

	private MongoCollection alignments;
	
	public MongoAlignmentProvider () throws UnknownHostException {
		try {
			DB db = new MongoClient().getDB("qgrs");
			Jongo jongo = new Jongo(db);
			alignments = jongo.getCollection("alignments");
		}
		catch (Exception e) {
			System.err.println("Error connecting to MongoDB qgrs database.");
			alignments = null;
		}
	}
	
	@Override
	protected AlignmentProviderResult getAlignmentFromCache(GeneSequencePair pair) {
		if ( this.alignments == null ) return null;
			System.out.println("Finding alignment for " + pair.getPrinciple().getAccessionNumber() + " x " + pair.getComparison().getAccessionNumber());
			try {
			Alignment alignment = alignments.findOne("{$and: [{principal.accessionNumber:#}, {comparison.accessionNumber:#}]}", 
					pair.getPrinciple().getAccessionNumber(), pair.getComparison().getAccessionNumber()).as(Alignment.class);
			if ( alignment == null ) return null;
			return new AlignmentProviderResult(alignment.getPrincipal().getAlignedSequence(), alignment.getComparison().getAlignedSequence(), false);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
}
