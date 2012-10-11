package qgrs.compute;

import jaligner.Alignment;
import jaligner.Sequence;
import jaligner.SmithWatermanGotoh;
import jaligner.matrix.MatrixLoader;
import qgrs.compute.alignment.AlignmentMatrix;
import qgrs.compute.alignment.AlignmentPath;
import qgrs.compute.alignment.AlignmentProperties;
import qgrs.compute.alignment.StandardAlignmentProperties;
import qgrs.job.CancelFlag;
import qgrs.job.DefaultCancelFlag;
import qgrs.job.JobStage;
import qgrs.job.StatusHolder;

public class SmithWatermanAlignment implements GeneralAligner {

	StatusHolder statusHolder = null;
	CancelFlag cancelFlag = new DefaultCancelFlag();
	
	@Override
	public void setCancelFlag(CancelFlag flag) {
		cancelFlag = flag;
	}

	@Override
	public void align(GeneSequencePair pair, StatusHolder statusHolder)
			throws Exception {
		
		Sequence s1 = new Sequence(pair.getPrinciple().getPureSequence());
		Sequence s2 = new Sequence(pair.getComparison().getPureSequence());
		if ( statusHolder != null ) statusHolder.setStatus(JobStage.Alignment_Calc, 0.5, "Computing alignment");
		
        Alignment alignment = SmithWatermanGotoh.align(s1, s2, MatrixLoader.load("EDNAFULL"), 50f, 1f);
        
        if ( statusHolder != null ) statusHolder.setStatus(JobStage.Alignment_Calc, 1, "Finalizing alignment");
		
        String gapped1 = getFullSequenceGapped(s1.getSequence(), alignment.getSequence1(), alignment.getStart1() );
        String gapped2 = getFullSequenceGapped(s2.getSequence(), alignment.getSequence2(), alignment.getStart2() );
        
        pair.getPrinciple().setGaps(gapped1);
		pair.getComparison().setGaps(gapped2);
		
		
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

	private String getFullSequenceGapped(String original, char []aligned, int start) {
		StringBuilder b = new StringBuilder();
		for ( int i = 0; i < start; i++ ) {
			b.append(original.charAt(i));
		}
		int curOriginal = start;
		for ( int i = 0; i < aligned.length; i++ ) {
			b.append(aligned[i]);
			if ( aligned[i] != '-' ) {
				curOriginal++;
			}
		}
		for ( int i = curOriginal; i < original.length(); i++ )  {
			b.append(original.charAt(i));
		}
		return b.toString();
	}

}
