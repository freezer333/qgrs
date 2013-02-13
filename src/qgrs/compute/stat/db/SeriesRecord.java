package qgrs.compute.stat.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeriesRecord {

	
	public final int seriesId;
	
	public SeriesRecord(ResultSet rs) throws SQLException {
		this.seriesId = rs.getInt("seriesId");
	}
}
