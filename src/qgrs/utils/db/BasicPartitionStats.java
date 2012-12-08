package qgrs.utils.db;

import java.text.DecimalFormat;

public class BasicPartitionStats extends AbstractDbTask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new BasicPartitionStats().report();

	}
	
	public void print(String where, String label) {
		
		DecimalFormat format = new DecimalFormat("0.0%");
		int countConserved = executeCount("SELECT COUNT(DISTINCT id) FROM HC_QGRS_J " + where);
		int count99Conserved = executeCount("SELECT COUNT(DISTINCT id) FROM HC99_QGRS_J " + where);
		int countTotal = executeCount( "SELECT COUNT(DISTINCT id) FROM HUMAN_QGRS " + where);
		double percentConserved = ((double) countConserved )/countTotal;
		double percent99Conserved = ((double) count99Conserved )/countTotal;
		
		System.out.println(padRight(label, 25) + ":  " + format.format(percentConserved) + "\t" + format.format(percent99Conserved) + "\t" + countConserved + "\t" + count99Conserved + "\t" + countTotal);

		
	}
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	@Override
	public void report() {
		
		System.out.println("\t\t\t%95\t\t%99.9\t#95\t#99\t#Total");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		print("", "Entire transcriptome");
		print("WHERE in5Prime = 'true'", "5 UTR");
		print("WHERE inCds = 'true'", "CDS");
		print("WHERE in3Prime = 'true'", "3 UTR");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		print("WHERE numTetrads = '2'", "2  Tetrads");
		print("WHERE numTetrads = '3'", "3  Tetrads");
		print("WHERE numTetrads >= '4'", "4+ Tetrads");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		print("WHERE in5Prime = 'true' AND numTetrads = '2'", "5 UTR / 2  Tetrads");
		print("WHERE in5Prime = 'true' AND numTetrads = '3'", "5 UTR / 3  Tetrads");
		print("WHERE in5Prime = 'true' AND numTetrads >= '4'", "5 UTR / 4+ Tetrads");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		print("WHERE inCds = 'true' AND numTetrads = '2'", "CDS / 2  Tetrads");
		print("WHERE inCds = 'true' AND numTetrads = '3'", "CDS / 3  Tetrads");
		print("WHERE inCds = 'true' AND numTetrads >= '4'", "CDS / 4+ Tetrads");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		print("WHERE in3Prime = 'true' AND numTetrads = '2'", "3 UTR / 2  Tetrads");
		print("WHERE in3Prime = 'true' AND numTetrads = '3'", "3 UTR / 3  Tetrads");
		print("WHERE in3Prime = 'true' AND numTetrads >= '4'", "3 UTR / 4+ Tetrads");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		
		/*countConserved = executeCount("SELECT COUNT(DISTINCT id) FROM HC_QGRS_J WHERE in5Prime = 'true'");
		count99Conserved = executeCount("SELECT COUNT(DISTINCT id) FROM HC99_QGRS_J WHERE in5Prime = 'true'");
		countTotal = executeCount( "SELECT COUNT(DISTINCT id) FROM HUMAN_QGRS WHERE in5Prime = 'true'");
		percentConserved = ((double) countConserved )/countTotal;
		percent99Conserved = ((double) count99Conserved )/countTotal;
		System.out.println("% Conserved (5'):             " + format.format(percentConserved) + "\t" + format.format(percent99Conserved) + "\t" + countConserved + "\t" + count99Conserved + "\t" + countTotal);

		countConserved = executeCount("SELECT COUNT(DISTINCT id) FROM HC_QGRS_J WHERE inCds = 'true'");
		count99Conserved = executeCount("SELECT COUNT(DISTINCT id) FROM HC99_QGRS_J WHERE inCds = 'true'");
		countTotal = executeCount( "SELECT COUNT(DISTINCT  id) FROM HUMAN_QGRS WHERE inCds = 'true'");
		percentConserved = ((double) countConserved )/countTotal;
		percent99Conserved = ((double) count99Conserved )/countTotal;
		System.out.println("% Conserved (CDS):            " + format.format(percentConserved) + "\t" + format.format(percent99Conserved) + "\t" + countConserved + "\t" + count99Conserved + "\t" + countTotal);

		countConserved = executeCount("SELECT COUNT(DISTINCT  id) FROM HC_QGRS_J WHERE in3Prime = 'true'");
		count99Conserved = executeCount("SELECT COUNT(DISTINCT  id) FROM HC99_QGRS_J WHERE in3Prime = 'true'");
		countTotal = executeCount( "SELECT COUNT(DISTINCT  id) FROM HUMAN_QGRS WHERE in3Prime = 'true'");
		percentConserved = ((double) countConserved )/countTotal;
		percent99Conserved = ((double) count99Conserved )/countTotal;
		System.out.println("% Conserved (3'):             " + format.format(percentConserved) + "\t" + format.format(percent99Conserved) + "\t" + countConserved + "\t" + count99Conserved + "\t" + countTotal);
		
		
		
		countConserved = executeCount("SELECT COUNT(DISTINCT  id) FROM HC_QGRS_J WHERE numTetrads = '2'");
		count99Conserved = executeCount("SELECT COUNT(DISTINCT  id) FROM HC99_QGRS_J WHERE numTetrads = '2'");
		countTotal = executeCount( "SELECT COUNT(DISTINCT  id) FROM HUMAN_QGRS WHERE numTetrads = '2'");
		percentConserved = ((double) countConserved )/countTotal;
		percent99Conserved = ((double) count99Conserved )/countTotal;
		System.out.println("% Conserved (2 Tetrads):      " + format.format(percentConserved) + "\t" + format.format(percent99Conserved) + "\t" + countConserved + "\t" + count99Conserved + "\t" + countTotal);

		countConserved = executeCount("SELECT COUNT(DISTINCT  id) FROM HC_QGRS_J WHERE numTetrads = '3'");
		count99Conserved = executeCount("SELECT COUNT(DISTINCT  id) FROM HC99_QGRS_J WHERE numTetrads = '3'");
		countTotal = executeCount( "SELECT COUNT(DISTINCT  id) FROM HUMAN_QGRS WHERE numTetrads = '3'");
		percentConserved = ((double) countConserved )/countTotal;
		percent99Conserved = ((double) count99Conserved )/countTotal;
		System.out.println("% Conserved (3 Tetrads):      " + format.format(percentConserved) + "\t" + format.format(percent99Conserved) + "\t" + countConserved + "\t" + count99Conserved + "\t" + countTotal);

		countConserved = executeCount("SELECT COUNT(DISTINCT  id) FROM HC_QGRS_J WHERE numTetrads = '4'");
		count99Conserved = executeCount("SELECT COUNT(DISTINCT  id) FROM HC99_QGRS_J WHERE numTetrads = '4'");
		countTotal = executeCount( "SELECT COUNT(DISTINCT  id) FROM HUMAN_QGRS WHERE numTetrads = '4'");
		percentConserved = ((double) countConserved )/countTotal;
		percent99Conserved = ((double) count99Conserved )/countTotal;
		System.out.println("% Conserved (4 Tetrads):      " + format.format(percentConserved) + "\t" + format.format(percent99Conserved) + "\t" + countConserved + "\t" + count99Conserved + "\t" + countTotal);

		countConserved = executeCount("SELECT COUNT(DISTINCT  id) FROM HC_QGRS_J WHERE numTetrads >= '5'");
		count99Conserved = executeCount("SELECT COUNT(DISTINCT  id) FROM HC99_QGRS_J WHERE numTetrads >= '5'");
		countTotal = executeCount( "SELECT COUNT(DISTINCT  id) FROM HUMAN_QGRS WHERE numTetrads >= '5'");
		percentConserved = ((double) countConserved )/countTotal;
		percent99Conserved = ((double) count99Conserved )/countTotal;
		System.out.println("% Conserved (5+ Tetrads):     " + format.format(percentConserved) + "\t" + format.format(percent99Conserved) + "\t" + countConserved + "\t" + count99Conserved + "\t" + countTotal);

		*/
		
		closeConnection();
	}

}
