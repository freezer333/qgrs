package qgrs.compute.alignment;

import jaligner.Alignment;
import jaligner.Sequence;
import jaligner.SmithWatermanGotoh;
import jaligner.formats.Pair;
import jaligner.matrix.Matrix;
import jaligner.matrix.MatrixLoader;


import java.util.logging.Level;
import java.util.logging.Logger;

public class JAlignerTest {
	
	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(JAlignerTest.class.getName());
	
	private static String loadSequence1() {
		return "AAATCGTGACATCGTGACACACTTTGGCGG";
	}
	private static String loadSequence2() {
		return "TTCGGAGAGCTTTGCCAAATTCGGAGA";
	}
	
	private static String getFullSequenceGapped(String original, char []aligned, int start) {
		StringBuilder b = new StringBuilder();
		for ( int i = 0; i < start; i++ ) {
			b.append(original.charAt(i));
		}
		int curOriginal = start;
		for ( int i = 0; i < aligned.length; i++ ) {
			b.append(aligned[i]);
			if ( aligned[i] != '-' ) {
				curOriginal++;
			}
		}
		for ( int i = curOriginal; i < original.length(); i++ )  {
			b.append(original.charAt(i));
		}
		return b.toString();
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
        try {
        	logger.info("Running example...");
        	
			Sequence s1 = new Sequence(loadSequence1());
			Sequence s2 = new Sequence(loadSequence2());
	        
	        Alignment alignment = SmithWatermanGotoh.align(s1, s2, MatrixLoader.load("EDNAFULL"), 50f, 1f);
	        System.out.println("Sequence1 starting at position " + alignment.getStart1());
	        System.out.println("Sequence2 starting at position " + alignment.getStart2());
	        
	        System.out.println ( alignment.getSummary() );
	        System.out.println ( new Pair().format(alignment) );
	        
	        logger.info("Finished running example");
	        
	        System.out.println(getFullSequenceGapped(s1.getSequence(), alignment.getSequence1(), alignment.getStart1() ));
	        System.out.println(getFullSequenceGapped(s2.getSequence(), alignment.getSequence2(), alignment.getStart2() ));
        } catch (Exception e) {
        	logger.log(Level.SEVERE, "Failed running example: " + e.getMessage(), e);
        }
    }
}