package qgrs.compute.stat.qgrs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import qgrs.compute.stat.PartitionResultRecorder;

public class QgrsPartitionResultRecorder extends
		PartitionResultRecorder {

	public QgrsPartitionResultRecorder(String tableName) {
		super(tableName);
	}
	
	@Override
	public PreparedStatement buildPreparedStatementForBatch(Connection conn) throws Exception{
		String iSql = 	"INSERT INTO "+this.tableName+" (label, samples, " +
						"qgrsTotalAll, qgrsMeanAll, qgrsMedianAll, " +
						"qgrsTotal5Prime, qgrsMean5Prime, qgrsMedian5Prime, " +
						"qgrsTotalCds, qgrsMeanCds, qgrsMedianCds, " +
						"qgrsTotal3Prime, qgrsMean3Prime, qgrsMedian3Prime, " + 
						"qgrsTotalCDS80, qgrsMeanCDS80, qgrsMedianCDS80) " + 
						"VALUES (?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return conn.prepareStatement(iSql);
	}

	@Override
	public void createResultsTable(Connection con) throws Exception{
		dropTable(con);
		
		String tSql = "CREATE TABLE IF NOT EXISTS " + tableName +" (" +
				"label varchar(255) PRIMARY KEY , " +
				"samples  int, " + 
				"qgrsTotalAll  double, " + 
				"qgrsMeanAll  double, " + 
				"qgrsMedianAll  double, " + 
				"qgrsTotal5Prime double, " +
				"qgrsMean5Prime  double, " + 
				"qgrsMedian5Prime  double, " + 
				"qgrsTotalCds double, " +
				"qgrsMeanCds  double, " + 
				"qgrsMedianCds  double, " + 
				"qgrsTotal3Prime double, " +
				"qgrsMean3Prime  double, " + 
				"qgrsMedian3Prime  double," +
				"qgrsTotalCDS80 double, " +
				"qgrsMeanCDS80  double, " + 
				"qgrsMedianCDS80  double" +
				")";
		
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(tSql);
		}
		finally {
			if ( stmt != null ) stmt.close();
		}
		
	}
}
