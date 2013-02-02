package qgrs.compute.stat.qgrs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TableReader {
	enum RowReader {
		Integer {
			@Override
			public String read(ResultSet rs, String columnName) throws SQLException {
				return new Integer(rs.getInt(columnName)).toString();
			}
		},
		Double {
			@Override
			public String read(ResultSet rs, String columnName) throws SQLException {
				return new Double(rs.getDouble(columnName)).toString();
			}
		},
		Boolean {
			@Override
			public String read(ResultSet rs, String columnName) throws SQLException {
				return new Boolean(rs.getBoolean(columnName)).toString();
			}
		},
		String {
			@Override
			public String read(ResultSet rs, String columnName) throws SQLException {
				return rs.getString(columnName);
			}
		};
		
		public abstract String read(ResultSet rs, String columnsName) throws SQLException;
	}
	
	
	private String tableName;
	public final Map<String, RowReader> columns;
	
	
	public TableReader(String tableName, Connection dc) throws SQLException {
		this.tableName = tableName;
		Map<String, RowReader> cols = new HashMap<String, RowReader>();
		String q = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='" + this.tableName + "'";
		Statement stmt = dc.createStatement();
		ResultSet rs = stmt.executeQuery(q);
		while ( rs.next() ) {
			String columnName = rs.getString("COLUMN_NAME");
			String datatype = rs.getString("TYPE_NAME");
			RowReader rr = RowReader.String;
			if ( datatype.equalsIgnoreCase("INTEGER")) {
				rr = RowReader.Integer;
			}
			if ( datatype.equalsIgnoreCase("DOUBLE")) {
				rr = RowReader.Double;
			}
			if ( datatype.equalsIgnoreCase("BOOLEAN")) {
				rr = RowReader.Boolean;
			}
			cols.put(columnName, rr);
		}
		
		columns = Collections.unmodifiableMap(cols);
		
		stmt.close();
	}
	
}
