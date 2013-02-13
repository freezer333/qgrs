package qgrs.compute.stat.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PartitionRecord {

	public final String partitionId;
	
	public PartitionRecord(ResultSet rs) throws SQLException {
		partitionId = rs.getString("partitionId");
	}
}
