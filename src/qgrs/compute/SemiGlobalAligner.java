package qgrs.compute;

/*
 * Semi-Global sequence alignment algorithm with Gotoh's improvement(a function to calculate
 * the gap penalty). Uses a combination of the algorithms found in AlignmentAlgorithms.pdf 
 * that Dr. Di'Antonio and I(Vlado) have. Similarity matrix taken from Wikipidia page for
 * Needleman-Wunsch
 */

import java.util.LinkedList;
import java.util.List;

import qgrs.data.GeneSequence;
import qgrs.job.CancelException;
import qgrs.job.CancelFlag;
import qgrs.job.DefaultCancelFlag;
import qgrs.job.JobStage;
import qgrs.job.StatusHolder;
import qgrs.utils.Matrix;
import qgrs.utils.MatrixFactory;


/* Must determine percent similarity (how many bases are aligned divided by larger, without the unaligned ends) 
 * 
 * Find first and last match.  Count bases between.  Divide matches / larger of the two number of bases.
 * 
 * 
 * 
 */

public class SemiGlobalAligner implements GeneralAligner{

	private final static SemiGlobalAligner _instance;
	static {
		_instance = new SemiGlobalAligner();
	}
	
	public static SemiGlobalAligner getInstance() {
		return _instance;
	}
	
    @Override
	public void cleanup() {
    	
	}
	public static final int A=0, G=1, C=2, T=3;
    
	public static final int gapOpen=50,gapExtend=1;
    
								// A    G   C   T
//original matrix
/*	   public final int[] similarity = {10, -1, -3, -4, //A
                                      -1,  7, -5, -3, //G
                                      -3, -5,  9,  0, //C
                                     -4, -3,  0,  8};//T 
    								// A    G   C   T
*/
	
	public final int[] similarity = { 5, -4, -4, -4,
									-4, 5, -4, -4,
									-4, -4, 5, -4, 
									-4, -4, -4, 5};
	
    CancelFlag cancelFlag = new DefaultCancelFlag();
	
	@Override
	public void setCancelFlag(CancelFlag flag) {
		cancelFlag = flag;
	}
	
    Matrix matrixE;
    Matrix matrixF; 
    StatusHolder statusHolder = null;
    
    // Cannot be instantiated - needs to be a singleton
    private SemiGlobalAligner() {}
    
