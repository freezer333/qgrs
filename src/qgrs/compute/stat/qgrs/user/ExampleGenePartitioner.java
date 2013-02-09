package qgrs.compute.stat.qgrs.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.Analysis;

public class ExampleGenePartitioner extends GenePartitioner {

	public ExampleGenePartitioner(Analysis runner) {
		super(runner);
		// TODO Auto-generated constructor stub
	}


	/**
	 * This isn't a very useful partitioner, it simple returns 5 
	 * sets of the first 250 (50 in each set) genes.  It is
	 * meant for demonstration purposes - its doubtful meaningful
	 * statistics will be produced :)
	 * 
	 * Note, this doesn't even select out for a species...
	 */
	
	
	
	
	@Override
	public HashSet<GenePartition> partition(Connection c) {
		HashSet<GenePartition> partitons = new HashSet<GenePartition>();
		for (int i = 0; i < 250; i+= 50 ) {
			partitons.add(getPartition(c, i));
		}
		return partitons;
	}
	
	
	private GenePartition getPartition(Connection c, int offset) {
		String q = "SELECT accessionNumber FROM GENE LIMIT 50 OFFSET " + offset;
		GenePartition p = new GenePartition(this.runner, "Genes " + offset + "-" + (offset+50));
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
