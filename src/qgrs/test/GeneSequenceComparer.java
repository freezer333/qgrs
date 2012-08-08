package qgrs.test;

import static org.junit.Assert.assertEquals;
import qgrs.data.GeneSequence;

public class GeneSequenceComparer {

	
	public static void assertTheSame(GeneSequence src, GeneSequence dst) {
		assertEquals("accessionNumber", src.getAccessionNumber(), dst.getAccessionNumber());
		assertEquals("sequenceLength", src.getSequenceLength(), dst.getSequenceLength());
		assertEquals("directInput", src.isDirectInput(), dst.isDirectInput());
		assertEquals("giNumber", src.getGiNumber(), dst.getGiNumber());
		assertEquals("geneSymbol", src.getGeneSymbol(), dst.getGeneSymbol());
		assertEquals("ncbiLink", src.getNcbiLink(), dst.getNcbiLink());
		assertEquals("geneName", src.getGeneName(), dst.getGeneName());
		assertEquals("species", src.getSpecies(), dst.getSpecies());
		assertEquals("cds start", src.getCds().getStart(), dst.getCds().getStart());
		assertEquals("utr5 start", src.getUtr5().getStart(), dst.getUtr5().getStart());
		assertEquals("utr3 end", src.getUtr3().getEnd(), dst.getUtr3().getEnd());
		assertEquals("poly a signals", src.getPolyASignals().size(), dst.getPolyASignals().size());
		assertEquals("poly a sites", src.getPolyASites().size(), dst.getPolyASites().size());
	}
}
