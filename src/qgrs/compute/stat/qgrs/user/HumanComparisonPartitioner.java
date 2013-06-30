package qgrs.compute.stat.qgrs.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.Analysis;

public class HumanComparisonPartitioner extends GenePartitioner {
	private final String comparison;
	
	public HumanComparisonPartitioner(Analysis runner, String comparison) {
		super(runner);
		this.comparison = comparison;
	}


	
	@Override
	public HashSet<GenePartition> partition(Connection c) {
		HashSet<GenePartition> partitions = new HashSet<GenePartition>();
		
		String q = "Select distinct principle from GENE_A join GENE AS GC on GC.accessionNumber = comparison JOIN GENE AS GP on GP.accessionNumber = principle WHERE GP.SPECIES = 'Homo sapiens' AND GC.species = '" + comparison + "'";
		GenePartition p = new GenePartition(runner, "Human Genes with " + comparison + " Homologs");
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			
			while ( rs.next() ) {
				p.ids.add(rs.getString("principle"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		partitions.add(p);
		return partitions;
	}
	
	
	
	
	

}
