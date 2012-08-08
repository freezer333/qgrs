package qgrs.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Element;

import qgrs.data.QgrsHomologyRecord;
import qgrs.data.query.HomologyQuery;
import framework.db.QueryConstraint;
import framework.db.QueryConstraints;
import framework.db.StatementBuilder;

public class HomologyRecordDb  extends DbTable {
	private static String TABLE = "QGRS_H";
	
	DatabaseConnection dc;
	final PreparedStatement insertStatement;
	final PreparedStatement selectByAlignmentIdStatement;
	
	public HomologyRecordDb(DatabaseConnection dc) {
		this.dc = dc;
		
		this.insertStatement = createInsertStatement();
		this.selectByAlignmentIdStatement = createSelectByAlignmentIdStatement();
	}
	
	public void close() {
		try {
			this.insertStatement.close();
			this.selectByAlignmentIdStatement.close();
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public void put(QgrsHomologyRecord record) {
		PreparedStatement ps = insertStatement;
		
		
		fillPreparedStatement(ps, record);
		this.dc.executeUpdate(ps);
		
		try {
			ResultSet gk = ps.getGeneratedKeys();
			if ( gk.next() ) {
				record.setId(gk.getInt(1));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	
	public List<QgrsHomologyRecord> get(String alignmentId) {
		try {
			this.selectByAlignmentIdStatement.setString(1, alignmentId);
			ResultSet rs = this.selectByAlignmentIdStatement.executeQuery();
			List<QgrsHomologyRecord> records = new LinkedList<QgrsHomologyRecord>();
			while ( rs.next()) {
				records.add(new QgrsHomologyRecord(rs));
			}
			return records;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public List<QgrsHomologyRecord> get(HomologyQuery where, int limit, int offset) {
		try {
			String query = "SELECT * FROM QGRS_H " + where.toSql() + " ORDER BY id LIMIT " + limit + " OFFSET " + offset;
			System.out.println("Optomization Candidate:\nQUERY DEBUG:  " + query);
			long start = System.nanoTime();
			PreparedStatement ps = dc.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			List<QgrsHomologyRecord> records = new LinkedList<QgrsHomologyRecord>();
			while ( rs.next()) {
				records.add(new QgrsHomologyRecord(rs));
			}
			double  elapsed = System.nanoTime() - start;
			System.out.println("QUERY TIME:  " + new DecimalFormat("0.000").format(elapsed /1000000000) + " sec");
			return records;
			
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public HashMap<String, Integer> getCountByAlignmentId(HomologyQuery where) {
		try {
			
			String query = "SELECT alignmentId, COUNT(*) as total FROM QGRS_H " + where.toSql() + " GROUP BY alignmentId";
			System.out.println("Optomization Candidate:\nQUERY DEBUG:  " + query);
			long start = System.nanoTime();
			PreparedStatement ps = dc.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			HashMap<String, Integer> retval = new HashMap<String, Integer> ();
			while ( rs.next()) {
				retval.put(rs.getString("alignmentId"), rs.getInt("total"));
			}
			double  elapsed = System.nanoTime() - start;
			System.out.println("QUERY TIME:  " + new DecimalFormat("0.000").format(elapsed /1000000000) + " sec");
			return retval;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	public int getCount(HomologyQuery where) {
		try {
			String query = "SELECT COUNT(*) as total FROM QGRS_H " + where.toSql();
			System.out.println("Optomization Candidate:\nQUERY DEBUG:  " + query);
			long start = System.nanoTime();
			PreparedStatement ps = dc.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			double  elapsed = System.nanoTime() - start;
			System.out.println("QUERY TIME:  " + new DecimalFormat("0.000").format(elapsed /1000000000) + " sec");
			if ( rs.next()) {
				return rs.getInt("total");
			}
			
			return 0;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public boolean has(String alignmentId) {
		return get(alignmentId).size() > 0;
	}
	
	
	
	private PreparedStatement createInsertStatement() {
		StatementBuilder builder = buildBuilder();
		return this.buildInsert(builder);
	}
	
	private PreparedStatement createSelectByAlignmentIdStatement() {
		StatementBuilder builder = new StatementBuilder(TABLE);
		return buildSelectByAlignmentIdStatement(builder);
	}
	private PreparedStatement buildSelectByAlignmentIdStatement(StatementBuilder builder) {
		QueryConstraints qc = new QueryConstraints();
		qc.add(new QueryConstraint("alignmentId", ""));
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
		builder.add("alignmentId");
		builder.add("gq1Id");
		builder.add("gq2Id");
		builder.add("overallScore");
		
		builder.add("p_accessionNumber");
		builder.add("p_geneName");
		builder.add("p_geneSymbol");
		builder.add("p_species");
		builder.add("p_tetrads");
		builder.add("p_gScore");
		builder.add("p_in5UTR");
		builder.add("p_inCDS");
		builder.add("p_in3UTR");
		builder.add("c_accessionNumber");
		builder.add("c_geneName");
		builder.add("c_geneSymbol");
		builder.add("c_species");
		builder.add("c_tetrads");
		builder.add("c_gScore"); 
		builder.add("c_in5UTR");
		builder.add("c_inCDS");
		builder.add("c_in3UTR");
		builder.add("alignmentScore");
		
		return builder;
	}
	
	private void fillPreparedStatement(PreparedStatement ps, QgrsHomologyRecord r) {
		try {
			ps.setString(1, r.getAlignmentId());
			ps.setString(2, r.getGq1Id());
			ps.setString(3, r.getGq2Id());
			ps.setDouble(4, r.getOverallScore());
			
			ps.setString(5, r.getP_accessionNumber());
			ps.setString(6, r.getP_geneName());
			ps.setString(7, r.getP_geneSymbol());
			ps.setString(8, r.getP_species());
			ps.setInt(9, r.getP_tetrads());
			ps.setInt(10, r.getP_gScore());
			ps.setBoolean(11, r.isP_in5UTR());
			ps.setBoolean(12, r.isP_inCDS());
			ps.setBoolean(13, r.isP_in3UTR());
			ps.setString(14, r.getC_accessionNumber());
			ps.setString(15, r.getC_geneName());
			ps.setString(16, r.getC_geneSymbol());
			ps.setString(17, r.getC_species());
			ps.setInt(18, r.getC_tetrads());
			ps.setInt(19, r.getC_gScore());
			ps.setBoolean(20, r.isC_in5UTR());
			ps.setBoolean(21, r.isC_inCDS());
			ps.setBoolean(22, r.isC_in3UTR());
			ps.setDouble(23, r.getAlignmentScore());
			
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
