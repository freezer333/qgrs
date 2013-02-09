package qgrs.compute.stat.qgrs.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.Analysis;

public class FunctionGenePartitioner extends GenePartitioner {

	public FunctionGenePartitioner(Analysis runner) {
		super(runner);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Note that this is somewhat limited, it doesn't consider species.
	 * 
	 * A good first attempt, but it should be enhanced to only
	 * include accession number corresponding to human genes.
	 * 
	 * This could be done by altering the code in get partition to use a 
	 * join against the GENE table and selecting only homosapien as species.
	 * 
	 */
	
	@Override
	public HashSet<GenePartition> partition(Connection connection) {
		
		HashSet<GenePartition> partitions = new HashSet<GenePartition>();
		
		Collection<String> functionList = getFunctions(connection);
		
		for (String function : functionList) {
			partitions.add(getPartition(connection, function));
		}
		return partitions;
	}
	
	Collection<String> getFunctions(Connection c) {
		Collection<String> list = new LinkedList<String>();
		String q = "SELECT DISTINCT GOTERM FROM GO WHERE GOTYPE='Function'";
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			while ( rs.next() ) {
				list.add(rs.getString("GOTERM"));
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	private GenePartition getPartition(Connection c, String ontologyTerm) {
		
		// note that the ontology terms is escaped, since many ontology terms have ' in them.
		String q = "SELECT accessionNumber FROM GO WHERE GOTERM='" + ontologyTerm.replace("'", "''") + "'";
		GenePartition p = new GenePartition(this.runner, ontologyTerm);
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			while ( rs.next() ) {
				p.ids.add(rs.getString("accessionNumber"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}

}
