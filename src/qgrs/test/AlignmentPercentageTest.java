package qgrs.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import qgrs.compute.GeneSequencePair;
import qgrs.data.GeneSequence;

public class AlignmentPercentageTest {

	
	private final String A = "---ATCGATCTCGGGAAATCC-----";
	private final String B = "------GATCTCGGGAAATCCCC---";
	private final String C = "------G-ATCT-CGGGAAATCCCCA---";
	private final String D = "------GGATCTCCGGGAAAGCCCCG---";
	
	@Test
	public void testPerfectMatch() {
		GeneSequence seqA = GeneSequence.buildFromDirectInput(A);
		GeneSequencePair pair = new GeneSequencePair(seqA, seqA);
		assertEquals("100% Alignment Match failed", pair.getSimilarityPercentage(), 1, 0.00001);
	}
	@Test
	public void testUnevenEnds() {
		GeneSequence seqA = GeneSequence.buildFromDirectInput(A);
		GeneSequence seqB = GeneSequence.buildFromDirectInput(B);
		GeneSequencePair pair = new GeneSequencePair(seqA, seqB);
		assertEquals("100% Alignment Match with uneven ends failed", pair.getSimilarityPercentage(), 15.0/18.0, 0.00001);
	}
	@Test
	public void testMismatch() {
		GeneSequence seqA = GeneSequence.buildFromDirectInput(C);
		GeneSequence seqB = GeneSequence.buildFromDirectInput(D);
		GeneSequencePair pair = new GeneSequencePair(seqA, seqB);
		assertEquals("100% Alignment Match with uneven ends failed", pair.getSimilarityPercentage(), 16.0/20, 0.00001);
	}
}
