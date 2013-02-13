package qgrs.compute.stat;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import qgrs.db.AppProperties;
import framework.db.DatabaseConnectionParameters;

public abstract class Analysis implements Callable<Object>{

	final int THREAD_POOL_SIZE = 20;
	
	final GenePartitioner partitioner;
	protected final boolean active;
	
	public Analysis(boolean active) {
		super();
		this.partitioner = buildPartitioner();
		this.active = active;
	}
	
	@Override
	public Object call() throws Exception {
		execute();
		return null;
	}
	
	

	public void execute() throws Exception {
		System.out.println("Connecting to database...");
		Connection c = getConnection();
		
		System.out.println("Initializing database tables...");
		
		// Make sure the tables exist
		createTableIfNotExists(c);
		createPartitionTableIfNotExists(c);
		prepareCustomTables(c);
		
		// Wipe out existing data for this analysis (reverse order)
		cleanCustomTablesById(c);
		cleanTablesById(c);
		
		// Create a record for this analysis
		insert(c);
		
		System.out.println("Executing " + this.getDescription());
		System.out.print("Creating partitions...");
		HashSet<GenePartition> partitions = partitioner.partition(c);
		
		StatusReporter r= new StatusReporter(partitions.size());
		System.out.println(" " + partitions.size() + " created");
		
		Collection<PartitionAnalyzer> processors = new HashSet<PartitionAnalyzer>();
		for ( GenePartition partition: partitions) {
			recordPartitionEntry(c, partition);
			processors.add(createProcessor(partition, r));
		} 
		
		System.out.println("Executing analysis...");
		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		List<Future<PartitionResult>> results = executorService.invokeAll(processors);
		
		System.out.println("\nReporting Results...");
		PreparedStatement ps = this.buildResultsStatement(c);
		for  ( Future<PartitionResult> result : results ) {
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
	
	protected abstract PreparedStatement buildResultsStatement(Connection c)  throws Exception;
	protected abstract GenePartitioner buildPartitioner();
	protected abstract PartitionAnalyzer createProcessor(GenePartition partition, StatusReporter reporter);
	public abstract String getDescription();
	public abstract String getId();
	
	
	public boolean isActive() {
		return this.active;
	}
	
	
	public String getTableName() {
		return getId().replace(" ", "_").toUpperCase();
	}
	
	
	protected abstract void cleanCustomTablesById(Connection c);
	protected abstract void prepareCustomTables(Connection c);
	
	
	
	private void recordPartitionEntry(Connection c, GenePartition partition) {
		createPartitionTableIfNotExists(c);
		insert(c, partition);
	}
	
	private void createPartitionTableIfNotExists(Connection c) {
		String sql = "create table if not exists partition (" + 
					 "analysisId varchar(255) not null , " +
					 "partitionId varchar(255) not null , " +
					 "description varchar(max), " +
					 "numSamples int, " + 
					 "primary key (analysisId, partitionId), " +
					 "foreign key (analysisId) references analysis(id) on delete cascade)";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			executeUpdateAndClose(ps);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void insert(Connection c, GenePartition partition) {
		String sql = "insert into partition (analysisId, partitionId, description, numSamples) values (?, ?, ?, ?)";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, this.getId());
			ps.setString(2, partition.getPartitionId());
			ps.setString(3, partition.getDescription());
			ps.setInt(4, partition.getGeneIds().size());
			executeUpdateAndClose(ps);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void executeUpdateAndClose(PreparedStatement ps) {
		try {
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	private void createTableIfNotExists(Connection c) {
		String sql = "create table if not exists analysis (" + 
					 "id varchar(255) not null primary key, " +
					 "description varchar(max), " +
					 "active boolean not null default false, " + 
					 "date date not null)";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			executeUpdateAndClose(ps);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void cleanTablesById(Connection c) {
		
		
		String sql = "delete from analysis where id = ?";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, this.getId());
			executeUpdateAndClose(ps);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void insert(Connection c) {
		// insert this ID into the analysis table. 
		String sql = "insert into analysis (id, description, active, date) values (?, ?, ?, ?)";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, this.getId());
			ps.setString(2, this.getDescription());
			ps.setBoolean(3, this.isActive());
			ps.setDate(4, new Date(new java.util.Date().getTime()));
			executeUpdateAndClose(ps);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
