package qgrs.utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import qgrs.db.AppProperties;

import framework.db.DatabaseConnectionParameters;

public abstract class AbstractDbTask {

	Connection conn;
	DatabaseConnectionParameters params = new DatabaseConnectionParameters(AppProperties.getConnectionStringFromPropsxml(), "sa", "sa");
	protected boolean printSql = false;
	
	public AbstractDbTask () {
		conn = getConnection();
	}
	public abstract void report();
	
	protected int executeCount(String q) {
		try {
			if ( printSql ) System.out.println(q);
			Statement stmt = conn.createStatement();
	   		ResultSet rs = stmt.executeQuery(q);
	   		int retval = 0;
	   		if ( rs.next() ) {
	   			retval = rs.getInt(1);
	   		}
	   		
			stmt.close();
			return retval;
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	protected void executeUpdate( String q) {
		try {
			if ( printSql ) System.out.println(q);
			Statement stmt = conn.createStatement();
	   		stmt.executeUpdate(q);
			stmt.close();
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	void dropTable(Connection conn, String table) {
		try {
			String q = "DROP TABLE "+table;
			if ( printSql )  System.out.println(q);
			executeUpdate(q);
		}
		catch (Exception e) {
			System.out.println(table + " does not exist");
		}
	}
	protected Connection getConnection() {
		try {
		 Class.forName("org.h2.Driver");
	        Connection conn = DriverManager.
	            getConnection(params.getConnectionString(), params.getUsername(), params.getPassword());
	        return conn;
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	protected void closeConnection() {
		try {
			if ( conn != null ) {
				conn.close() ;
			}
		}
		catch (Exception e ){
			throw new RuntimeException (e);
		}
	}
}
