package qgrs.compute.stat.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeriesRecord {

	public final String analysisId;
	public final int seriesId;
	public final String description;
	
	
	public SeriesRecord(ResultSet rs) throws SQLException {
		this.seriesId = rs.getInt("seriesId");
		this.analysisId = rs.getString("analysisId");
		this.description = rs.getString("description");
	}
}
