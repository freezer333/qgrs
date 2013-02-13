package qgrs.compute.stat.db;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ResultRecord {
	
	public final int total;
	public final double mean;
	public final double median;
	
	public ResultRecord (ResultSet rs) throws SQLException {
		total = rs.getInt("total");
		mean = rs.getDouble("mean");
		median = rs.getDouble("median");
	}
	
}
