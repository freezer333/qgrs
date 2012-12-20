package qgrs.db.tasks;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

import qgrs.db.AppProperties;
import framework.db.DatabaseConnectionParameters;

public class BuildGeneAQTable extends AbstractDbTask{

	DatabaseConnectionParameters params ;
	
	class GeneInfo {
		final String accessionNumber;
		final String species;
		final String symbol;
		
		public GeneInfo(String accessionNumber, String species, String symbol) {
			super();
			this.accessionNumber = accessionNumber;
			this.species = species;
			this.symbol = symbol;
			
		}
	}
	
	class GeneAlignment {
		final String id;
		final GeneInfo principle;
		final GeneInfo comparison;
		final double alignment;
		public GeneAlignment(GeneInfo principle, GeneInfo symbol,
				double alignment) {
			super();
			this.id = principle.accessionNumber + "x" + symbol.accessionNumber;
			this.principle = principle;
			this.comparison = symbol;
			this.alignment = alignment;
		}
		
		
	}
	
	
	HashMap<String, GeneInfo> genes = new HashMap<String, GeneInfo>();
	HashSet<GeneAlignment> alignments = new HashSet<GeneAlignment>();
	
	public BuildGeneAQTable () {
		params = new DatabaseConnectionParameters(AppProperties.getConnectionStringFromPropsxml(), "sa", "sa");
		
	}
	
	public void createTable() {
		String q = "CREATE TABLE IF NOT EXISTS GENE_AQ (" +
				"id varchar(255), " +
				"principle varchar(255), " +
				"comparison varchar(255), " +
				"pSpecies varchar(255), " +
				"cSpecies varchar(255), " +
				"pSymbol varchar(255), " +
				"cSymbol varchar(255), " +
				"similarityPercentage double, " +
				")";
		executeUpdate(q);
		System.out.println("GENE_AQ Table Created");
	
	}
	
	public void execute() throws Exception{
		
		dropTable(conn, "GENE_AQ");
		buildGenemap();
		System.out.println("Loaded " + genes.size() + " genes");
		buildAlignmentMap();
		System.out.println("Loaded " + alignments.size() + " alignments");
		createTable();
		insertAlignments();
		System.out.println("Alignments inserted successfully.");
	}

	private void insertAlignments() throws SQLException {
		PreparedStatement ps = this.conn.prepareStatement(
				"INSERT INTO GENE_AQ (id,principle,comparison,pSpecies, cSpecies,pSymbol,cSymbol, similarityPercentage) values (?, ?, ?, ?, ?, ?, ?, ?)");
		
		
		// Insert all genea into GENE_AQ
		for ( GeneAlignment a : this.alignments ) {
			 ps.setString(1, a.id);
			 ps.setString(2, a.principle.accessionNumber);
			 ps.setString(3, a.comparison.accessionNumber);
			 ps.setString(4, a.principle.species);
			 ps.setString(5, a.comparison.species);
			 ps.setString(6, a.principle.symbol);
			 ps.setString(7, a.comparison.symbol);
			 ps.setDouble(8, a.alignment);
			 ps.addBatch();
		}
		ps.executeBatch();
		ps.close();
	}

	private void buildGenemap() throws SQLException {
		String sql = "select * from gene";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while ( rs.next() ) {
			this.genes.put(rs.getString("accessionnumber"), 
					new GeneInfo(rs.getString("accessionnumber"), rs.getString("species"), rs.getString("genesymbol")));
		}
		
		stmt.close();
	}

	private void buildAlignmentMap() throws SQLException {
		String sql;
		Statement stmt;
		ResultSet rs;
		// Select everything out of GENE_A and build map of genea
		sql = "select ID, PRINCIPLE, COMPARISON, SIMILARITYPERCENTAGE from gene_A";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		while ( rs.next() ) {
			alignments.add( new GeneAlignment(
							genes.get(rs.getString("principle")), 
							genes.get(rs.getString("comparison")), 
							rs.getDouble("similaritypercentage"))
					);
		}
		stmt.close();
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new BuildGeneAQTable().execute();

	}


	@Override
	public void report() {
		// TODO Auto-generated method stub
		
	}

}
