package qgrs.compute.stat.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PartitionRecord {

	public final String partitionId;
	public final String analysisId;
	public final String description;
	public final int numSamples;
	
	public PartitionRecord(ResultSet rs) throws SQLException {
		partitionId = rs.getString("partitionId");
		analysisId = rs.getString("analysisId");
		description = rs.getString("description");
		numSamples = rs.getInt("numSamples");
	}
}
