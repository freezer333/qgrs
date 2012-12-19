package qgrs.test;

import qgrs.db.DatabaseConnection;
import framework.db.DatabaseConnectionParameters;

public class TestDatabaseConnection extends DatabaseConnection {

	public TestDatabaseConnection() {
		super(new DatabaseConnectionParameters("jdbc:h2:~/qgrs_test", "sa", "sa"));
	}
}
