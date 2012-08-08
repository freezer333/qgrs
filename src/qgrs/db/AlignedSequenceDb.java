package qgrs.db;

import java.io.Reader;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import framework.db.QueryConstraint;
import framework.db.QueryConstraints;
import framework.db.StatementBuilder;

public class AlignedSequenceDb extends DbTable {
private static String TABLE = "GENE_A_SEQ";
	
	DatabaseConnection dc;
	final PreparedStatement insertStatement;
	final PreparedStatement selectStatement;
	
	
	public AlignedSequenceDb(DatabaseConnection dc) {
		this.dc = dc;
		this.insertStatement = createInsertStatement();
		this.selectStatement = createSelectStatement();
	}
	
	public void close() {
		try {
			this.insertStatement.close();
			this.selectStatement.close();
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	
	public void put(String alignmentId, String accessionNumber, String sequence) {
		PreparedStatement ps = null;
		if (has(alignmentId, accessionNumber)) {
			return;
		}
		else {
			ps = insertStatement;
		}
		
		fillPreparedStatement(ps, alignmentId, accessionNumber, sequence);
		this.dc.executeUpdate(ps);
	}
	
	private String readClob(Reader reader) {
		return new Scanner(reader).useDelimiter("\\A").next();
	}
	
	public String get(String alignmentId, String accessionNumber) {
		try {
			this.selectStatement.setString(1, alignmentId);
			this.selectStatement.setString(2, accessionNumber);
			ResultSet rs = this.selectStatement.executeQuery();
			if ( rs.next()) {
				return readClob(rs.getCharacterStream("sequence"));
			}
			else {
				return null;
			}
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	
	public boolean has(String alignmentId, String accessionNumber) {
		try {
			this.selectStatement.setString(1, alignmentId);
			this.selectStatement.setString(2, accessionNumber);
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
	
	
	private PreparedStatement buildSelectStatement(StatementBuilder builder) {
		QueryConstraints qc = new QueryConstraints();
		qc.add(new QueryConstraint("id", ""));
		qc.add(new QueryConstraint("accessionNumber", ""));
		try {
			return dc.getConnection().prepareStatement(builder.buildSelectStatement(qc));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private StatementBuilder buildBuilder() {
		StatementBuilder builder = new StatementBuilder(TABLE);
		builder.add("id");
		builder.add("accessionNumber");
		builder.add("sequence");
		return builder;
	}
	
	
	
	
	private void fillPreparedStatement(PreparedStatement ps, String alignmentId, String accessionNumber, String sequence) {
		try {
			ps.setString(1, alignmentId);
			ps.setString(2, accessionNumber);
			ps.setClob(3, new StringReader(sequence));
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
