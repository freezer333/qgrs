package qgrs.compute.stat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import qgrs.db.ReservedTableNames;

public abstract class PartitionResultRecorder {

	public abstract PreparedStatement buildPreparedStatementForBatch(Connection conn) throws Exception;
	public abstract void createResultsTable(Connection con)  throws Exception;
	
	public final String tableName;
	
	
	public PartitionResultRecorder(String tableName) {
		for ( ReservedTableNames table : ReservedTableNames.values() ) {
			if ( table.toString().equalsIgnoreCase(tableName)) {
				throw new RuntimeException ("Cannot create PartitionResultRecorder with target table of " + tableName + " - the table name is reserved!");
			}
		}
		this.tableName = tableName;
	}
	
	// Most implementation of PartitionResult should drop the existing table when asked
	// to create a results table.
	protected void dropTable(Connection con) {
		String q = "DROP TABLE " + tableName;
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(q);
			
		} catch (SQLException e) {
		}
		finally {
			if ( stmt != null ) {
				try {
					stmt.close();
				}
				catch (Exception e){}
			}
		}
	}
}