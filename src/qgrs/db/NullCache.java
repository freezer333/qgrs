package qgrs.db;

import java.util.List;

import qgrs.data.GQuadruplex;
import qgrs.data.GeneSequence;
import qgrs.data.QgrsHomology;
import qgrs.data.records.AlignmentRecord;
import qgrs.data.records.QgrsHomologyRecord;

public class NullCache implements Cache {

	@Override
	public GeneSequence get(String accessionNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(GeneSequence sequence) {
		// TODO Auto-generated method stub

	}

	@Override
	public void flushAndClose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AlignmentRecord getAlignmentRecord(GeneSequence principle,
			GeneSequence comparison, String alignmnetBuildKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(AlignmentRecord alignmentRecord) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAlignedSequence(AlignmentRecord alignmentRecord, GeneSequence sequence) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(AlignmentRecord alignmentRecord, GeneSequence sequence) {
		// TODO Auto-generated method stub
		
	}
	
	public List<GQuadruplex> getQuadruplexes(GeneSequence s) {
		return null;
	}
	public void putQuadruplexes(GeneSequence s) {
		
	}

	@Override
	public void putHomologyResults(AlignmentRecord record,
			List<QgrsHomology> homologyResults, GeneSequence principle, GeneSequence comparison) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<QgrsHomologyRecord> getHomologyRecords(AlignmentRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

}
