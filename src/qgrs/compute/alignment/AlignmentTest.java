package qgrs.compute.alignment;

import qgrs.compute.GeneSequencePair;
import qgrs.compute.GeneralAligner;
import qgrs.compute.JStacsAligner;
import qgrs.data.GeneSequence;

public class AlignmentTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		// Aligner has gap opening penalty = -5 and gap extension = -1
		GeneralAligner aligner = new JStacsAligner();//new SmithWatermanAlignment();//new SequenceAligner() ;
		
		GeneSequence a = GeneSequence.buildFromDirectInput("ATCGTGACATCGTGACACACTTTGGCGG");
		GeneSequence b = GeneSequence.buildFromDirectInput("TTCGGAGAGCTTTGCCAAATTCGGAG");
		
		aligner.align(new GeneSequencePair(a, b), null);
	}

}
