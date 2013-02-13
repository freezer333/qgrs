package qgrs.compute.stat.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class AnalysisRecord {

	public final String id;
	public final String description;
	public final boolean active;
	public final Date date;
	
	Collection<PartitionRecord> partitions;
	Collection<SeriesRecord> series; // curves
	Collection<LocationRecord> locations;  // x-axis
	HashMap<RecordKey, ResultRecord> results; // data points
	
	private AnalysisRecord(ResultSet rs) throws SQLException {
		this.id = rs.getString("id");
		this.description = rs.getString("description");
		this.active = rs.getBoolean("active");
		this.date = rs.getDate("date");
	}
	
	static AnalysisRecord loadAnalysis(String analysisId, Connection c) {
		return null;
	}
	
	public static Collection<AnalysisRecord> getAllAnalysis_shallow(Connection c) throws SQLException{
		LinkedList<AnalysisRecord> ids = new LinkedList<AnalysisRecord>();
		String sql = "SELECT * FROM ANALYSIS";
		Statement stmt = c.createStatement();
		ResultSet rs =  stmt.executeQuery(sql);
		while ( rs.next() ) {
			ids.add(new AnalysisRecord(rs));
		}
		stmt.close();
		return ids;
	}
	
	
	
	
}
