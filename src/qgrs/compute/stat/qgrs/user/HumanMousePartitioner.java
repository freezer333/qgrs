package qgrs.compute.stat.qgrs.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.Analysis;

public class HumanMousePartitioner extends GenePartitioner {

	public HumanMousePartitioner(Analysis runner) {
		super(runner);
	}


	
	@Override
	public HashSet<GenePartition> partition(Connection c) {
		HashSet<GenePartition> partitions = new HashSet<GenePartition>();
		
		String q = "Select distinct principle from GENE_A join GENE AS GC on GC.accessionNumber = comparison JOIN GENE AS GP on GP.accessionNumber = principle WHERE GP.SPECIES = 'Homo sapiens' AND GC.species = 'Mus musculus'";
		GenePartition p = new GenePartition(runner, "Human Genes with Mouse Homologs");
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
