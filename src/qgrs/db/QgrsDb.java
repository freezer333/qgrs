package qgrs.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import qgrs.compute.BuildKey;
import qgrs.data.GQuadruplex;
import qgrs.data.GQuadruplexRecord;
import qgrs.data.GeneSequence;
import qgrs.data.query.QgrsQuery;
import framework.db.QueryConstraint;
import framework.db.QueryConstraints;
import framework.db.StatementBuilder;

public class QgrsDb  extends DbTable {
	private static String TABLE = "QGRS";
	
	DatabaseConnection dc;
	final PreparedStatement insertStatement;
	final PreparedStatement selectByGeneStatement;
	final PreparedStatement selectByIdStatement;
	
	public QgrsDb(DatabaseConnection dc) {
		this.dc = dc;
		
		this.insertStatement = createInsertStatement();
		this.selectByGeneStatement = createSelectByGeneStatement();
		this.selectByIdStatement = createSelectByIdStatement();
	}
	
	public void close() {
		try {
			this.insertStatement.close();
			this.selectByGeneStatement.close();
			this.selectByIdStatement.close();
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public void put(GQuadruplexRecord gq) {
		
		PreparedStatement ps = insertStatement;
		
		
		fillPreparedStatement(ps, gq);
		this.dc.executeUpdate(ps);
		
		
		
	}
	
	public GQuadruplexRecord get(String id) {
		try {
			this.selectByIdStatement.setString(1, id);
			ResultSet rs = this.selectByIdStatement.executeQuery();
			if ( rs.next()) {
				return new GQuadruplexRecord(rs);
			}
			else {
				return null;
			}
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public boolean has(GeneSequence s, String buildKey) {
		try {
			this.selectByGeneStatement.setString(1, s.getAccessionNumber());
			this.selectByGeneStatement.setString(2, buildKey);
			ResultSet rs = this.selectByGeneStatement.executeQuery();
			return rs.next();
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public List<GQuadruplexRecord> getAllRecords(GeneSequence s, String buildKey) {
		try {
			this.selectByGeneStatement.setString(1, s.getAccessionNumber());
			this.selectByGeneStatement.setString(2, buildKey);
			ResultSet rs = this.selectByGeneStatement.executeQuery();
			List<GQuadruplexRecord> retval = new LinkedList<GQuadruplexRecord>();
			while ( rs.next()) {
				
				retval.add(new GQuadruplexRecord(rs));
			}
			return retval;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public HashMap<String, GQuadruplexRecord> getIn(Collection<String> qgrsIds) {
		try {
			HashMap<String, GQuadruplexRecord> retval = new HashMap<String, GQuadruplexRecord>();
			if ( qgrsIds == null || qgrsIds.size()  < 1 ) {
				return retval;
			}
			String query = "SELECT * FROM QGRS WHERE id IN(" + getCSL(qgrsIds)+ ")";
			//System.out.println("QUERY DEBUG:  " + query);
			//long start = System.nanoTime();
			PreparedStatement ps = dc.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while ( rs.next()) {
				retval.put(rs.getString("id"), new GQuadruplexRecord(rs));
			}
			//double  elapsed = System.nanoTime() - start;
			//System.out.println("QUERY TIME:  " + new DecimalFormat("0.000").format(elapsed /1000000000) + " sec");
			return retval;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	
	public List<GQuadruplexRecord> getRecords(QgrsQuery where, int limit, int offset) {
		try {
			String query = "SELECT * FROM QGRS " + where.toSql() + " ORDER BY id LIMIT " + limit + " OFFSET " + offset;
			//System.out.println("QUERY DEBUG:  " + query);
			//long start = System.nanoTime();
			PreparedStatement ps = dc.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			List<GQuadruplexRecord> retval = new LinkedList<GQuadruplexRecord>();
			while ( rs.next()) {
				
				retval.add(new GQuadruplexRecord(rs));
			}
			//double  elapsed = System.nanoTime() - start;
			//System.out.println("QUERY TIME:  " + new DecimalFormat("0.000").format(elapsed /1000000000) + " sec");
			return retval;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	public int getRecordCount(QgrsQuery where) {
		try {
			String query = "SELECT COUNT(id) as total FROM QGRS " + where.toSql();
			//System.out.println("QUERY DEBUG:  " + query);
			//long start = System.nanoTime();
			PreparedStatement ps = dc.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			//double  elapsed = System.nanoTime() - start;
			//System.out.println("QUERY TIME:  " + new DecimalFormat("0.000").format(elapsed /1000000000) + " sec");
			if ( rs.next()) {
				return rs.getInt("total");
			}
			
			return 0;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	public List<GQuadruplex> getAll(GeneSequence s, String buildKey) {
		try {
			this.selectByGeneStatement.setString(1, s.getAccessionNumber());
			this.selectByGeneStatement.setString(2, buildKey);
			ResultSet rs = this.selectByGeneStatement.executeQuery();
			List<GQuadruplex> retval = new LinkedList<GQuadruplex>();
			while ( rs.next()) {
				GQuadruplexRecord r = new GQuadruplexRecord (rs);
				retval.add(new GQuadruplex(r, s));
			}
			return retval;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	private PreparedStatement createInsertStatement() {
		StatementBuilder builder = buildBuilder();
		return this.buildInsert(builder);
	}
	
	private PreparedStatement createSelectByIdStatement() {
		StatementBuilder builder = new StatementBuilder(TABLE);
		return buildSelectByIdStatement(builder);
	}
	private PreparedStatement createSelectByGeneStatement() {
		StatementBuilder builder = new StatementBuilder(TABLE);
		return buildSelectByGeneStatement(builder);
	}
	private PreparedStatement buildSelectByIdStatement(StatementBuilder builder) {
		QueryConstraints qc = new QueryConstraints();
		qc.add(new QueryConstraint("id", ""));
		try {
			return dc.getConnection().prepareStatement(builder.buildSelectStatement(qc));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	private PreparedStatement buildSelectByGeneStatement(StatementBuilder builder) {
		QueryConstraints qc = new QueryConstraints();
		qc.add(new QueryConstraint("geneId", ""));
		qc.add(new QueryConstraint("buildKey", ""));
		try {
			return dc.getConnection().prepareStatement(builder.buildSelectStatement(qc));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	private PreparedStatement buildInsert(StatementBuilder builder) {
		try {
			return this.dc.getConnection().prepareStatement(builder.buildInsertStatement());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private StatementBuilder buildBuilder() {
		StatementBuilder builder = new StatementBuilder(TABLE);
		builder.add("id");
		builder.add("geneId");
		builder.add("sequenceSlice");
		builder.add("tetrad1");
		builder.add("tetrad2");
		builder.add("tetrad3");
		builder.add("tetrad4");
		builder.add("loop1Length");
		builder.add("loop2Length");
		builder.add("loop3Length");
		builder.add("totalLength");
		builder.add("gScore");
		builder.add("numTetrads");
		builder.add("buildKey");
		builder.add("in5Prime");
		builder.add("inCds");
		builder.add("in3Prime");
		builder.add("distanceFromPolyASignal");
		return builder;
	}
	
	private void fillPreparedStatement(PreparedStatement ps, GQuadruplexRecord gq) {
		try {
			ps.setString(1, gq.getId());
			ps.setString(2, gq.getGeneAccessionNumber());
			ps.setString(3, gq.getSequenceSlice());
			ps.setInt(4, gq.getTetrad1());
			ps.setInt(5, gq.getTetrad2());
			ps.setInt(6, gq.getTetrad3());
			ps.setInt(7, gq.getTetrad4());
			ps.setInt(8, gq.getLoop1Length());
			ps.setInt(9, gq.getLoop2Length());
			ps.setInt(10, gq.getLoop3Length());
			ps.setInt(11,gq.getTotalLength());
			ps.setInt(12, gq.getScore());
			ps.setInt(13, gq.getNumTetrads());
			ps.setString(14, BuildKey.QgrsIdentify);
			ps.setBoolean(15, gq.isIn5Prime());
			ps.setBoolean(16, gq.isInCds());
			ps.setBoolean(17, gq.isIn3Prime());
			ps.setInt(18, gq.getDistanceFromPolyASignal());
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
