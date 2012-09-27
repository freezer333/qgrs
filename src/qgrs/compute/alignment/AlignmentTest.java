package qgrs.compute.alignment;

import qgrs.compute.GeneSequencePair;
import qgrs.compute.SequenceAligner;
import qgrs.data.GeneSequence;

public class AlignmentTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		// Aligner has gap opening penalty = -5 and gap extension = -1
		SequenceAligner aligner = new SequenceAligner() ;
		
		GeneSequence a = GeneSequence.buildFromDirectInput("ATCGTGAC");
		GeneSequence b = GeneSequence.buildFromDirectInput("TTCGGAG");
		
		aligner.align(new GeneSequencePair(a, b), null);
	}

}
