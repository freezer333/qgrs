package qgrs.db;

import java.util.Collection;

public class DbTable {

	public DbTable() {
		super();
	}

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

}