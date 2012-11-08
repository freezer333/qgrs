package qgrs.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import qgrs.data.AlignmentRecord;
import qgrs.data.GeneSequence;
import qgrs.data.query.AlignmentQuery;
import framework.db.QueryConstraint;
import framework.db.QueryConstraints;
import framework.db.StatementBuilder;

public class AlignmentRecordDb extends DbTable  {
	private static String TABLE = "GENE_A";
	
	DatabaseConnection dc;
	final PreparedStatement insertStatement;
	final PreparedStatement selectStatement;
	final PreparedStatement selectByIdStatement;
	final PreparedStatement selectWithBuildKeyStatement;
	final PreparedStatement selectAllStatement;
	
	public AlignmentRecordDb(DatabaseConnection dc) {
		this.dc = dc;
		
		this.insertStatement = createInsertStatement();
		this.selectStatement = createSelectStatement();
		this.selectAllStatement = createSelectAllStatement();
		this.selectWithBuildKeyStatement = createSelectWithBuildKeyStatement();
		this.selectByIdStatement = createSelectByIdStatement();
	}
	
	public void close() {
		try {
			this.insertStatement.close();
			this.selectStatement.close();
			this.selectAllStatement.close();
			this.selectWithBuildKeyStatement.close();
			this.selectByIdStatement.close();
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	
	public void put(AlignmentRecord ar) {
		PreparedStatement ps = null;
		if (has(ar.getPrinciple(), ar.getComparison(), ar.getAlignmentBuildKey())) {
			return;
		}
		else {
			ps = insertStatement;
		}
		
		fillPreparedStatement(ps, ar);
		this.dc.executeUpdate(ps);
			
	}
	
	
	
	public AlignmentRecord get(GeneSequence principle, GeneSequence comparison) {
		try {
			this.selectStatement.setString(1, principle.getAccessionNumber());
			this.selectStatement.setString(2, comparison.getAccessionNumber());
			ResultSet rs = this.selectStatement.executeQuery();
			if ( rs.next()) {
				return new AlignmentRecord(rs);
			}
			else {
				return null;
			}
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	public AlignmentRecord get(String id) {
		try {
			this.selectByIdStatement.setString(1, id);
			ResultSet rs = this.selectByIdStatement.executeQuery();
			if ( rs.next()) {
				return new AlignmentRecord(rs);
			}
			else {
				return null;
			}
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	public List<AlignmentRecord> get(AlignmentQuery q) {
		try {
			List<AlignmentRecord> retval = new LinkedList<AlignmentRecord>();
			String query = "SELECT * FROM GENE_A " + q.toSql();
			//System.out.println("QUERY DEBUG:  " + query);
			//long start = System.nanoTime();
			PreparedStatement ps = dc.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while ( rs.next()) {
				retval.add(new AlignmentRecord(rs));
			}
			//double  elapsed = System.nanoTime() - start;
			//System.out.println("QUERY TIME:  " + new DecimalFormat("0.000").format(elapsed /1000000000) + " sec");
			return retval;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	public AlignmentRecord get(GeneSequence principle, GeneSequence comparison, String alignmentBuildKey) {
		try {
			this.selectWithBuildKeyStatement.setString(1, principle.getAccessionNumber());
			this.selectWithBuildKeyStatement.setString(2, comparison.getAccessionNumber());
			this.selectWithBuildKeyStatement.setString(3, alignmentBuildKey);
			ResultSet rs = this.selectWithBuildKeyStatement.executeQuery();
			if ( rs.next()) {
				return new AlignmentRecord(rs);
			}
			else {
				return null;
			}
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	public List<AlignmentRecord> getAll() {
		try {
			List<AlignmentRecord> retval = new LinkedList<AlignmentRecord>();
			ResultSet rs = this.selectAllStatement.executeQuery();
			while ( rs.next()) {
				retval.add(new AlignmentRecord(rs));
			}
			return retval;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	public int getCount() {
		try {
			Statement st = this.dc.getConnection().createStatement();
			long start = System.nanoTime();
			ResultSet res = st.executeQuery("SELECT COUNT(principle) FROM "+TABLE);
			double  elapsed = System.nanoTime() - start;
			System.out.println("QUERY TIME:  " + new DecimalFormat("0.000").format(elapsed /1000000000) + " sec");
			
			if ( res.next() ) {
				return res.getInt(1);
			}
			else {
				return 0;
			}
			
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	
	
	public boolean has(String principle, String comparison, String buildKey) {
		try {
			this.selectWithBuildKeyStatement.setString(1, principle);
			this.selectWithBuildKeyStatement.setString(2, comparison);
			this.selectWithBuildKeyStatement.setString(3, buildKey);
			ResultSet rs = this.selectWithBuildKeyStatement.executeQuery();
			return rs.next();
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public boolean has(String principle, String comparison) {
		try {
			this.selectStatement.setString(1, principle);
			this.selectStatement.setString(2, comparison);
			ResultSet rs = this.selectStatement.executeQuery();
			return rs.next();
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	
	private PreparedStatement createInsertStatement() {
		StatementBuilder builder = buildBuilder();
		return this.buildInsert(builder);
	}
	
	
	
	
	private PreparedStatement buildInsert(StatementBuilder builder) {
		try {
			return this.dc.getConnection().prepareStatement(builder.buildInsertStatement());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private PreparedStatement createSelectStatement() {
		StatementBuilder builder = new StatementBuilder(TABLE);
		return buildSelectStatement(builder);
	}
	
	private PreparedStatement createSelectWithBuildKeyStatement() {
		StatementBuilder builder = new StatementBuilder(TABLE);
		return buildSelectWithBuildKeyStatement(builder);
	}
	private PreparedStatement createSelectByIdStatement() {
		StatementBuilder builder = new StatementBuilder(TABLE);
		return buildSelectByIdStatement(builder);
	}
	
	private PreparedStatement createSelectAllStatement() {
		StatementBuilder builder = new StatementBuilder(TABLE);
		try {
			return dc.getConnection().prepareStatement(builder.buildSelectStatement(new QueryConstraints()));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private PreparedStatement buildSelectStatement(StatementBuilder builder) {
		QueryConstraints qc = new QueryConstraints();
		qc.add(new QueryConstraint("principle", ""));
		qc.add(new QueryConstraint("comparison", ""));
		try {
			return dc.getConnection().prepareStatement(builder.buildSelectStatement(qc));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	private PreparedStatement buildSelectWithBuildKeyStatement(StatementBuilder builder) {
		QueryConstraints qc = new QueryConstraints();
		qc.add(new QueryConstraint("principle", ""));
		qc.add(new QueryConstraint("comparison", ""));
		qc.add(new QueryConstraint("alignmentBuildKey", ""));
		try {
			return dc.getConnection().prepareStatement(builder.buildSelectStatement(qc));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
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
	
	
	
	
	private StatementBuilder buildBuilder() {
		StatementBuilder builder = new StatementBuilder(TABLE);
		builder.add("id");
		builder.add("alignmentBuildKey");
		builder.add("principle");
		builder.add("comparison");
		builder.add("similarityScore");
		builder.add("similarityPercentage");
		builder.add("dateAligned");
		return builder;
	}
	
	
	
	
	private void fillPreparedStatement(PreparedStatement ps, AlignmentRecord ar) {
		try {
			ps.setString(1, ar.getId());
			ps.setString(2, ar.getAlignmentBuildKey());
			ps.setString(3, ar.getPrinciple());
			ps.setString(4, ar.getComparison());
			ps.setInt(5, ar.getSimilarityScore());
			ps.setDouble(6, ar.getSimilarityPercentage());
			ps.setDate(7, new java.sql.Date(ar.getDateAligned().getTime()));
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}
