package framework.db;

public class DatabaseConnectionParameters {

	private final String connectionString;
	private final String username;
	private final String password;
	public DatabaseConnectionParameters(String connectionString,
			String username, String password) {
		super();
		this.connectionString = connectionString;
		this.username = username;
		this.password = password;
	}
	public String getConnectionString() {
		return connectionString;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	
	
}
