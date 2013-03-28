package qgrs.compute.stat.qgrs.user;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.Analysis;

public class LengthPartition_5UTR extends GenePartitioner {

	public LengthPartition_5UTR(Analysis runner) {
		super(runner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<GenePartition> partition(Connection c) {
		HashSet<GenePartition> partitions = new HashSet<GenePartition>();

		partitions.add(getPartition(c, 0, 180, "Small 5'UTR"));
		partitions.add(getPartition(c, 181, 360, "Medium 5'UTR"));
		partitions.add(getPartition(c, 361, 5000, "Large 5'UTR"));

		return partitions;
	}

	private GenePartition getPartition(Connection c, int min_length, int max_length, String name) {
		String q = "SELECT * FROM GENE WHERE CDSSTART-1 >= " + min_length + " AND CDSSTART-1 <= " + max_length + " AND SPECIES = 'Homo sapiens'";

		GenePartition p = new GenePartition(this.runner, name);
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			while ( rs.next() ) {
				if (rs.getString("species").equalsIgnoreCase("Homo sapiens")){
					p.ids.add(rs.getString("accessionNumber"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}


}
