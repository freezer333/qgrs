package qgrs.compute.stat.qgrs.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.Analysis;

public class MultiplePolyASignalsPartitioner extends GenePartitioner {
	
	public MultiplePolyASignalsPartitioner(Analysis runner) {
		super(runner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<GenePartition> partition(Connection c) {
		HashSet<GenePartition> partitions = new HashSet<GenePartition>();

		partitions.add(getPartition(c, "Multiple Poly A Signals"));

		return partitions;
	}


	private GenePartition getPartition(Connection c, String name) {
		String q = "SELECT DISTINCT PA.ACCESSIONNUMBER FROM POLY_A_SIGNAL as PA" + 
		" JOIN POLY_A_SIGNAL as PB where PA.ACCESSIONNUMBER = PB.ACCESSIONNUMBER" +
		" and (PA.START-PB.START) > 50";
		GenePartition p = new GenePartition(this.runner, name);
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
