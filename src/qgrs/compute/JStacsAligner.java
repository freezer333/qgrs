package qgrs.compute;


import jaligner.SmithWatermanGotoh;
import jaligner.matrix.MatrixLoader;
import qgrs.compute.alignment.AlignmentPath;
import qgrs.job.CancelFlag;
import qgrs.job.DefaultCancelFlag;
import qgrs.job.JobStage;
import qgrs.job.StatusHolder;
import de.jstacs.algorithms.alignment.Alignment;
import de.jstacs.algorithms.alignment.PairwiseStringAlignment;
import de.jstacs.algorithms.alignment.Alignment.AlignmentType;
import de.jstacs.algorithms.alignment.cost.AffineCosts;
import de.jstacs.algorithms.alignment.cost.Costs;
import de.jstacs.algorithms.alignment.cost.SimpleCosts;
import de.jstacs.data.AlphabetContainer;
import de.jstacs.data.alphabets.DiscreteAlphabet;
import de.jstacs.data.sequences.Sequence;

public class JStacsAligner implements GeneralAligner {

	StatusHolder statusHolder = null;
	CancelFlag cancelFlag = new DefaultCancelFlag();

	@Override
	public void setCancelFlag(CancelFlag flag) {
		cancelFlag = flag;
	}

	@Override
	public void align(GeneSequencePair pair, StatusHolder statusHolder)
			throws Exception {

		String[] symbols = {"A", "C", "G", "T", "-"};
		DiscreteAlphabet abc = new DiscreteAlphabet( true, symbols );
		
		Sequence seq1 = Sequence.create( new AlphabetContainer( abc ), pair.getPrinciple().getPureSequence() );
		Sequence seq2 = Sequence.create( new AlphabetContainer( abc ), pair.getComparison().getPureSequence() );
		Costs costs = new SimpleCosts( -1, 1.5, 1);
		costs = new AffineCosts( 5, costs );
		Alignment align = new Alignment( AlignmentType.SEMI_GLOBAL, costs );
		
		if (statusHolder != null)
			statusHolder.setStatus(JobStage.Alignment_Calc, 0.5,
					"Computing alignment");

		PairwiseStringAlignment a = null;
		if ( pair.getPrinciple().getPureSequence().length() <= pair.getComparison().getPureSequence().length()) {
			// principle first
			a = align.getAlignment( seq1, seq2 );
			pair.getPrinciple().setGaps(a.getAlignedString(0));
			pair.getComparison().setGaps(a.getAlignedString(1));
		}
		else {
			// comparison first
			a = align.getAlignment( seq2, seq1 );
			pair.getPrinciple().setGaps(a.getAlignedString(1));
			pair.getComparison().setGaps(a.getAlignedString(0));
		}
		
		
		
		
		if (statusHolder != null)
			statusHolder.setStatus(JobStage.Alignment_Calc, 1,
					"Finalizing alignment");
		
		

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
