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
	
	
	static void executeSql(Connection conn, String sql) {
		execute(conn, sql);
	}
	static void rebuild(Connection conn, IndexCommand index) {
		executeSql(conn, index.createSql());
	}
	public static void makeIndexes(Connection conn) {
		rebuild(conn, new IndexCommand("GENE_ID_QGRS", "QGRS", "geneId"));
		rebuild(conn, new IndexCommand("GENE_SPECIES", "GENE", "species"));
		rebuild(conn, new IndexCommand("GENE_SPECIES", "GENE", "accessionNumber"));
		rebuild(conn, new IndexCommand("QGRS_GENEID", "QGRS", "geneId"));
		rebuild(conn, new IndexCommand("QGRS_ID", "QGRS", "id"));
		rebuild(conn, new IndexCommand("QGRS_GSCORE", "QGRS", "gScore"));
		rebuild(conn, new IndexCommand("GENE_A_PRINCIPAL", "GENE_A", "principle"));
		rebuild(conn, new IndexCommand("GENE_A_COMPARISON", "GENE_A", "comparison"));
		rebuild(conn, new IndexCommand("GENE_A_SIMPERCENT", "GENE_A", "similarityPercentage"));
		rebuild(conn, new IndexCommand("QGRS_H_OSCORE", "QGRS_H", "overallScore"));
		rebuild(conn, new IndexCommand("QGRS_H_ALIGNMENTID", "QGRS_H", "alignmentId"));
		rebuild(conn, new IndexCommand("QGRS_H_GQ1", "QGRS_H", "gq1Id"));
		rebuild(conn, new IndexCommand("QGRS_H_GQ2", "QGRS_H", "gq1Id"));
		rebuild(conn, new IndexCommand("QGRS_H_ALIGNMENTSCORE", "QGRS_H", "ALIGNMENTSCORE"));
		rebuild(conn, new IndexCommand("QGRS_H_PACCESSIONNUMBER", "QGRS_H", "P_ACCESSIONNUMBER"));
		rebuild(conn, new IndexCommand("QGRS_H_CACCESSIONNUMBER", "QGRS_H", "C_ACCESSIONNUMBER"));
		rebuild(conn, new IndexCommand("QGRS_H_PSPECIES", "QGRS_H", "P_SPECIES"));
		rebuild(conn, new IndexCommand("QGRS_H_CSPECIES", "QGRS_H", "C_SPECIES"));
		rebuild(conn, new IndexCommand("QGRS_H_P_GSCORE", "QGRS_H", "P_GSCORE"));
		rebuild(conn, new IndexCommand("QGRS_H_PTETRADS", "QGRS_H", "P_TETRADS"));
		rebuild(conn, new IndexCommand("GO_ACCESSIONNUMBER", "GO", "ACCESSIONNUMBER"));
		rebuild(conn, new IndexCommand("GO_GOTERM", "GO", "GOTERM"));
		rebuild(conn, new IndexCommand("GO_GOTYPE", "GO", "GOTYPE"));
		rebuild(conn, new IndexCommand("GENE_AQ_principle", "GENE_AQ", "principle"));
		rebuild(conn, new IndexCommand("GENE_AQ_comparison", "GENE_AQ", "comparison"));
		rebuild(conn, new IndexCommand("GENE_AQ_pSpecies", "GENE_AQ", "pSpecies"));
		rebuild(conn, new IndexCommand("GENE_AQ_cSpeciesE", "GENE_AQ", "cSpecies"));
		rebuild(conn, new IndexCommand("GENE_AQ_pSymbol", "GENE_AQ", "pSymbol"));
		rebuild(conn, new IndexCommand("GENE_AQ_cSymbol", "GENE_AQ", "cSymbol"));
		rebuild(conn, new IndexCommand("GENE_AQ_similarityPercentage", "GENE_AQ", "similarityPercentage"));
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbIndex indexer = new DbIndex();
		indexer.buildIndexes();
	}

}
