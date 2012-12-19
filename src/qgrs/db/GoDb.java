package qgrs.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import qgrs.data.GoType;
import qgrs.data.OntologyData;
import qgrs.data.records.OntologyRecord;
import framework.db.QueryConstraint;
import framework.db.QueryConstraints;
import framework.db.StatementBuilder;

public class GoDb {
	private static String TABLE = "GO";
	
	DatabaseConnection dc;
	final PreparedStatement insertStatement;
	final PreparedStatement selectByGeneStatement;
	
	public GoDb(DatabaseConnection dc) {
		this.dc = dc;
		
		this.insertStatement = createInsertStatement();
		this.selectByGeneStatement = createSelectByGeneStatement();
	}
	
	public void close() {
		try {
			this.insertStatement.close();
			this.selectByGeneStatement.close();
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public void delete(String accessionNumber) {
		PreparedStatement ps;
		try {
			ps = dc.getConnection().prepareStatement("DELETE FROM GO WHERE accessionNumber=?");
			ps.setString(1, accessionNumber);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void put(String accessionNumber, OntologyData data) {
		PreparedStatement ps = insertStatement;
		for ( String term : data.functionList ) {
			fillPreparedStatement(ps, accessionNumber, new OntologyRecord(term, GoType.Function));
			this.dc.executeUpdate(ps);
		}
		for ( String term : data.componentList ) {
			fillPreparedStatement(ps, accessionNumber, new OntologyRecord(term, GoType.Component));
			this.dc.executeUpdate(ps);
		}
		for ( String term : data.processList ) {
			fillPreparedStatement(ps, accessionNumber, new OntologyRecord(term, GoType.Process));
			this.dc.executeUpdate(ps);
		}
	}
	
	public OntologyData get(String accessionNumber) {
		OntologyData data = new OntologyData();
		try {
			this.selectByGeneStatement.setString(1, accessionNumber);
			ResultSet rs = this.selectByGeneStatement.executeQuery();
			while ( rs.next()) {
				OntologyRecord or = new OntologyRecord(rs.getString("goTerm"), GoType.valueOf(rs.getString("goType")));
				data.put(or);
			}
			return data;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	
	
	
	
	
	private PreparedStatement createInsertStatement() {
		StatementBuilder builder = buildBuilder();
		return this.buildInsert(builder);
	}
	
	
	private PreparedStatement createSelectByGeneStatement() {
		StatementBuilder builder = new StatementBuilder(TABLE);
		return buildSelectByGeneStatement(builder);
	}
	
	private PreparedStatement buildSelectByGeneStatement(StatementBuilder builder) {
		QueryConstraints qc = new QueryConstraints();
		qc.add(new QueryConstraint("accessionNumber", ""));
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
		builder.add("accessionNumber");
		builder.add("goTerm");
		builder.add("goType");
		return builder;
	}
	
	private void fillPreparedStatement(PreparedStatement ps, String accessionNumber, OntologyRecord or) {
		try {
			ps.setString(1, accessionNumber);
			ps.setString(2, or.term);
			ps.setString(3, or.type.toString());
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
