package qgrs.utils.db.populate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SeedUtils {

	public static final String InputFilename = "/Users/sfrees/dev/qgrs/input_testing.txt";
	public static final String ConnectionString = "jdbc:h2:file:/Users/sfrees/dev/seed_db";
	
	public static final String [] PrincipleSpeciesSet = {"Homo sapiens"};
	public static final String [] ComparisonSpeciesSet = {"Mus musculus", "Bos taurus"};
	
	public static final String servername = "localhost";
	public static final int serverport = 8091;
	
	
	
	
	public static boolean checkSpecies(String species, String [] matchSet) {
		for ( String s : matchSet ) {
			if ( s.equalsIgnoreCase(species)) {
				return true;
			}
		}
		return false;
	}
	public static Connection getConnection() {
		try {
		 Class.forName("org.h2.Driver");
	        Connection conn = DriverManager.
	            getConnection(ConnectionString, "sa", "sa");
	        return conn;
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public static void closeConnection(Connection c) {
		try {
			if ( c != null ) {
				c.close() ;
			}
		}
		catch (Exception e ){
			throw new RuntimeException (e);
		}
	}
	
	public static void  execute(Connection conn, String q) {
		try {
			Statement stmt = conn.createStatement();
	   		stmt.executeUpdate(q);
			stmt.close();
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
}
