package qgrs.test;

import org.biojavax.bio.db.ncbi.GenbankRichSequenceDB;
import org.biojavax.bio.seq.RichSequence;
import org.junit.Ignore;
import org.junit.Test;

import qgrs.data.GeneSequence;
import qgrs.db.GenbankRichSequenceTextDB;
import qgrs.db.GeneSequenceDb;

public class GeneSequenceDbTest {
	GeneSequence buildGeneSequence() {
		GenbankRichSequenceDB  ncbi = new GenbankRichSequenceTextDB();
		RichSequence rs;
		try {
			rs = ncbi.getRichSequence("NM_001127328.1");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String sequence =rs.seqString();
		return GeneSequence.buildFromRichSequence(sequence, rs);
	}
	
	@Ignore
	@Test
	public void testDbSerialization() {
		GeneSequence src = buildGeneSequence();
		GeneSequenceDb db = new GeneSequenceDb(new TestDatabaseConnection());
		db.put(src);
		GeneSequence dst = db.get(src.getAccessionNumber());

		GeneSequenceComparer.assertTheSame(src,  dst);
	}
}
