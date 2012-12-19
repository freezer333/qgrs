package qgrs.db;

import java.util.List;

import qgrs.data.GQuadruplex;
import qgrs.data.GeneSequence;
import qgrs.data.QgrsHomology;
import qgrs.data.records.AlignmentRecord;
import qgrs.data.records.QgrsHomologyRecord;

public interface Cache {
	
	GeneSequence get(String accessionNumber) ;
	void put(GeneSequence sequence) ;
	
	AlignmentRecord getAlignmentRecord(GeneSequence principle, GeneSequence comparison, String alignmentBuildKey) ;
	void put(AlignmentRecord alignmentRecord);
	
	String getAlignedSequence(AlignmentRecord alignmentRecord, GeneSequence sequence);
	void put(AlignmentRecord alignmentRecord, GeneSequence sequence);
	
	List<GQuadruplex> getQuadruplexes(GeneSequence s);
	void putQuadruplexes(GeneSequence s);
	void flushAndClose();
	
	void putHomologyResults(AlignmentRecord record, List<QgrsHomology> homologyResults, GeneSequence principle, GeneSequence comparison);
	List<QgrsHomologyRecord> getHomologyRecords (AlignmentRecord record);
}
