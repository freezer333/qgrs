package qgrs.compute.stat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.Callable;

import qgrs.db.AppProperties;
import framework.db.DatabaseConnectionParameters;



public abstract class PartitionAnalyzer implements Callable<PartitionResult>{

	final protected GenePartition genes;
	
	public PartitionAnalyzer(GenePartition parition) {
		this.genes = parition;
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
