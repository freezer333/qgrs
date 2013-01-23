package qgrs.compute.stat.qgrs.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.GenePartitioner;

public class PartitionTest1 implements GenePartitioner {

	/**
	 * Camille & Matt's First Partition Test
	 * 
	 * 
	 * Note, this doesn't even select out for a species...
	 */
	
	
	@Override
	public HashSet<GenePartition> partition(Connection c) {
		HashSet<GenePartition> partitons = new HashSet<GenePartition>();
		for (int i = 0; i < 100; i+= 20 ) {
			partitons.add(getPartition(c, i));
		}
		return partitons;
	}
	
	
	private GenePartition getPartition(Connection c, int offset) {
		String q = "SELECT accessionNumber FROM GENE LIMIT 20 OFFSET " + offset;
		GenePartition p = new GenePartition("Genes " + offset + "-" + (offset+20));
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
