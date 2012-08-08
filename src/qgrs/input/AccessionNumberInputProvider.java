package qgrs.input;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.biojava.bio.BioException;
import org.biojava.bio.seq.db.IllegalIDException;
import org.biojavax.bio.db.ncbi.GenbankRichSequenceDB;
import org.biojavax.bio.seq.RichSequence;

import qgrs.data.GeneSequence;
import qgrs.db.GenbankRichSequenceTextDB;

public class AccessionNumberInputProvider implements InputProvider {
	private LinkedList<String>sequenceNumbers= new LinkedList<String>();

	private final String acsession1;
	private final String acsession2;

	public AccessionNumberInputProvider(String acs1, String acs2)
	{
		this.acsession1 = acs1;
		this.acsession2 = acs2;
	}
	@Override
	public QGRSProgramInput getInput() {
		sequenceNumbers.addLast(this.acsession1);
		sequenceNumbers.addLast(this.acsession2);
		QGRSProgramInput input = new QGRSProgramInput();
		GeneSequence principle = null;
		List<GeneSequence>comparisons=new ArrayList<GeneSequence>();

		GenbankRichSequenceDB  ncbi = new GenbankRichSequenceTextDB();
		int nSequencesProcessed=0;
		try {
			for(nSequencesProcessed=0;nSequencesProcessed<sequenceNumbers.size();nSequencesProcessed++)
			{
				RichSequence rs = ncbi.getRichSequence(sequenceNumbers.get(nSequencesProcessed));
				String sequence=rs.seqString();

				if(nSequencesProcessed==0)
				{
					try {
						principle = GeneSequence.buildFromRichSequence(sequence, rs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					try {
						GeneSequence c = GeneSequence.buildFromRichSequence(sequence, rs);
						comparisons.add(c);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}


			}
		} catch (IllegalIDException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();


		} catch (BioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} 
		input.setPrinciple(principle);
		input.setComparisons(comparisons);
		
		return input;
	}

}
