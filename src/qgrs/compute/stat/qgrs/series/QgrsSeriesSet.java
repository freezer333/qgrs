package qgrs.compute.stat.qgrs.series;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;


public class QgrsSeriesSet extends LinkedList<QgrsCriteriaSeries>{
	public void insert(Connection c, String analysisId) {
		String sql = "insert into series (analysisId, seriesId, description) values(?, ?, ?)";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			for ( QgrsCriteriaSeries q : this ) {
				System.out.println("Series insert > analysis = " + analysisId + ", series order -> " + q.getOrder());
				ps.setString(1, analysisId);
				ps.setInt(2, q.getOrder());
				ps.setString(3, q.getDescription());
				ps.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
