package qgrs.compute.stat;

import java.sql.PreparedStatement;

public interface PartitionResult {

	void addBatch(PreparedStatement ps) ;
	
}
