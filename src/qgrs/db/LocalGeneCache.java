package qgrs.db;

import framework.web.util.StringUtils;
import qgrs.data.AlignmentRecord;
import qgrs.data.GeneSequence;


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
		
		// Now collect any available GO terms.
		
		return gene;
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

	}

	

}
