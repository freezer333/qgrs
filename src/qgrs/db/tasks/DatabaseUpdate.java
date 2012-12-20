package qgrs.db.tasks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import qgrs.db.AppProperties;
import framework.db.DatabaseConnectionParameters;
import framework.web.ResourceResolver;

public class DatabaseUpdate {

	DatabaseConnectionParameters params ;
	boolean drop = true;
	
	public DatabaseUpdate (ResourceResolver r) {
		params = new DatabaseConnectionParameters(AppProperties.getConnectionString(r), "sa", "sa");
		drop = AppProperties.dropTables(r);
	}
	
	public void update() {
		Connection conn = getConnection();
		try {
			this.createGenesTable(conn);
			new AddKeys().execute();
			DbIndex.makeIndexes(conn);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally {
			if ( conn != null ) {
				closeConnection(conn);
			}
		}
		
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
	
	
	
	void createGenesTable(Connection conn) {
			String q = "CREATE TABLE IF NOT EXISTS GENE (accessionNumber char(50), " +
					"sequenceLength int, " +
					"giNumber varchar(255), " +
					"geneSymbol varchar(255), " +
					"ncbiLink varchar(255), " +
					"geneName varchar(255), " +
					"species varchar(255), " +
					"cdsStart int, " +
					"cdsEnd int, " +
					"utr3Start int, " +
					"utr3End int, " +
					"utr5Start int, " +
					"utr5End int" +
					")";
			execute(conn, q);
			System.out.println("GENE Table Created");
			
			q = "CREATE TABLE IF NOT EXISTS POLY_A_SITE (accessionNumber char(50), " +
					"Start int, " +
					"End int" +
					")";
			execute(conn, q);
			System.out.println("POLY_A_SITE Table Created");
			
			q = "CREATE TABLE IF NOT EXISTS POLY_A_SIGNAL (accessionNumber char(50), " +
					"Start int, " +
					"End int" +
					")";
			execute(conn, q);
			System.out.println("POLY_A_SIGNAL Table Created");
			
			q = "CREATE TABLE IF NOT EXISTS GENE_A (" +
					"id varchar(255), " +
					"principle varchar(255), " +
					"comparison varchar(255), " +
					"similarityScore int, " +
					"similarityPercentage double, " +
					"alignmentBuildKey varchar(255), " +
					"dateAligned date" +
					")";
			execute(conn, q);
			System.out.println("GENE-A Table Created");
			
			
			q = "CREATE TABLE IF NOT EXISTS GENE_A_SEQ (" +
					"id varchar(255), " +  // Foreign key to GENE-A
					"accessionNumber varchar(255), " + // key to determine which gene of the pair is represented
					"sequence CLOB" +
					")";
			execute(conn, q);
			System.out.println("GENE-A-SEQ Table Created");
			
			q = "CREATE TABLE IF NOT EXISTS QGRS (" +
					"id varchar(255), " +  
					"geneId  varchar(255), " + // key to determine which gene of the pair is represented
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
					"buildKey varchar(255), " +
					"in5Prime boolean, " + 
					"inCds boolean, " + 
					"in3Prime boolean, " +
					"distanceFromPolyASignal int" + 
					")";
			execute(conn, q);
			
			q = "CREATE TABLE IF NOT EXISTS QGRS_H (" +
					"id int AUTO_INCREMENT PRIMARY KEY , " + 
					"alignmentId  varchar(255), " + 
					"gq1Id  varchar(255), " + 
					"gq2Id  varchar(255), " + 
					"overallScore double, " +
					"p_accessionNumber  varchar(255), " + 
					"p_geneName  varchar(255), " + 
					"p_geneSymbol  varchar(255), " + 
					"p_species  varchar(255), " + 
					"p_tetrads  int, " + 
					"p_gScore  int, " + 
					"p_in5UTR boolean, " + 
					"p_inCDS boolean, " + 
					"p_in3UTR boolean, " +
					"c_accessionNumber  varchar(255), " + 
					"c_geneName  varchar(255), " + 
					"c_geneSymbol  varchar(255), " + 
					"c_species  varchar(255), " + 
					"c_tetrads  int, " + 
					"c_gScore  int, " + 
					"c_in5UTR boolean, " + 
					"c_inCDS boolean, " + 
					"c_in3UTR boolean, " + 
					"alignmentScore  double" +
					
					")";
			execute(conn, q);
			System.out.println("QGRS_H Table Created");
			
			
			q = "CREATE TABLE IF NOT EXISTS GO (" +
					"accessionNumber  varchar(255), " + 
					"goTerm  varchar(255), " + 
					"goType   varchar(255)" + 
					")";
			execute(conn, q);
			System.out.println("GO Table Created");
			
			
			
			
	
		
	}
	
	
	
	
	
	
	
	
}
