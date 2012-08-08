package qgrs.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import qgrs.compute.GeneSequencePair;
import qgrs.data.Base;
import qgrs.data.BaseSymbol;
import qgrs.data.GQuadruplex;
import qgrs.data.GeneSequence;
import qgrs.data.QgrsHomology;

public class ConsoleResultProcessor extends ResultProcessor{

	//OUTPUTING TO A FILE ONLY FOR TEST PURPOSES
	public void handleResults(List<GeneSequencePair> pairs,  List<QgrsHomology> similarityResults) {
	    FileWriter fstream = null;
		try {
			fstream = new FileWriter("qgrsConsoleResultProcessorTestOutput.txt");
	        BufferedWriter out = new BufferedWriter(fstream);
	        System.out.println("Displaying results for " + pairs.size() + " pairs");
	        
	        for(GeneSequencePair pair : pairs)
	        {
	        	GeneSequence p=pair.getPrinciple();
	        	GeneSequence c=pair.getComparison();
	        	for(Base base: p.getBases())
	        	{
	        		System.out.print(base.getSymbol().toString());
	        		out.write(base.getSymbol().toString());
	        	}
	        	System.out.println();
	        	out.newLine();
	        	
	        	for(Base base: c.getBases())
	        	{
	        		System.out.print(base.getSymbol().toString());
	        		out.write(base.getSymbol().toString());
	        	}
	        	System.out.println();
	        	System.out.println("General alignment score: "+pair.getSimilarityScore());
	        	out.newLine();
	        	out.write("General alignment score: "+pair.getSimilarityScore());
	        	out.newLine();
	        	System.out.println();
	        	out.newLine();
	        	System.out.println();
	        	out.newLine();
	        	System.out.println();
	        	out.newLine();
	        	
	        }
	        
//UNCOMMENT THIS CODE IF YOU WANT THE OUTPUT WITH THE NUMBERS OF THE G-QUADRUPLEXES
//MARKED
//			out.write("Results for " + pairs.size() + " Pairs");
//			out.newLine();
//			out.write("------------------------------------");
//			out.newLine();
//			
//			System.out.println("Results for " + pairs.size() + " Pairs");
//			System.out.println("------------------------------------");
//			for ( GeneSequencePair pair : pairs ) {
//	//			System.out.println("Original Sequences:");
//	//			System.out.println("Sequence A:  " + pair.getOriginalA().toString());
//	//			System.out.println("Sequence B:  " + pair.getOriginalB().toString());
//	//			System.out.println("Aligned Sequences:");
//	//			System.out.println("Sequence A:  " + pair.getAlignedA().toString());
//	//			System.out.println("Sequence B:  " + pair.getAlignedB().toString());
//	//			System.out.println("General Alignement Score:  " + pair.getGeneralAlignmentScore());
//	//			System.out.println("------------------------------------");
//				
//				List<GQuadruplex> gquadsA=pair.getPrinciple().getgQuads();
//				List<GQuadruplex> gquadsB=pair.getComparison().getgQuads();
//				
//				out.write("Original Sequences:");
//				out.newLine();
//				out.write("Sequence A:  ");
//				out.newLine();
//				
//				System.out.println("Original Sequences:");
//				
//				System.out.println("Sequence A:  ");
//				
//				GeneSequence originalA=pair.getPrinciple();
//				displaySequence(out,originalA,gquadsA,true);
//				
//				out.newLine();
//				out.write("Sequence B:");
//				out.newLine();
//				
//				System.out.println();
//				System.out.println("Sequence B:");
//				
//				GeneSequence originalB=pair.getComparison();
//				displaySequence(out,originalB,gquadsB,true);
//				
//				out.newLine();
//				out.write("Aligned Sequences:");
//				out.newLine();
//				
//				System.out.println();
//				System.out.println("Aligned Sequences:");
//				
//				//System.out.println("Sequence A:");
//				
//				GeneSequence alignedA=pair.getPrinciple();
//				displaySequence(out,alignedA,gquadsA,false);
//				
//				//System.out.println("Sequence B:");
//				
//				out.newLine();
//				
//				System.out.println();		
//				GeneSequence alignedB=pair.getComparison();
//				displaySequence(out,alignedB,gquadsB,false);
//
//				out.newLine();
//				out.write("General Alignement Score:  " + pair.getGeneralAlignmentScore());
//				out.newLine();
//				out.write("------------------------------------");
//				out.newLine();
//				
//				
//				System.out.println();
//				System.out.println("General Alignement Score:  " + pair.getGeneralAlignmentScore());
//				System.out.println("------------------------------------");
//				System.out.println("NB:HAVE IN MIND THAT THE RESULTS TEND TO BE TOO"
//						+" LARGE FOR THE CONSOLE TO SHOW ALL, SO THEY ARE ALSO SAVED IN A FILE" +
//								"FOR TEST PURPOSES");
//			}
	        
	        
	        
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	//outputFileForTesting FOR TESTING PURPOSES ONLY. SHOULD BE DELETED AS AN ARGUMENT
	//WHEN WE DON'T OUTPUT TO A FILE
	public static void displaySequence(BufferedWriter outputFileForTesting,GeneSequence sequence,List<GQuadruplex>gquads, boolean displayOriginal) 
	{		
		boolean previousFoundOne=true;
		
		for(Base base : sequence.getBases())
		{
			boolean foundOne=false;
			for(int i=1;i<=gquads.size();i++)
			{
				
				GQuadruplex gq = gquads.get(i-1);
				if(base==gq.getStart())
				{
					foundOne=displayGQCharacter(i, base, outputFileForTesting, foundOne, previousFoundOne, 1);
					continue;
				}
				if(base==gq.getTetrad2Start())
				{
					foundOne=displayGQCharacter(i, base, outputFileForTesting, foundOne, previousFoundOne, 2);
					continue;
				}
				if(base==gq.getTetrad3Start())
				{
					foundOne=displayGQCharacter(i, base, outputFileForTesting, foundOne, previousFoundOne, 3);
					continue;
				}
				if(base==gq.getTetrad4Start())
				{
					foundOne=displayGQCharacter(i, base, outputFileForTesting, foundOne, previousFoundOne, 4);
					continue;
				}

			}
			if(!foundOne)
			{
				if(displayOriginal&&(base.getSymbol()==BaseSymbol.Gap))
				{
					continue;
				}
				try {
					outputFileForTesting.write(base.getSymbol().toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.print(base.getSymbol().toString());
				previousFoundOne=false;
			}
			else
			{
				try {
					outputFileForTesting.write(" ");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.print(" ");
				previousFoundOne=foundOne;
			}

		}
		
	}
	
	//outputFileForTesting FOR TESTING PURPOSES ONLY. SHOULD BE DELETED AS AN ARGUMENT
	//WHEN WE DON'T OUTPUT TO A FILE
	static boolean displayGQCharacter(int i,Base base,BufferedWriter outputFileForTesting, boolean foundOne,boolean previousFoundOne,int gGroup)
	{
		try 
		{
			if(foundOne)
			{
				outputFileForTesting.write("/"+i+base.getSymbol().toString()+gGroup);
				System.out.print("/"+i+base.getSymbol().toString()+gGroup);
			}
			else
			{
				if(!previousFoundOne)
				{
					outputFileForTesting.write(" ");
					System.out.print(" ");
				}
				outputFileForTesting.write(i+base.getSymbol().toString()+gGroup);
				System.out.print(i+base.getSymbol().toString()+gGroup);
				foundOne=true;
			}		
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return foundOne;
	}
	
	
}
