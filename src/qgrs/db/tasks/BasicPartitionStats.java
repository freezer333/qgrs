package qgrs.db.tasks;

import java.text.DecimalFormat;

public class BasicPartitionStats extends AbstractDbTask {

	
	/*
	 * 	CREATE OR REPLACE VIEW HC_QGRS_J AS 
   		SELECT QGRS.id,   QGRS.geneId,    QGRS.sequenceSlice,    QGRS.tetrad1,    QGRS.tetrad2,    QGRS.tetrad3, 
                   QGRS.tetrad4,    QGRS.loop1Length,    QGRS.loop2Length,   QGRS.loop3Length,    QGRS.totalLength,     
                   QGRS.gScore,   QGRS.numTetrads, 
                   QGRS.in5Prime,    QGRS.inCds,    QGRS.in3Prime, QGRS.distanceFromPolyASignal, 
                   GENE.geneName, GENE.geneSymbol, GENE.sequenceLength, 
                   GENE.cdsStart, GENE.cdsEnd, GENE.SPECIES, 
                   CASE P_TETRADS < C_TETRADS THEN P_TETRADS ELSE C_TETRADS END as hTetrads, 
				   CASE P_GSCORE < C_GSCORE THEN P_GSCORE ELSE C_GSCORE END as hGScore
				FROM QGRS JOIN GENE ON GENE.accessionNumber = QGRS.geneId JOIN QGRS_H ON QGRS_H.gq1Id=QGRS.id WHERE QGRS_H.overallScore > 0.95 AND GENE.SPECIES = 'Homo sapiens'
	 * 
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new BasicPartitionStats().report();

	}
	
	public void print(String where, String label) {
		
		DecimalFormat format = new DecimalFormat("0.0%");
		int countConserved = executeCount("SELECT COUNT(DISTINCT id) FROM HC_QGRS_J " + where);
		int countTotal = executeCount( "SELECT COUNT(DISTINCT id) FROM HUMAN_QGRS " + where);
		double percentConserved = ((double) countConserved )/countTotal;
		
		System.out.println(padRight(label, 25) + ":  " + format.format(percentConserved) + "\t" +  countConserved + "\t" +  countTotal);

		
	}
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	@Override
	public void report() {
		
		System.out.println("\t\t\t\t%95\t#95\t#99\t#Total");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		print("", "Entire transcriptome");
		print("WHERE in5Prime = 'true'", "5 UTR");
		print("WHERE inCds = 'true'", "CDS");
		print("WHERE in3Prime = 'true'", "3 UTR");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		print("WHERE hTetrads = '2'", "2  Tetrads");
		print("WHERE hTetrads = '3'", "3  Tetrads");
		print("WHERE hTetrads >= '4'", "4+ Tetrads");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		print("WHERE in5Prime = 'true' AND hTetrads = '2'", "5 UTR / 2  Tetrads");
		print("WHERE in5Prime = 'true' AND hTetrads = '3'", "5 UTR / 3  Tetrads");
		print("WHERE in5Prime = 'true' AND hTetrads >= '4'", "5 UTR / 4+ Tetrads");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		print("WHERE inCds = 'true' AND hTetrads = '2'", "CDS / 2  Tetrads");
		print("WHERE inCds = 'true' AND hTetrads = '3'", "CDS / 3  Tetrads");
		print("WHERE inCds = 'true' AND hTetrads >= '4'", "CDS / 4+ Tetrads");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		print("WHERE in3Prime = 'true' AND hTetrads = '2'", "3 UTR / 2  Tetrads");
		print("WHERE in3Prime = 'true' AND hTetrads = '3'", "3 UTR / 3  Tetrads");
		print("WHERE in3Prime = 'true' AND hTetrads >= '4'", "3 UTR / 4+ Tetrads");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		
	
		
		closeConnection();
	}

}
