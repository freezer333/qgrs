package qgrs.data.providers;

import qgrs.compute.BuildKey;
import qgrs.compute.GeneSequencePair;
import qgrs.data.records.AlignmentRecord;
import qgrs.db.AlignedSequenceDb;
import qgrs.db.AlignmentRecordDb;
import qgrs.db.DatabaseConnection;

public class RDBAlignmentProvider extends AlignmentProvider {

	private final DatabaseConnection connection;
	
	public RDBAlignmentProvider (DatabaseConnection connection) {
		this.connection = connection;
	}
	
	@Override
	protected AlignmentProviderResult getAlignmentFromCache(
			GeneSequencePair pair) {
		
		AlignmentRecordDb db = new AlignmentRecordDb(connection);
		AlignmentRecord retval = db.get(pair.getPrinciple(), pair.getComparison(), BuildKey.Alignment);
		db.close();
		if ( retval == null ) return null;
		
		AlignedSequenceDb adb = new AlignedSequenceDb(connection);
		String principleSequence = adb.get(retval.getId(), pair.getPrinciple().getAccessionNumber());
		String comparisonSequence = adb.get(retval.getId(), pair.getComparison().getAccessionNumber());
		if ( principleSequence != null && comparisonSequence != null ) {
			pair.getPrinciple().buildBases(principleSequence);
			pair.getComparison().buildBases(comparisonSequence);
		}
		return new AlignmentProviderResult(retval.getPrinciple(), retval.getComparison(), false);
		
	}

	
	
}
