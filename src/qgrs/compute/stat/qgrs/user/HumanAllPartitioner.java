package qgrs.compute.stat.qgrs.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.GenePartitioner;

public class HumanAllPartitioner implements GenePartitioner {

	/**
	 * This doesn't really partition anything, it simply returns
	 * one single partition containing all human genes in the database.
	 * 
	 * Use this with care, since there is no parallelism, this will be 
	 * very long running.
	 */
	
	
	@Override
	public HashSet<GenePartition> partition(Connection c) {
		HashSet<GenePartition> partitions = new HashSet<GenePartition>();
		String q = "SELECT accessionNumber FROM GENE WHERE SPECIES = 'Homo sapiens'";
		GenePartition p = new GenePartition("Human Genes");
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
		partitions.add(p);
		return partitions;
	}
	
	
	private GenePartition getPartition(Connection c, int offset) {
		String q = "SELECT accessionNumber FROM GENE LIMIT 50 OFFSET " + offset;
		GenePartition p = new GenePartition("Genes " + offset + "-" + (offset+50));
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