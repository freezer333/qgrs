package qgrs.compute;

import qgrs.compute.alignment.AlignmentMatrix;
import qgrs.compute.alignment.AlignmentPath;
import qgrs.compute.alignment.AlignmentProperties;
import qgrs.compute.alignment.StandardAlignmentProperties;
import qgrs.data.GeneSequence;
import qgrs.job.CancelFlag;
import qgrs.job.DefaultCancelFlag;
import qgrs.job.JobStage;
import qgrs.job.StatusHolder;

public class SequenceAligner implements GeneralAligner {

	AlignmentProperties props = new StandardAlignmentProperties();
	StatusHolder statusHolder = null;
	
	CancelFlag cancelFlag = new DefaultCancelFlag();
	
	@Override
	public void setCancelFlag(CancelFlag flag) {
		cancelFlag = flag;
	}

	@Override
	public void align(GeneSequencePair pair, StatusHolder statusHolder)
			throws Exception {
		
		AlignmentMatrix matrix = new AlignmentMatrix(pair.getPrinciple(), pair.getComparison(), props);
		AlignmentPath path = matrix.computeBestAlignment(statusHolder, cancelFlag);
		if ( statusHolder != null ) statusHolder.setStatus(JobStage.Alignment_Calc, 0.9, "Applying gaps");
		applyGaps(pair, path);
		matrix = null;
		if ( statusHolder != null ) statusHolder.setStatus(JobStage.Alignment_Calc, 1, "Finalizing alignment");
		System.gc();
	}
	
	void applyGaps(GeneSequencePair pair, AlignmentPath path) {
		pair.getPrinciple().setGaps(path.getSeq1());
		pair.getComparison().setGaps(path.getSeq2());
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

}
