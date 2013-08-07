package qgrs.data.providers;

import java.util.HashMap;

import qgrs.data.GeneSequence;
import qgrs.data.providers.SequenceProvider.Key;
import qgrs.input.InputProvider;
import qgrs.input.QGRSProgramInput;

public class PredictorInputProvider implements InputProvider {

	private String principal;
	private String comparison;
	private int numNcbi;
	SequenceProvider provider;
	
	public PredictorInputProvider(String principal, String comparison, SequenceProvider provider) {
		super();
		this.principal = principal;
		this.comparison = comparison;
		this.numNcbi = 0;
		this.provider = provider;
	}

	

	@Override
	public QGRSProgramInput getInput() {
		HashMap<Key, Object> seqValues;
		
		seqValues = provider.getSequence(principal);
		if ( seqValues == null ) return null;
		GeneSequence p = GeneSequence.buildFromProviderMap(seqValues);
		if ( seqValues.containsKey(Key.Live) && ((boolean)seqValues.get(Key.Live)) ) numNcbi++;
		
		seqValues = provider.getSequence(comparison);
		if ( seqValues == null ) return null;
		GeneSequence c = GeneSequence.buildFromProviderMap(seqValues);
		if ( seqValues.containsKey(Key.Live) && ((boolean)seqValues.get(Key.Live)) ) numNcbi++;
		
		QGRSProgramInput input = new QGRSProgramInput();
		input.setPrinciple(p);
		input.setComparison(c);
		return input;
		
	}

	@Override
	public int getNumNcbiCalls() {
		return numNcbi;
	}

}
