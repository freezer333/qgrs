package qgrs.job;

import java.util.LinkedList;
import java.util.List;

import qgrs.compute.FamilyHomologyScorer;
import qgrs.compute.GeneSequencePair;
import qgrs.compute.QgrsCompute;
import qgrs.compute.SemiGlobalSequenceAligner;
import qgrs.compute.gscore.QgrsFinder;
import qgrs.compute.interfaces.GeneralAligner;
import qgrs.data.GeneSequence;
import qgrs.data.providers.AlignmentProvider;
import qgrs.input.InputProvider;
import qgrs.input.QGRSProgramInput;
import qgrs.output.ResultProcessor;


public class AlignmentJob extends Job{

	private InputProvider inputProvider;
	private final ResultProcessor resultProcessor;
	private final AlignmentProvider alignmentProvider;
	QGRSProgramInput input;
	volatile CancelFlag cancelFlag = new CancelFlag();
	
	private int ncbiCallCount = 0;
	private int embossCallCount = 0;

	public AlignmentJob(InputProvider inputProvider, ResultProcessor resultProcessor, AlignmentProvider alignmentProvider) {
		this.inputProvider = inputProvider;
		this.resultProcessor = resultProcessor;
		this.alignmentProvider = alignmentProvider;
	}
	
	public void cancel() {
		this.cancelFlag.raise();
	}
	
	public int getRequiredThrottle() {
		return Math.max(ncbiCallCount, embossCallCount) * 1000;
	}

	@Override
	public void runJob() throws Exception {	
		QgrsCompute qAligner = new QgrsCompute(this);
		try {
			this.setStatus(JobStage.Downloading, -1, null);
			input = inputProvider.getInput();
			this.ncbiCallCount = inputProvider.getNumNcbiCalls();
			
			this.checkInput();
			
			this.configureSemiGlobalAlignment(qAligner);
			this.configureQgrsIdentification(qAligner);
			this.checkCancel();
			
			// Run identification on principle (once)
			qAligner.identifyQgrs(input.getPrinciple(), QgrsCompute.SeqTag.Principle);
			this.checkCancel();
			
			List<GeneSequencePair> pairs = this.buildPairs();
			if ( !this.computePairs(qAligner, pairs) ) {
				throw new RuntimeException("The pairs could not be computed");
			}
			
			if ( !qAligner.isAlignmentCached() ) {
				this.embossCallCount++;
			}
			
			if ( this.resultProcessor != null ) {
				this.resultProcessor.handleResults(pairs, qAligner.getgAligner().getQgrsHomologyResults());
			}
			this.setStatus(JobStage.Complete, -1, null);
			this.resultProcessor.cleanup();
		}
		finally {
			qAligner = null;
			System.gc();
		}
	}
	
	private boolean computePairs(QgrsCompute qAligner, List<GeneSequencePair> pairs) {
		for ( GeneSequencePair pair : pairs ) {
			try {
				qAligner.compute(pair);
				this.checkCancel();
			}
			catch (CancelException ce) {
				System.out.println("Job Canceled");
				return false;
			}
			catch (Exception e ) {
				this.setStatus(JobStage.Error, -1, e.getMessage());
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	
	private void checkInput() throws Exception{
		if ( input == null) {                                      
			this.setStatus(JobStage.Error, -1, "Input was not specified.");
			throw new Exception ("Input was not specified.");
		}
		if ( input.getPrinciple() == null ) {
			this.setStatus(JobStage.Error, -1, "Principle mRNA was not recognized.");
			throw new Exception ("Principle mRNA was not recognized");
		}
		if ( input.getComparisons() == null || input.getComparisons().size() < 1 ) {
			this.setStatus(JobStage.Error, -1, "Comparison mRNA was not recognized.");
			throw new Exception ("Comparison mRNA was not recognized.");
		}
	}
	
	void configureSemiGlobalAlignment(QgrsCompute qAligner) {
		GeneralAligner gAligner = new SemiGlobalSequenceAligner(this.alignmentProvider);
		gAligner.setCancelFlag(this.cancelFlag);
		qAligner.setAligner(gAligner);
		try {
			qAligner.setgIdentifier(new QgrsFinder());
		} catch (Exception e1) {
			this.setStatus(JobStage.Error, -1, e1.getMessage());
			e1.printStackTrace();
		}
	}
	
	void configureQgrsIdentification(QgrsCompute qAligner) {
		FamilyHomologyScorer bga = new FamilyHomologyScorer(this);
		bga.setCancelFlag(this.cancelFlag);
		qAligner.setgAligner(bga);
	}
	
	void checkCancel() throws Exception {
		if ( this.cancelFlag.isRaised()) throw new CancelException();
	}
	
	List<GeneSequencePair> buildPairs() throws Exception{
		if ( input.getComparisons() == null ) {
			throw new Exception ("No Comparison mRNA sequence specified");
		}
		List<GeneSequence> principleCopies= input.getPrinciple().makeDeepCopies(input.getComparisons().size());	

		List<GeneSequencePair> pairs = new LinkedList<GeneSequencePair>();
		int principleCounter=0;
		for (GeneSequence seq : input.getComparisons()) {
			pairs.add(new GeneSequencePair(principleCopies.get(principleCounter), seq));
			principleCounter++;
		}
		return pairs;
	}
	
	public ResultProcessor getResultProcessor() {
		return resultProcessor;
	}

	





}
