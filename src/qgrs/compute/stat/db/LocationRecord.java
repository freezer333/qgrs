package qgrs.compute.stat.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationRecord {

	public final int id;
	public final String label;
	
	public LocationRecord(ResultSet rs) throws SQLException {
		this.id = rs.getInt("resultId");
		this.label = rs.getString("label");
	}
	
	
}
