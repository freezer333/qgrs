package qgrs.compute.stat.qgrs.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.Analysis;

public class OntologyGenePartitioner extends GenePartitioner {

	public OntologyGenePartitioner(Analysis runner) {
		super(runner);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public HashSet<GenePartition> partition(Connection c) {
		HashSet<GenePartition> partitons = new HashSet<GenePartition>();
		for (int i = 0; i < 250; i+= 50 ) {
			partitons.add(getPartition(c, i));
		}
		return partitons;
	}
	
	//Transcription Factor Binding
	private GenePartition getPartition(Connection c, int offset) {
		String q = "SELECT ACCESSIONNUMBER FROM GO WHERE GOTERM='transcription factor binding' LIMIT 50 OFFSET " + offset;
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
