package qgrs.compute;

import qgrs.compute.alignment.AlignmentMatrix;
import qgrs.compute.alignment.AlignmentPath;
import qgrs.compute.alignment.AlignmentProperties;
import qgrs.compute.alignment.StandardAlignmentProperties;
import qgrs.job.CancelFlag;
import qgrs.job.StatusHolder;

public class SequenceAligner implements GeneralAligner {

	AlignmentProperties props = new StandardAlignmentProperties();
	
	@Override
	public void setCancelFlag(CancelFlag flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void align(GeneSequencePair pair, StatusHolder statusHolder)
			throws Exception {
		
		AlignmentMatrix matrix = new AlignmentMatrix(pair.getPrinciple(), pair.getComparison(), props);
		AlignmentPath path = matrix.computeBestAlignment();
		applyGaps(pair, path);
	}
	
	void applyGaps(GeneSequencePair pair, AlignmentPath path) {
		
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

}
