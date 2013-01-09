package qgrs.compute.stat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import qgrs.db.AppProperties;
import framework.db.DatabaseConnectionParameters;

public abstract class Engine {

	final int THREAD_POOL_SIZE = 20;
	
	final GenePartitioner partitioner;
	final PartitionResultStatementBuilder statementBuilder;
	
	public Engine() {
		super();
		this.partitioner = buildPartitioner();
		this.statementBuilder = buildStatementBuilder();
	}

	public void execute() throws Exception {
		System.out.println("Connecting to database...");
		Connection c = getConnection();
		System.out.print("Creating partitions...");
		HashSet<GenePartition> partitions = partitioner.partition(c);
		System.out.println(" " + partitions.size() + " created");
		Collection<PartitionAnalyzer> processors = new HashSet<PartitionAnalyzer>();
		
		for ( GenePartition partition: partitions) {
			processors.add(createProcessor(partition));
		}
		System.out.println("Executing analysis...");
		ExecutorService executorService = Executors.newFixedThreadPool(16);
		List<Future<PartitionResult>> results = executorService.invokeAll(processors);
		System.out.println("Reporting Results...");
		PreparedStatement ps = statementBuilder.buildPreparedStatementForBatch(c);
		for  ( Future<PartitionResult> result : results ) {
			if ( result == null ) {
				System.out.println("null future");
			}
			if ( result.get() == null ) {
				System.out.println("Null Result");
			}
			result.get().addBatch(ps);
		}
		
		if ( ps != null ) {
			ps.executeBatch();
		}
		
		c.close();
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
	
	protected abstract GenePartitioner buildPartitioner();
	protected abstract PartitionResultStatementBuilder buildStatementBuilder();
	protected abstract PartitionAnalyzer createProcessor(GenePartition partition);
	
	
	
	
	
}
