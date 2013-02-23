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
	
	public final Collection<PartitionRecord> partitions;
	public final Collection<SeriesRecord> series; // curves
	public final Collection<LocationRecord> locations;  // x-axis
	public final HashMap<RecordKey, ResultRecord> results; // data points
	
	private AnalysisRecord(ResultSet rs) throws SQLException {
		this.id = rs.getString("id");
		this.description = rs.getString("description");
		this.active = rs.getBoolean("active");
		this.date = rs.getDate("date");
		
		this.partitions = new LinkedList<PartitionRecord>();
		this.series = new LinkedList<SeriesRecord>();
		this.locations = new LinkedList<LocationRecord>();
		this.results = new HashMap<RecordKey, ResultRecord>();
	}
	
	private PartitionRecord getParitionRecord(String id) {
		for ( PartitionRecord p : this.partitions ) {
			if ( p.partitionId.equalsIgnoreCase(id)) {
				return p;
			}
		}
		return null;
	}
	
	private SeriesRecord getSeriesRecord(int id) {
		for ( SeriesRecord s : this.series ) {
			if ( s.seriesId == id ) {
				return s;
			}
		}
		return null;
	}
	
	private LocationRecord getLocationRecord(int id) {
		for ( LocationRecord loc : this.locations ) {
			if ( loc.id == id ) {
				return loc;
			}
		}
		return null;
	}
	
	private void loadSeriesRecords(Connection c) throws SQLException{
		String sql = "select * from series where analysisId = '" + this.id + "'";
		Statement stmt = c.createStatement();
		ResultSet rs =  stmt.executeQuery(sql);
		while ( rs.next() ) {
			SeriesRecord s = new SeriesRecord(rs);
			this.series.add(s);
		}
		stmt.close();
	}
	
	private void loadPartitionRecords(Connection c) throws SQLException{
		String sql = "select * from partition where analysisId = '" + this.id + "'";
		Statement stmt = c.createStatement();
		ResultSet rs =  stmt.executeQuery(sql);
		while ( rs.next() ) {
			PartitionRecord s = new PartitionRecord(rs);
			this.partitions.add(s);
		}
		stmt.close();
	}
	private void loadLocationRecords(Connection c) throws SQLException{
		String sql = "select distinct resultId, label from results where analysisId = '" + this.id + "'";
		Statement stmt = c.createStatement();
		ResultSet rs =  stmt.executeQuery(sql);
		while ( rs.next() ) {
			LocationRecord s = new LocationRecord(rs);
			this.locations.add(s);
		}
		stmt.close();
	}
	
	private void loadResultRecords(Connection c) throws SQLException {
		String sql = "select * from results where analysisId = '" + this.id + "'";
		Statement stmt = c.createStatement();
		ResultSet rs =  stmt.executeQuery(sql);
		while ( rs.next() ) {
			String partitionId = rs.getString("partitionId");
			int seriesId = rs.getInt("seriesId");
			int locationId = rs.getInt("resultId");
			
			RecordKey key = new RecordKey(this.getParitionRecord(partitionId), this.getSeriesRecord(seriesId), this.getLocationRecord(locationId));
			ResultRecord record = new ResultRecord(rs);
			this.results.put(key, record);
		}
		stmt.close();
	}
	
	
	public static AnalysisRecord loadAnalysis(String analysisId, Connection c) throws SQLException {
		AnalysisRecord a  = null;
		String sql = "SELECT * FROM ANALYSIS where id = '" + analysisId + "'";
		Statement stmt = c.createStatement();
		ResultSet rs =  stmt.executeQuery(sql);
		if ( rs.next() ) {
			a = new AnalysisRecord(rs);
		}
		else {
			return null;
		}
		stmt.close();
		a.loadSeriesRecords(c);
		a.loadPartitionRecords(c);
		a.loadLocationRecords(c);
		a.loadResultRecords(c);
		return a;
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
