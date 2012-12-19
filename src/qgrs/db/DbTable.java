package qgrs.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public abstract class DbTable {

	public DbTable() {
		super();
	}
	
	public abstract int getCount();

	protected String getCSL(Collection<String> tokens) {
		StringBuilder sb = new StringBuilder() ;
		for ( String s : tokens) {
			sb.append("'");
			sb.append(s);
			sb.append("'");
			sb.append(",");
		}
		String r = sb.toString();
		return r.substring(0, r.length()-1);
	}
	
	protected int getCount(String tableName, Connection connection) {
		try {
			String q = "SELECT COUNT(*) FROM " + tableName;
			ResultSet rs = connection.createStatement().executeQuery(q);
			if ( rs.next() ) {
				return rs.getInt(1);
			}
		}
		catch ( Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}