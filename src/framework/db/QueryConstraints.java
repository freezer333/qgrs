package framework.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;


public class QueryConstraints extends LinkedList<QueryConstraint>{

	public QueryConstraints() {
		super();
	}

	
	
	public String getParameterString() {
		if ( this.size() < 1 ) return "";
		String query = "WHERE ";
		for ( QueryConstraint q : this ) {
			query += q.getName() + q.getOperand() + "? AND ";
		}
		query =  query.substring(0, query.length() - " AND ".length());
		return query;
	}
	public void fillPreparedStatement(PreparedStatement ps) throws SQLException {
		int index = 1;
		for ( QueryConstraint q : this ) {
			if ( q.getValue() instanceof Integer ) {
				ps.setInt(index++, (Integer)q.getValue());
			}
			else if ( q.getValue() instanceof String) {
				ps.setString(index++, (String) q.getValue());
			}
			else {
				throw new UnsupportedOperationException ("Query Constraint of unsupported type - " + q.getName() + " = " + q.getValue());
			}
		}
	}
}
