package qgrs.test;

import framework.db.DatabaseConnectionParameters;
import qgrs.db.DatabaseConnection;

public class TestDatabaseConnection extends DatabaseConnection {

	public TestDatabaseConnection() {
		super(new DatabaseConnectionParameters("jdbc:h2:~/qgrs_test", "sa", "sa"));
	}
}
