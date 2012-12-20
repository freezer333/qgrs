package qgrs.test;

import org.biojavax.bio.db.ncbi.GenbankRichSequenceDB;
import org.biojavax.bio.seq.RichSequence;
import org.jdom.Element;
import org.junit.Test;

import qgrs.data.GeneSequence;
import qgrs.input.GenbankRichSequenceTextDB;

public class GeneSequenceXmlTest {

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
	
	@Test
	public void testSequenceXmlSerialization() {
		GeneSequence src = buildGeneSequence();
		Element e = src.getXmlElement();
		GeneSequence dst = GeneSequence.buildFromXml(e);
		
		GeneSequenceComparer.assertTheSame(src,  dst);
		
		
	}
	
}
