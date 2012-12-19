package framework.db;

import java.util.LinkedList;

public class StatementBuilder extends LinkedList<String> {
	
	final String tableName;
	
	public StatementBuilder(String tableName) {
		super();
		this.tableName = tableName;
	}
	public String buildSelectStatement(QueryConstraints constraints) {
		return buildSelectStatement(constraints, null);
	}
	public String buildSelectStatement(QueryConstraints constraints, JoinList joins) {
		String stmt = "SELECT * FROM " + this.tableName ;
		for ( String column : this ) {
			stmt += (column + "=?, ");
		}
		stmt += " ";
		if ( joins != null ) {
			for ( Join j : joins) {
				stmt += j.getJoinExpression();
				stmt += " ";
			}
		}
		stmt += constraints.getParameterString();
	//	System.out.println(stmt);
		return stmt;
	}
	
	public String buildDeleteStatement(QueryConstraints constraints) {
		String stmt = "DELETE FROM " + this.tableName ;
		for ( String column : this ) {
			stmt += (column + "=?, ");
		}
		stmt += " ";
		stmt += constraints.getParameterString();
	//	System.out.println(stmt);
		return stmt;
	}
	
	
	public String buildUpdateStatement(QueryConstraints constraints) {
		String stmt = "UPDATE " + this.tableName + " SET ";
		for ( String column : this ) {
			stmt += (column + "=?, ");
		}
		stmt = stmt.substring(0, stmt.length()-2);
		stmt += " ";
		stmt += constraints.getParameterString();
		return stmt;
	}
	
	public String buildInsertStatement() {
		String stmt = "INSERT INTO " + this.tableName + "(";
		for ( String column : this ) {
			stmt += column;
			stmt += ", ";
		}
		stmt = stmt.substring(0, stmt.length()-2);
		stmt += ") VALUES (";
		for ( int i = 0; i < this.size(); i++ ) {
			stmt += "?, ";
		}
		stmt = stmt.substring(0, stmt.length()-2);
		stmt += ")";
		return stmt;
	}
}
