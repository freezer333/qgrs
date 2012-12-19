package qgrs.db.tasks;

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
	static void  execute(Connection conn, String q) {
		try {
			System.out.println(q);
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
			makeIndexes(conn);
		}
		finally {
			if ( conn != null ) {
				closeConnection(conn);
			}
		}	
	}
	
	public static void makeIndexes(Connection conn) {
		
		String q = "CREATE INDEX IF NOT EXISTS GENE_ID_QGRS ON QGRS (geneId)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS GENE_SPECIES ON GENE (species)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS GENE_SPECIES ON GENE (accessionNumber)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_GENEID ON QGRS (geneId)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_ID ON QGRS (id)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_GSCORE ON QGRS (gScore)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS GENE_A_PRINCIPAL ON GENE_A (principle)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS GENE_A_COMPARISON ON GENE_A (comparison)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS GENE_A_SIMPERCENT ON GENE_A (similarityPercentage)";
		execute(conn, q);
		
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_OSCORE ON QGRS_H (overallScore)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_ALIGNMENTID ON QGRS_H (alignmentId)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_GQ1 ON QGRS_H (gq1Id)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_GQ2 ON QGRS_H (gq1Id)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_ALIGNMENTSCORE ON QGRS_H (ALIGNMENTSCORE)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_PACCESSIONNUMBER ON QGRS_H (P_ACCESSIONNUMBER)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_CACCESSIONNUMBER ON QGRS_H (C_ACCESSIONNUMBER)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_PSPECIES ON QGRS_H (P_SPECIES)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_CSPECIES ON QGRS_H (C_SPECIES)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_P_GSCORE ON QGRS_H (P_GSCORE)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS QGRS_H_PTETRADS ON QGRS_H (P_TETRADS)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS GO_ACCESSIONNUMBER ON GO (ACCESSIONNUMBER)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS GO_GOTERM ON GO (GOTERM)";
		execute(conn, q);
		
		q = "CREATE INDEX IF NOT EXISTS GO_GOTYPE ON GO (GOTYPE)";
		execute(conn, q);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbIndex indexer = new DbIndex();
		indexer.buildIndexes();
	}

}
