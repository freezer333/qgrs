package qgrs.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import qgrs.data.GeneSequence;
import qgrs.data.providers.NoCacheSequenceProvider;
import qgrs.data.providers.SequenceProvider;
import qgrs.data.providers.SequenceProvider.Key;
import framework.web.AbstractWebContext;
import framework.web.util.StringUtils;

public class FlexibleInputProvider implements InputProvider {

	enum InputType { 
		Id(true), Raw(false), FASTA(true); 
		
		public final boolean hasId;
		private InputType(boolean hasID) { hasId = hasID;}
	};

	InputType seq1Type;
	InputType seq2Type;
	String seq1Id;
	String seq2Id;
	String seq1Chars;
	String seq2Chars;
	String errorMessage;
	
	int ncbiCount = 0;
	
	final int MAX_SEQ_LENGTH = 10025;
	
	public FlexibleInputProvider (AbstractWebContext context) {
		seq1Type = this.getSeq1InputType(context);
		seq2Type = this.getSeq2InputType(context);
		seq1Id = context.getString(QParam.Sequence1);
		seq1Chars = context.getString(QParam.Sequence1Chars);
		seq2Id = context.getString(QParam.Sequence2);
		seq2Chars = context.getString(QParam.Sequence2Chars);
		errorMessage = null;
	}

	InputType getSeq1InputType(AbstractWebContext context) {
		if ("seq1IdOption".equals(context.getFromSession(QParam.Sequence1InputOption))) return InputType.Id;
		String sequence = (String) context.get(QParam.Sequence1Chars);
		if ( sequence.startsWith(">")) return InputType.FASTA;
		else return InputType.Raw;
	}
	InputType getSeq2InputType(AbstractWebContext context) {
		if ("seq2IdOption".equals(context.getFromSession(QParam.Sequence2InputOption)))return InputType.Id;
		String sequence = (String) context.get(QParam.Sequence2Chars);
		if ( sequence.startsWith(">")) return InputType.FASTA;
		else return InputType.Raw;
	}


	private GeneSequence buildGeneSequenceFromId(String id, SequenceProvider provider) throws Exception{
		GeneSequence s ;
		
		try {
			HashMap<Key, Object> seqValues;
			seqValues = provider.getSequence(id);
			if ( seqValues == null ) return null;
			s = GeneSequence.buildFromProviderMap(seqValues);
			if ( seqValues.containsKey(Key.Live) && ((Boolean)seqValues.get(Key.Live)) ) { 
				ncbiCount++;
			}
		} catch (Exception e) {
			if ( !StringUtils.isDefined(id)) {
				throw new Exception ("Please make sure you enter 2  nucleotide sequences for analysis");
			}
			else {
				e.printStackTrace();
				throw new Exception("The mRNA sequence identified by ["+id+"] was not recognized");
			}
		}
		return s;
	}
	
	
	
	private GeneSequence buildGeneSequenceFromRaw(String raw) throws Exception {
		GeneSequence s = GeneSequence.buildFromDirectInput(raw);
		return s;
	}
	
	private String getFastaPart(String fasta, int termNumber) {
		int start = termNumber -1;
		int found = 0;
		int index = 0;
		int startIndex = 0;
		while ( index >= 0 && found < start ) {
			index = fasta.indexOf("|", index+1);
			if ( index >= 0) found++;
		}
		if ( index < 0 ) return null;
		startIndex = index;
		int end = fasta.indexOf("|", index+1);
		if ( end < 0 ) return null;
		return fasta.substring(startIndex+1, end);
	}
	private GeneSequence buildGeneSequenceFromFASTA(String fasta, SequenceProvider provider) throws Exception {
		String id = getFastaPart(fasta, 4);
		GeneSequence retval = null;
		if ( StringUtils.isDefined(id)) {
			try {
				retval = this.buildGeneSequenceFromId(id, provider);
			}
			catch  (Exception e) {
				e.printStackTrace();
			}
		}
		if ( retval == null ) {
			// unable to retrieve it.
			String sequence = fasta.substring(fasta.indexOf('\n'));
			retval = GeneSequence.buildFromDirectInput(sequence, id==null?"N/A":id);
		}
		
		return retval;
	}
	
	
	

	@Override
	public QGRSProgramInput getInput(){
		QGRSProgramInput input = new QGRSProgramInput();
		List<GeneSequence>comparisons=new ArrayList<GeneSequence>();
		SequenceProvider provider = null;
		try {
			

			if ( seq1Type.hasId || seq2Type.hasId ){
				try {
					provider = new NoCacheSequenceProvider();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

			switch ( seq1Type ) {
			case Id:
				input.setPrinciple(this.buildGeneSequenceFromId(this.seq1Id, provider));
				break;
			case Raw:
				input.setPrinciple(this.buildGeneSequenceFromRaw(this.seq1Chars));
				break;
			case FASTA:
				input.setPrinciple(this.buildGeneSequenceFromFASTA(this.seq1Chars, provider));
				break;
			}

			switch ( seq2Type ) {
			case Id:
				comparisons.add(this.buildGeneSequenceFromId(this.seq2Id, provider));
				break;
			case Raw:
				comparisons.add(this.buildGeneSequenceFromRaw(this.seq2Chars));
				break;
			case FASTA:
				comparisons.add(this.buildGeneSequenceFromFASTA(this.seq2Chars, provider));
				break;
			}

			input.setComparisons(comparisons);


		} 
		catch (Exception e) {
			e.printStackTrace();
			this.errorMessage = e.getMessage();
		}
		return input;
	}

	@Override
	public int getNumNcbiCalls() {
		return ncbiCount;
	}

}
