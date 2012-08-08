package qgrs.input;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.biojava.bio.BioException;
import org.biojava.bio.seq.db.IllegalIDException;
import org.biojavax.bio.db.ncbi.GenbankRichSequenceDB;
import org.biojavax.bio.seq.RichSequence;

import qgrs.data.GeneSequence;
import qgrs.db.GenbankRichSequenceTextDB;


public class SequenceNumberInputProvider implements InputProvider {
	public List<String>sequenceNumbers= new LinkedList<String>();
	
	public SequenceNumberInputProvider()
	{
		
	}
	@Override
	public QGRSProgramInput getInput() {
		// TODO Auto-generated method stub
       // =new ArrayList<String>();
		FileWriter fstream = null;
		try {
			fstream = new FileWriter("qgrsConsoleResultProcessorTestInput.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedWriter out = new BufferedWriter(fstream);
        int nSNEntered=0;
        while(true)
	    {

        	System.out.println("enter SequenceNumber or type \"quit\" to quit."
        	+"Need to have entered at least 2 sequence numbers to genetrate valid"
        	+"input");
	    	InputStreamReader isr = new InputStreamReader(System.in);
	    	BufferedReader br = new BufferedReader(isr);
	    	String userInput ="";
	    	try {
	    		userInput = br.readLine();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(userInput.trim().toLowerCase().equals("quit"))
			{
				break;
			}
			if(userInput.isEmpty())
			{
				continue;
			}
			sequenceNumbers.add(nSNEntered, userInput);
			nSNEntered++;
		}
        if(sequenceNumbers.size()<2)
        {
        	System.out.println("Too few sequences entered to construct"
        	+"QGRSProgramInput");
        	return null;
        }
        
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
				out.write(sequence);
		        out.newLine();
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
						comparisons.add(GeneSequence.buildFromRichSequence(sequence, rs));
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

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		input.setPrinciple(principle);
		input.setComparisons(comparisons);
		//System.out.println("Success!");
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
	


}
