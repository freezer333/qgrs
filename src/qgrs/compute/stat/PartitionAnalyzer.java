package qgrs.compute.stat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.Callable;

import qgrs.db.AppProperties;
import framework.db.DatabaseConnectionParameters;



public abstract class PartitionAnalyzer implements Callable<PartitionResult>{

	final protected GenePartition parition;
	final protected StatusReporter statusReporter;
	
	public PartitionAnalyzer(GenePartition parition, StatusReporter reporter) {
		this.parition = parition;
		this.statusReporter = reporter;
	}
	
	protected Connection getConnection(){
		DatabaseConnectionParameters params = new DatabaseConnectionParameters(AppProperties.getConnectionStringFromPropsxml(), "sa", "sa");
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
}