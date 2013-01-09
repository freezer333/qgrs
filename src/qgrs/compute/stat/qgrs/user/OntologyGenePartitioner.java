package qgrs.compute.stat.qgrs.user;

import java.sql.Connection;
import java.util.HashSet;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.GenePartitioner;

public class OntologyGenePartitioner implements GenePartitioner {

	
	
	@Override
	public HashSet<GenePartition> partition(Connection connection) {
		
		HashSet<GenePartition> partitions = new HashSet<GenePartition>();
		
		// query a list of gene functions
		
		// for each, query the list of genes which have it.  Each gene forms a partition
		
		
		
		return partitions;
		
	}

}
