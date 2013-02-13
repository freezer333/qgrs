package qgrs.compute.stat.qgrs.series;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;


public class QgrsSeriesSet extends LinkedList<QgrsCriteriaSeries>{
	public void insert(Connection c, String analysisId, String partitionId) {
		String sql = "insert into series (analysisId, partitionId, seriesId, description) values(?, ?, ?, ?)";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			for ( QgrsCriteriaSeries q : this ) {
				System.out.println("Series insert > analysis = " + analysisId + ", partition = " + partitionId + " series order -> " + q.getOrder());
				ps.setString(1, analysisId);
				ps.setString(2, partitionId);
				ps.setInt(3, q.getOrder());
				ps.setString(4, q.getDescription());
				ps.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
