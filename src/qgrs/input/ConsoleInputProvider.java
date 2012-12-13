
package qgrs.input;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import qgrs.data.GeneSequence;


public class ConsoleInputProvider implements InputProvider {

	@Override
	public QGRSProgramInput getInput() {
		
		QGRSProgramInput input = new QGRSProgramInput();
		GeneSequence principle = null;
		List<GeneSequence>comparisons=new ArrayList<GeneSequence>();
		InputStreamReader streamReader = new InputStreamReader(System.in);
		BufferedReader cin = new BufferedReader(streamReader);


		
		// ASk user to enter the principle gene (check for illegal characters)
		while(true)
		{

			System.out.println("Enter principle gene:");
			String principleStr;
			try {
				principleStr = cin.readLine();
				principle=GeneSequence.buildFromDirectInput(principleStr);
				break;
			}
			catch(Exception e)
			{
				System.out.println("Illegal input");
				continue;
			}

			
				
		}
		
		// Ask for number of comparison gene sequences...
		int numComp=0;
		while(true&&numComp<=0)
		{
			System.out.println("How many comparison sequences?");
			try
			{
				numComp=Integer.parseInt(cin.readLine());
				break;
			}
			catch(Exception e)
			{
				//System.out.println("Illegal input ");
				continue;
			}

		}
		
		
		// Get  the comparison sequences
		for(int i=1;i<=numComp;i++)
		{
			System.out.println("Type in comarison sequence no."+i);

			try
			{
				String sequenceStr=cin.readLine();
				comparisons.add(i-1, GeneSequence.buildFromDirectInput(sequenceStr));
				//continue;
			}
			catch(Exception e)
			{
				System.out.println("Illegal input");
				
				i--;
			}
			
		}
		
		
		// Assemble input and return;
		input.setPrinciple(principle);
		input.setComparisons(comparisons);

		return input;
	}

	@Override
	public int getNumNcbiCalls() {
		// TODO Auto-generated method stub
		return 0;
	}

}
