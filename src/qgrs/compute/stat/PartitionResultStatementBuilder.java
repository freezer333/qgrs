package qgrs.compute.stat;

import java.sql.Connection;
import java.sql.PreparedStatement;

public interface PartitionResultStatementBuilder {

	public abstract PreparedStatement buildPreparedStatementForBatch(
			Connection conn);

}