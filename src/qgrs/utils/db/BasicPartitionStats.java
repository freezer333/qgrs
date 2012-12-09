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
		int countConserved = executeCount("SELECT COUNT(DISTINCT id) FROM HUMAN_QGRS_CONSERVED " + where);
		int countTotal = executeCount( "SELECT COUNT(DISTINCT id) FROM HUMAN_QGRS " + where);
		double percentConserved = ((double) countConserved )/countTotal;
		
		System.out.println(padRight(label, 25) + ":  " + format.format(percentConserved) + "\t" +  countConserved + "\t" +  countTotal);

		
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
		
	
		
		closeConnection();
	}

}
