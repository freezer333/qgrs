package qgrs.utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import qgrs.db.AppProperties;
import framework.db.DatabaseConnectionParameters;
import framework.web.ResourceResolver;

public class ViewCreator {

	DatabaseConnectionParameters params ;
	
	public ViewCreator (ResourceResolver r) {
		params = new DatabaseConnectionParameters(AppProperties.getConnectionString(r), "sa", "sa");
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
	void dropTable(Connection conn, String table) {
		try {
			System.out.println("DROPPING "+table+" Table");
			String q = "DROP TABLE "+table;
			System.out.println(q);
			execute(conn, q);
		}
		catch (Exception e) {
			System.out.println(table + " does not exist");
		}
	}
	
	public void createIndexes() {
		
	}
	
	
	public void createConservedQgrsView() {
		Connection conn = getConnection();
		
		System.out.println("Create a view table which contains QGRS instances with the following constraints:  ");
		System.out.println("\tIt is found within human mRNA");
		System.out.println("\tIt is the principle qgrs in at least one qgrs-h record which ");
		System.out.println("\thas a homology score > 0.95");
		System.out.println("The QGRS data will also be merged with gene information for quick access");
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("This script will remove the existing view first.  You will not lose the underlying data.");
		System.out.println("Populating the conserved human QGRS view may take several hours (yes... hours).");
		System.out.println("------------------------------------------------------------------------------------");
		
		// First drop the table if it exists.
		dropTable(conn, "CONSERVED_QGRS");
		// Create the table that serves as the view
		String q = "CREATE TABLE CONSERVED_QGRS (" +
					"id varchar(255), " +  
					"geneId  varchar(255), " + 
					"sequenceSlice varchar(255), " +
					"tetrad1 int, " +
					"tetrad2 int, " +
					"tetrad3 int, " +
					"tetrad4 int, " +
					"loop1Length int, " +
					"loop2Length int, " +
					"loop3Length int, " +
					"totalLength int, " +
					"gScore int, " +
					"numTetrads int, " +
					"in5Prime boolean, " + 
					"inCds boolean, " + 
					"in3Prime boolean, " +
					"distanceFromPolyASignal int, " + 
					"geneSymbol varchar(255), " +
					"geneName varchar(255), " +
					"cdsStart int, " +
					"cdsEnd int" +
					")";
		System.out.println(q);
		execute(conn, q) ;
		
		
		// Now issue the batch insertion
		System.out.println("Populating the conserved human QGRS table, this may take several hours (yes... hours).");
		q = "INSERT INTO HC_QGRS "  + 
		    "SELECT QGRS.id, " +  
				   "QGRS.geneId, " + // key to determine which gene of the pair is represented
				   "QGRS.sequenceSlice, " +
				   "QGRS.tetrad1, " +
				   "QGRS.tetrad2, " +
				   "QGRS.tetrad3, " +
				   "QGRS.tetrad4, " +
				   "QGRS.loop1Length, " +
				   "QGRS.loop2Length, " +
				   "QGRS.loop3Length, " +
				   "QGRS.totalLength, " +
				   "QGRS.gScore, " +
				   "QGRS.numTetrads, " +
				   "QGRS.in5Prime, " + 
				   "QGRS.inCds, " + 
				   "QGRS.in3Prime, " +
				   "QGRS.distanceFromPolyASignal, " +
				   "GENE.geneSymbol," +
				   "GENE.geneName, " +
				   "GENE.cdsStart, " +
				   "GENE.cdsEnd" +
			" FROM QGRS JOIN GENE ON GENE.accessionNumber = QGRS.geneId WHERE QGRS.id IN ( " +
				  " SELECT QGRS_H.gq1Id FROM QGRS_H " + 
				  " WHERE QGRS_H.overallScore > 0.95 )";
		   
		System.out.println(q);
		execute(conn, q) ;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