	@Override
	public synchronized void align(GeneSequencePair pair, StatusHolder statusHolder) throws Exception {
		// TODO Auto-generated method stub
		this.statusHolder = statusHolder;
		GeneSequence principle=pair.getPrinciple();
		GeneSequence comparison=pair.getComparison();
		
		String strA=principle.toString();
		String strB=comparison.toString();
		
		
		int[]arrayA=GeneSequence.convertStringToArr(strA);
		int[]arrayB=GeneSequence.convertStringToArr(strB);
		
		Matrix alignMatrix=null;
		List<GeneSequence>alignedSequences=null;
		
		if ( statusHolder != null ) statusHolder.setStatus(JobStage.Alignment_Calc, 0, null);
		if(strA.length()>=strB.length())
	    {
	    	alignMatrix = calculateMatrix(arrayA, arrayB);
	    	alignedSequences= getAlignments(alignMatrix, arrayA, arrayB, strA,strB, principle, comparison);
			pair.setSimilarityScore(alignMatrix.get(arrayB.length,arrayA.length));
	    }
	    else
	    {
	    	alignMatrix = calculateMatrix(arrayB, arrayA);
	    	alignedSequences= getAlignments(alignMatrix, arrayB, arrayA, strB,strA, principle, comparison);
			pair.setSimilarityScore(alignMatrix.get(arrayA.length,arrayB.length));
	    }
        

		if ( statusHolder != null ) statusHolder.setStatus(JobStage.Alignment_Apply, 1, "Applying gaps");
		if(principle.getBases().size()>=comparison.getBases().size())
		{
			principle.addGaps(alignedSequences.get(0));
			comparison.addGaps(alignedSequences.get(1));
		}
		else
		{
			principle.addGaps(alignedSequences.get(1));
			comparison.addGaps(alignedSequences.get(0));			
		}
		alignMatrix.close();
		matrixE.close();
	    matrixF.close(); 
	    matrixE = null;
	    matrixF = null;
	    alignMatrix = null;
	    this.statusHolder = null;
	}

    
	private Matrix calculateMatrix(int[] first, int[] second) throws CancelException
	{
		int longer,shorter;

		longer=first.length;
		shorter=second.length;

		Matrix matrixS = MatrixFactory.build(shorter+1,longer+1);
		try {
			matrixS.open();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		matrixE = MatrixFactory.build(shorter+1,longer+1);
		matrixF = MatrixFactory.build(shorter+1,longer+1);  

		try {
			matrixE.open();
			matrixF.open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		matrixS.put(0, 0, 0);
		matrixE.put(0, 0, 0);
		matrixF.put(0, 0, 0);

		for (int y = 1; y <= longer; y++)
		{

			matrixS.put(0, y, 0);
			matrixF.put(0, y, 0);

		}
		if ( this.cancelFlag.isRaised()) throw new CancelException();  
		for (int x = 1; x <= shorter; x++)
		{
			matrixS.put(x, 0, 0);
			matrixE.put(x, 0, 0); 
		}
		if ( this.cancelFlag.isRaised()) throw new CancelException();

		int n = 1;
		for (int x = 1; x <= shorter; x++) {
			for (int y = 1; y <= longer; y++)
			{   
				this.statusHolder.setStatus(JobStage.Alignment_Calc, n / (float)(shorter*longer), null);
				n++;

				// CHANGES TO TEST CODE!!! 
				//	int e = Math.max(matrixS.get(x, y-1) -gapOpen,matrixE.get(x, y-1)-gapExtend);
				//	int f = Math.max(matrixS.get(x-1, y)-gapOpen,matrixF.get(x-1, y)-gapExtend);     

				int e, f;

				//no gap penalties in first column or row
				if (x == shorter && y == longer){
					e = matrixS.get(x, y-1);
					f = matrixS.get(x-1, y);
				}
				else {
					//no gap penalties in last column
					if ( x == shorter){
						e = matrixS.get(x, y-1);

						//without Gotoh
						//f = matrixS.get(x-1, y) -gapExtend;
						
						//with Gotoh
						f = Math.max(matrixS.get(x-1, y)-gapOpen - gapExtend,matrixF.get(x-1, y)-gapExtend);   
					}
					else {
						//no gap penalties in last row
						if ( y == longer){
							//without Gotoh
							//e = matrixS.get(x, y-1) - gapExtend;
							
							//with Gotoh
							e = Math.max(matrixS.get(x, y-1) -gapOpen - gapExtend,matrixE.get(x, y-1)-gapExtend);
							
							f = matrixS.get(x-1, y);
						}
						else {
							//without Gotoh
							//e = matrixS.get(x, y-1) -gapExtend;
							//f = matrixS.get(x-1, y) -gapExtend;
						
							//with Gotoh
							e = Math.max(matrixS.get(x, y-1) -gapOpen - gapExtend,matrixE.get(x, y-1)-gapExtend);
							f = Math.max(matrixS.get(x-1, y)-gapOpen - gapExtend,matrixF.get(x-1, y)-gapExtend);   
						}
					}
				}

				// END - CHANGES

				matrixE.put(x, y, e);
				matrixF.put(x, y, f);
				int maxBetweenGaps=Math.max(e, f);
				int noGap;
				noGap = matrixS.get(x-1, y-1) + similar(second[x-1],first[y-1]);
				matrixS.put(x, y, Math.max(maxBetweenGaps,noGap));
			}
			if ( this.cancelFlag.isRaised()) throw new CancelException();
		}
		return matrixS;
	}

    private  List<GeneSequence> getAlignments(Matrix matrix, int[] longerArr, int[] shorterArr,
    		String longer, String shorter, GeneSequence principle, GeneSequence comparison) throws CancelException
    {
    	List<GeneSequence> alSequences=new LinkedList<GeneSequence>();
        String aOneA = "";
        String aOneB = "";

        int j = longer.length();
        int i = shorter.length();
       
        while (i > 0 && j > 0)
        {
        	this.statusHolder.setStatus(JobStage.Alignment_Apply,  (longer.length()-i) / (float)longer.length(), null);
        	int score = matrix.get(i, j);
        	int scorediag = matrix.get(i-1, j-1);
        /*	if (score == scorediag + similar(longerArr[j-1], shorterArr[i-1]))
        	{
        		aOneA = longer.charAt(j-1) + aOneA;
        		aOneB = shorter.charAt(i-1) + aOneB;
        		i--;j--;                
        	}
        	else if (score == matrixE.get(i, j))
        	{
        		aOneA = longer.charAt(j-1) + aOneA;
        		aOneB = "-" + aOneB;
        		j--;
        	}
        	else if(score == matrixF.get(i, j))
        	{
        		aOneA = "-" + aOneA;
        		aOneB = shorter.charAt(i-1) + aOneB;
        		i--;
        	}    */
        	
        	if (score == matrixE.get(i, j))
        	{
        		aOneA = longer.charAt(j-1) + aOneA;
        		aOneB = "-" + aOneB;
        		j--;
        	}
        	else if(score == matrixF.get(i, j))
        	{
        		aOneA = "-" + aOneA;
        		aOneB = shorter.charAt(i-1) + aOneB;
        		i--;
        	} 
        	else if (score == scorediag + similar(longerArr[j-1], shorterArr[i-1]))
        	{
        		aOneA = longer.charAt(j-1) + aOneA;
        		aOneB = shorter.charAt(i-1) + aOneB;
        		i--;j--;                
        	}
        	
        }
        if ( this.cancelFlag.isRaised()) throw new CancelException();
        while(j > 0)
        {
            aOneA = longer.charAt(j - 1) + aOneA;
            aOneB = "-" + aOneB;
            j--;            
        }
        while(i > 0)
        {
            aOneA = "-" + aOneA;
            aOneB = shorter.charAt(i - 1) + aOneB;
            i--;            
        }
        try {
			alSequences.add(GeneSequence.buildFromSourceSequence(aOneA, principle));
	        alSequences.add(GeneSequence.buildFromSourceSequence(aOneB, comparison));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return alSequences;

    }
    
    private int similar(int first, int second)
    {
        return similarity[first * 4 + second];
    }
    
  /*  private int gapFunction(int i)
    {
    	return gapOpen+gapExtend*(i-1);
    } */


}
