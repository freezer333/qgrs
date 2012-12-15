package qgrs.db;

import qgrs.data.AlignmentRecord;
import qgrs.data.GeneSequence;
import framework.web.util.StringUtils;


public class LocalGeneCache extends NullCache {

	private final DatabaseConnection connection;
	
	public LocalGeneCache (DatabaseConnection connection) {
		this.connection = connection;
	}

	@Override
	public GeneSequence get(String accessionNumber) {
		GeneSequenceDb db = new GeneSequenceDb(connection);
		
		GeneSequence gene = db.get(accessionNumber);
		if ( gene == null ) return null;
		
		// Need to pull in the actual sequence data, which would be in the GENE-A table
		AlignedSequenceDb seqDb = new AlignedSequenceDb(connection);
		String sequence = seqDb.getPureSequence(accessionNumber);
		if ( !StringUtils.isDefined(sequence)) {
			return null;// can't use the gene, we don't have the sequence
		}
		gene.buildBases(sequence);
		
		db.close();
		seqDb.close();
		return gene;
	}

	@Override
	public void put(GeneSequence sequence) {
		GeneSequenceDb db = new GeneSequenceDb(connection);
		db.put(sequence);
		db.close();
	}
	@Override
	public void put(AlignmentRecord alignmentRecord) {
		AlignmentRecordDb db = new AlignmentRecordDb(connection);
		db.put(alignmentRecord);
		db.close();
	}
	@Override
	public void put(AlignmentRecord alignmentRecord, GeneSequence sequence) {
		AlignedSequenceDb db = new AlignedSequenceDb(connection);
		db.put(alignmentRecord.getId(), sequence.getAccessionNumber(), sequence.getAlignedSequence());
		db.close();
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
	public String getAlignedSequence(AlignmentRecord alignmentRecord,
			GeneSequence sequence) {
		
		AlignedSequenceDb db = new AlignedSequenceDb(connection);
		String seq = db.get(alignmentRecord.getId(), sequence.getAccessionNumber());
		db.close();
		return seq;
	}

	@Override
	public void flushAndClose() {
		// TODO Auto-generated method stub
		try {
			this.connection.close();
		}
		catch ( Exception e) {
			e.printStackTrace();
		}
	}

	

}
