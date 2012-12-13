package qgrs.input;

import org.biojavax.bio.db.ncbi.GenbankRichSequenceDB;
import org.biojavax.bio.seq.RichSequence;

import qgrs.data.GeneSequence;
import qgrs.db.Cache;
import qgrs.db.GenbankRichSequenceTextDB;
import qgrs.db.NullCache;

public class AccessionNumberInputProvider implements InputProvider {
	
	private final String acsession1;
	private final String acsession2;
	private final Cache geneCache;
	private int numNcbiCalls = 0;

	public AccessionNumberInputProvider(String acs1, String acs2) {
		this(acs1, acs2, new NullCache() );
	}
	public AccessionNumberInputProvider(String acs1, String acs2, Cache readCache)
	{
		this.acsession1 = acs1;
		this.acsession2 = acs2;
		this.geneCache = readCache;
	}
	
	
	@Override
	public QGRSProgramInput getInput() {
	
		GeneSequence principle = geneCache.get(this.acsession1);
		GeneSequence comparison = geneCache.get(this.acsession2);
		
		QGRSProgramInput input = new QGRSProgramInput();
		
		if ( principle == null || comparison == null ){
			try {
				GenbankRichSequenceDB  ncbi = new GenbankRichSequenceTextDB();
				RichSequence rs;
				String sequence;
				
				if ( principle == null ) {
					rs = ncbi.getRichSequence(acsession1);
					sequence=rs.seqString();
					principle = GeneSequence.buildFromRichSequence(sequence, rs);
					this.numNcbiCalls++;
				}
				
				if ( comparison == null ) {
					rs = ncbi.getRichSequence(acsession2);
					sequence=rs.seqString();
					comparison = GeneSequence.buildFromRichSequence(sequence, rs);
					this.numNcbiCalls++;
				}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		input.setPrinciple(principle);
		input.setComparison(comparison);
		
		return input;
	}
	
	@Override
	public int getNumNcbiCalls() {
		return numNcbiCalls;
	}
	

}
