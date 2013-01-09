package qgrs.compute.stat;

import java.sql.Connection;
import java.util.HashSet;

public interface GenePartitioner {

	
	HashSet<GenePartition> partition(Connection c);
}
