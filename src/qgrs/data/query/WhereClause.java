package qgrs.data.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import framework.web.util.StringUtils;

public abstract class WhereClause {

	public abstract String toSql();
	public abstract void set(PreparedStatement ps)  throws SQLException;
	
	protected String p(Object v) {
		return "'" + v + "'";
	}
	
	protected String stringConstraint(String column, String constraint) {
		if ( StringUtils.isDefined(constraint)) {
			return column + " = " + p(constraint);
		}
		else return "";
	}


	protected String in(Collection<String> terms) {
		int count = 0;
		StringBuilder b = new StringBuilder();
		for ( String t : terms) {
			if ( StringUtils.isDefined(t) ) {
				if ( count > 0) b.append(", ");
				b.append("'");
				b.append(t);
				b.append("'");
				count++;
			}
		}
		return " IN (" + b.toString() + ") ";
	}
	private String and(Collection<String> criteria) {
		int count = 0;
		StringBuilder b = new StringBuilder();
		for ( String c : criteria) {
			if ( StringUtils.isDefined(c)) {
				if ( count > 0 ) b.append(" AND ");
				b.append(c);
				count++;
			}
		}
		return b.toString();
	}
	protected String where(Collection<String> criteria) {
		String a = and(criteria);
		if ( StringUtils.isDefined(a)) {
			return " WHERE " + a; 
		}
		else {
			return "";
		}
	}
	protected String continuedWhere(Collection<String> criteria) {
		String a = and(criteria);
		if ( StringUtils.isDefined(a)) {
			return " AND " + a; 
		}
		else {
			return "";
		}
	}
	
	public String toSql(WhereClause other) {
		return or(this, other);
	}
	protected static String or(WhereClause wc1, WhereClause wc2) {
		String c1 = wc1.toSql();
		String c2 = wc2.toSql();
		if ( !StringUtils.isDefined(c1)) {
			return wc2.toSql();
		}
		if ( !StringUtils.isDefined(c2)) {
			return wc1.toSql();
		}
		int pos1 = c1.indexOf("WHERE");
		int pos2 = c2.indexOf("WHERE");
		String c1p = c1.substring(pos1 + "WHERE".length());
		String c2p = c2.substring(pos2 + "WHERE".length());
		return " WHERE (" + c1p + ") OR (" + c2p + ")"; 
	}
	
	
}
