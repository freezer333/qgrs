package qgrs.utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import qgrs.db.AppProperties;
import framework.db.DatabaseConnectionParameters;
import framework.web.ResourceResolver;

public class ViewCreator {

	DatabaseConnectionParameters params ;
	
	public ViewCreator () {
		params = new DatabaseConnectionParameters("jdbc:h2:file:C:/projects/qgrs/database/qgrsdb", "sa", "sa");
	}
	
	Connection getConnection() {
		try {
		 Class.forName("org.h2.Driver");
	        Connection conn = DriverManager.
	            getConnection(params.getConnectionString(), params.getUsername(), params.getPassword());
	        return conn;
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	void closeConnection(Connection c) {
		try {
			if ( c != null ) {
				c.close() ;
			}
		}
		catch (Exception e ){
			throw new RuntimeException (e);
		}
	}
	
	void execute(Connection conn, String q) {
		try {
			Statement stmt = conn.createStatement();
	   		stmt.executeUpdate(q);
			stmt.close();
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	
	
	public void createConservedQgrsView() {
		Connection conn = getConnection();
		
		String q = "CREATE OR REPLACE VIEW HUMAN_QGRS_CONSERVED AS " +
				   "SELECT " +
				   "QGRS.id,   QGRS.geneId,    QGRS.sequenceSlice,    QGRS.tetrad1,    QGRS.tetrad2,    QGRS.tetrad3, " +
                   "QGRS.tetrad4,    QGRS.loop1Length,    QGRS.loop2Length,   QGRS.loop3Length,    QGRS.totalLength,    " + 
                   "QGRS.gScore,   QGRS.numTetrads,  " +
                   "QGRS.in5Prime,    QGRS.inCds,    QGRS.in3Prime, QGRS.distanceFromPolyASignal,  " +
                   "GENE.geneName, GENE.geneSymbol, GENE.sequenceLength,  " +
                   "GENE.cdsStart, GENE.cdsEnd, GENE.SPECIES " +
				   "FROM QGRS JOIN GENE ON GENE.accessionNumber = QGRS.geneId JOIN QGRS_H ON QGRS_H.gq1Id=QGRS.id WHERE QGRS_H.overallScore > 0.95 AND GENE.SPECIES = 'Homo sapiens' "; 
		execute(conn, q) ;
		System.out.println("Created View:  HUMAN_QGRS_CONSERVED");
		
		q = "CREATE OR REPLACE VIEW HUMAN_QGRS AS  " +
			"SELECT 	QGRS.id,   QGRS.geneId,    QGRS.sequenceSlice,    QGRS.tetrad1,    QGRS.tetrad2,    QGRS.tetrad3,  " +
            "QGRS.tetrad4,    QGRS.loop1Length,    QGRS.loop2Length,   QGRS.loop3Length,    QGRS.totalLength,     " +
            "QGRS.gScore,   QGRS.numTetrads,  " +
            "QGRS.in5Prime,    QGRS.inCds,    QGRS.in3Prime, QGRS.distanceFromPolyASignal,  " +
            "GENE.geneName, GENE.geneSymbol, GENE.sequenceLength,  " +
            "GENE.cdsStart, GENE.cdsEnd, GENE.SPECIES " +
            "FROM QGRS JOIN GENE ON GENE.accessionNumber = QGRS.geneId WHERE GENE.SPECIES = 'Homo sapiens' " ;
		execute(conn, q) ;
		System.out.println("Created View:  HUMAN_QGRS");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ViewCreator().createConservedQgrsView();
	}

}
