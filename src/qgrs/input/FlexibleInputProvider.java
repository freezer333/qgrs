package qgrs.input;

import java.util.ArrayList;
import java.util.List;

import org.biojavax.bio.db.ncbi.GenbankRichSequenceDB;
import org.biojavax.bio.seq.RichSequence;

import qgrs.data.GeneSequence;
import qgrs.db.Cache;
import qgrs.db.GenbankRichSequenceTextDB;
import framework.web.AbstractWebContext;
import framework.web.util.StringUtils;

public class FlexibleInputProvider implements InputProvider {

	enum InputType { Id, Raw, FASTA };

	InputType seq1Type;
	InputType seq2Type;
	String seq1Id;
	String seq2Id;
	String seq1Chars;
	String seq2Chars;
	String errorMessage;
	
	int ncbiCount = 0;
	
	final int MAX_SEQ_LENGTH = 10025;
	private final Cache geneCache;
	
	public FlexibleInputProvider (AbstractWebContext context, Cache geneCache) {
		seq1Type = this.getSeq1InputType(context);
		seq2Type = this.getSeq2InputType(context);
		seq1Id = context.getString(QParam.Sequence1);
		seq1Chars = context.getString(QParam.Sequence1Chars);
		seq2Id = context.getString(QParam.Sequence2);
		seq2Chars = context.getString(QParam.Sequence2Chars);
		errorMessage = null;
		this.geneCache = geneCache;
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


	private GeneSequence buildGeneSequenceFromId(String id, GenbankRichSequenceDB ncbi) throws Exception{
		GeneSequence s = this.geneCache.get(id);
		if ( s != null ) return s;
		
		try {
			RichSequence rs = ncbi.getRichSequence(id);
			String sequence =rs.seqString();
			this.ncbiCount++;
			s = GeneSequence.buildFromRichSequence(sequence, rs);
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
	private GeneSequence buildGeneSequenceFromFASTA(String fasta, GenbankRichSequenceDB ncbi) throws Exception {
		String id = getFastaPart(fasta, 4);
		RichSequence rs = null;
		if ( StringUtils.isDefined(id)) {
			try {
				rs = ncbi.getRichSequence(id);
			}
			catch  (Exception e) {
				e.printStackTrace();
			}
		}
		String sequence = fasta.substring(fasta.indexOf('\n'));
		GeneSequence s = null;
		if ( rs == null ) {
			s = GeneSequence.buildFromDirectInput(sequence, id==null?"N/A":id);
		}
		else {
			s = GeneSequence.buildFromRichSequence(sequence, rs);
		}
		return s;
	}
	
	
	public String preCheck(){
		
		/*String sequenceToBig = "This application currently cannot perform alignments on mRNA sequences with more than 10000 bases.  We are sorry for the inconvenience and intend to add support for longer sequences in the future";
		QGRSProgramInput input = getInput();
		if ( this.errorMessage != null ) {
			return this.errorMessage;
		}
		if ( input.getPrinciple().getBases().size() > MAX_SEQ_LENGTH) {
			return (sequenceToBig + " (Sequence 1 has " + input.getPrinciple().getBases().size() + " bases)"); 
		}
		if ( input.getComparisons().get(0).getBases().size() > MAX_SEQ_LENGTH) {
			return (sequenceToBig + " (Sequence 2 has " + input.getComparisons().get(0).getBases().size() + " bases)"); 
		}*/
		
		return null;
	}

	@Override
	public QGRSProgramInput getInput(){
		QGRSProgramInput input = new QGRSProgramInput();
		List<GeneSequence>comparisons=new ArrayList<GeneSequence>();
		GenbankRichSequenceDB  ncbi = null;

		try {
			

			if ( seq1Type == InputType.Id || seq2Type == InputType.Id || seq1Type == InputType.FASTA || seq2Type == InputType.FASTA ) {
				ncbi = new GenbankRichSequenceTextDB();
			}

			switch ( seq1Type ) {
			case Id:
				System.out.println("Building from rich sequence 1");
				input.setPrinciple(this.buildGeneSequenceFromId(this.seq1Id, ncbi));
				break;
			case Raw:
				input.setPrinciple(this.buildGeneSequenceFromRaw(this.seq1Chars));
				break;
			case FASTA:
				input.setPrinciple(this.buildGeneSequenceFromFASTA(this.seq1Chars, ncbi));
				break;
			}

			switch ( seq2Type ) {
			case Id:
				System.out.println("Building from rich sequence 2");
				comparisons.add(this.buildGeneSequenceFromId(this.seq2Id, ncbi));
				break;
			case Raw:
				comparisons.add(this.buildGeneSequenceFromRaw(this.seq2Chars));
				break;
			case FASTA:
				comparisons.add(this.buildGeneSequenceFromFASTA(this.seq2Chars, ncbi));
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
