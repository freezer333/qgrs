package qgrs.compute;

import qgrs.job.Cancellable;
import qgrs.job.StatusHolder;

// This will implement the general alignment of 
// two sequences
public interface GeneralAligner extends Cancellable{

	/**
	 * Performs a general (global, local, semi-global, etc.) alignment on two 
	 * gene sequences
	 * @param pair a pair with the original A and B sequences already 
	 * populated.
	 * @return populated the pair's alignedA and alignedB sequence
	 * with the optimal alignment and fills in its general alignment 
	 * score
	 * @throws exception if the alignment could not be performed for any
	 * reason.
	 */
	void align(GeneSequencePair pair, StatusHolder statusHolder) throws Exception ; 
	void cleanup();

	
}
