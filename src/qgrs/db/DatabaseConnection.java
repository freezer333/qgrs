package qgrs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import framework.db.DatabaseConnectionParameters;

public class DatabaseConnection {
	DatabaseConnectionParameters params;
	Connection connection;
	
	
	public DatabaseConnection(DatabaseConnectionParameters params) {
		this.params = params;
		this.connection = buildConnection();
	}
	public DatabaseConnection(Connection connection) {
		this.params = null;
		this.connection = connection;
	}
	
	
	public Connection getConnection() {
		return connection;
	}
	public void close() {
		this.closeConnection(this.connection);
	}
	
	void executeUpdate(PreparedStatement ps) {
		try {
			ps.executeUpdate();
			
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	private Connection buildConnection() {
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
	private void closeConnection(Connection c) {
		try {
			if ( c != null ) {
				c.close() ;
			}
		}
		catch (Exception e ){
			throw new RuntimeException (e);
		}
	}
	
}
