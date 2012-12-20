package qgrs.data.cache;

import java.util.List;

import qgrs.compute.BuildKey;
import qgrs.data.GQuadruplex;
import qgrs.data.GeneSequence;
import qgrs.data.QgrsHomology;
import qgrs.data.records.AlignmentRecord;
import qgrs.data.records.GQuadruplexRecord;
import qgrs.data.records.QgrsHomologyRecord;
import qgrs.db.AlignedSequenceDb;
import qgrs.db.AlignmentRecordDb;
import qgrs.db.DatabaseConnection;
import qgrs.db.GeneSequenceDb;
import qgrs.db.HomologyRecordDb;
import qgrs.db.QgrsDb;

public class ReadWriteCache implements Cache {

	private final DatabaseConnection connection;
	
	
	public ReadWriteCache (DatabaseConnection connection) {
		this.connection = connection;
	}

	@Override
	public GeneSequence get(String accessionNumber) {
		return null;
	}

	@Override
	public void put(GeneSequence sequence) {
		GeneSequenceDb db = new GeneSequenceDb(connection);
		db.put(sequence);
		db.close();
	}

	@Override
	public void flushAndClose() {
		this.connection.close();
	}

	@Override
	public AlignmentRecord getAlignmentRecord(GeneSequence principle,
			GeneSequence comparison, String alignmentBuildKey) {
		AlignmentRecordDb db = new AlignmentRecordDb(connection);
		AlignmentRecord retval = db.get(principle, comparison, alignmentBuildKey);
		db.close();
		return retval;
	}

	@Override
	public void put(AlignmentRecord alignmentRecord) {
		AlignmentRecordDb db = new AlignmentRecordDb(connection);
		db.put(alignmentRecord);
		db.close();
	}

	@Override
	public String getAlignedSequence(AlignmentRecord alignmentRecord, GeneSequence sequence) {
		AlignedSequenceDb db = new AlignedSequenceDb(connection);
		String seq = db.get(alignmentRecord.getId(), sequence.getAccessionNumber());
		db.close();
		return seq;
	}

	@Override
	public void put(AlignmentRecord alignmentRecord, GeneSequence sequence) {
		AlignedSequenceDb db = new AlignedSequenceDb(connection);
		db.put(alignmentRecord.getId(), sequence.getAccessionNumber(), sequence.getAlignedSequence());
		db.close();
	}
	
	public List<GQuadruplex> getQuadruplexes(GeneSequence s) {
		QgrsDb db = new QgrsDb(connection);
		List<GQuadruplex> retval = db.getAll(s);
		db.close();
		return retval;
	}
	public void putQuadruplexes(GeneSequence s) {
		QgrsDb db = new QgrsDb(connection);
		for ( GQuadruplex gq : s.getgQuads()) {
			if ( gq.isAcceptable() ) {
				GQuadruplexRecord r = gq.getRecord();
				db.put(r);
				// set the ID that has been created
				gq.setId(r.getId());
			}
		}
		db.close();
	}

	@Override
	public void putHomologyResults(AlignmentRecord record, List<QgrsHomology> homologyResults, GeneSequence principle, GeneSequence comparison) {
		HomologyRecordDb db = new HomologyRecordDb(connection);
		for ( QgrsHomology h : homologyResults) {
			if ( h.getOverallScore() > CacheConstants.HomologyScoreCutoff ) {
				QgrsHomologyRecord hr = h.buildHomologyRecord(record, principle, comparison);
				db.put(hr);
			}
		}
	}

	@Override
	public List<QgrsHomologyRecord> getHomologyRecords(AlignmentRecord record) {
		HomologyRecordDb db = new HomologyRecordDb(connection);
		List<QgrsHomologyRecord> retval = db.get(record.getId());
		db.close();
		return retval;
	}

}
