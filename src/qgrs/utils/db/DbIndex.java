package qgrs.utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import qgrs.db.AppProperties;
import framework.db.DatabaseConnectionParameters;

public class DbIndex {

	DatabaseConnectionParameters params ;
	
	public DbIndex () {
		params = new DatabaseConnectionParameters(AppProperties.getConnectionStringFromPropsxml(), "sa", "sa");
		
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
	
	
	public void buildIndexes() {
		Connection conn = null;
		try {
			conn = getConnection();
			makeIndex_Set1(conn);
		}
		finally {
			if ( conn != null ) {
				closeConnection(conn);
			}
		}	
	}
	
	void makeIndex_Set1(Connection conn) {
		
		System.out.println("Creating GENE_ID_QGRS index...");
		String q = "CREATE INDEX IF NOT EXISTS GENE_ID_QGRS ON QGRS (geneId)";
		execute(conn, q);
		System.out.println("Index GENE_ID_QGRS complete");
		
		System.out.println("Creating GENE_SPECIES index...");
		q = "CREATE INDEX IF NOT EXISTS GENE_SPECIES ON GENE (species)";
		execute(conn, q);
		System.out.println("Index GENE_SPECIES complete");
		
		System.out.println("Creating GENE_ACCESSION_NUMBER index...");
		q = "CREATE INDEX IF NOT EXISTS GENE_SPECIES ON GENE (accessionNumber)";
		execute(conn, q);
		System.out.println("Index GENE_ACCESSION_NUMBER complete");
		
		System.out.println("Creating QGRS_GENEID index...");
		q = "CREATE INDEX IF NOT EXISTS QGRS_GENEID ON QGRS (geneId)";
		execute(conn, q);
		System.out.println("Index QGRS_GENEID complete");
		
		System.out.println("Creating QGRS_ID index...");
		q = "CREATE INDEX IF NOT EXISTS QGRS_ID ON QGRS (id)";
		execute(conn, q);
		System.out.println("Index QGRS_ID complete");
		
		System.out.println("Creating QGRS_GSCORE index...");
		q = "CREATE INDEX IF NOT EXISTS QGRS_GSCORE ON QGRS (gScore)";
		execute(conn, q);
		System.out.println("Index QGRS_GSCORE complete");
		
		System.out.println("Creating GENE_A_PRINCIPAL index...");
		q = "CREATE INDEX IF NOT EXISTS GENE_A_PRINCIPAL ON GENE_A (principle)";
		execute(conn, q);
		System.out.println("Index GENE_A_PRINCIPAL complete");
		
		System.out.println("Creating GENE_A_SIMPERCENT index...");
		q = "CREATE INDEX IF NOT EXISTS GENE_A_SIMPERCENT ON GENE_A (similarityPercentage)";
		execute(conn, q);
		System.out.println("Index GENE_A_SIMPERCENT complete");
		
		
		
		System.out.println("Creating QGRS_H_OSCORE index...");
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_OSCORE ON QGRS_H (overallScore)";
		execute(conn, q);
		System.out.println("Index QGRS_H_OSCORE complete");
		
		System.out.println("Creating QGRS_H_ALIGNMENTID index...");
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_ALIGNMENTID ON QGRS_H (alignmentId)";
		execute(conn, q);
		System.out.println("Index QGRS_H_ALIGNMENTID complete");
		
		System.out.println("Creating QGRS_H_GQ1 index...");
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_GQ1 ON QGRS_H (gq1Id)";
		execute(conn, q);
		System.out.println("Index QGRS_H_GQ1 complete");
		
		System.out.println("Creating QGRS_H_GQ2 index...");
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_GQ2 ON QGRS_H (gq1Id)";
		execute(conn, q);
		System.out.println("Index QGRS_H_GQ2 complete");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbIndex indexer = new DbIndex();
		indexer.buildIndexes();
	}

}
