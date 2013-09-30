package qgrs.db.seed;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;

import qgrs.data.GeneSequence;
import qgrs.data.mongo.operations.DbSave;
import qgrs.data.providers.RDBAlignmentProvider;
import qgrs.data.providers.RDBSequenceProvider;
import qgrs.data.providers.SequenceProvider;
import qgrs.data.records.AlignmentRecord;
import qgrs.db.AlignmentRecordDb;
import qgrs.db.AppProperties;
import qgrs.db.DatabaseConnection;
import qgrs.db.GeneSequenceDb;
import framework.db.DatabaseConnectionParameters;

public class MrnaHomologInsert {
	
	static int complete = 0;
	static int errors = 0;
	static DatabaseConnection connection;
	static SequenceProvider provider;
	static RDBAlignmentProvider ap;
	static DbSave mongoSave;
	
	static {
		DatabaseConnectionParameters params  = new DatabaseConnectionParameters(AppProperties.getSeedCacheConnectionStringFromPropsxml(), "sa", "sa");
		connection = new DatabaseConnection(params);
		provider = new RDBSequenceProvider(connection);
		ap = new RDBAlignmentProvider(connection);
		try {
			mongoSave = new DbSave();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static Connection getConnection(DatabaseConnectionParameters params) {
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
	
	
	
	public static void main(String[] args) throws Exception {
		GeneSequenceDb geneDb = new GeneSequenceDb(connection);
		AlignmentRecordDb alignmentdb = new AlignmentRecordDb (connection);
		for ( AlignmentRecord ar : alignmentdb.getAll() ) {
			System.out.println(ar.getPrinciple() + " -> " + ar.getComparison());
			GeneSequence homolog = geneDb.get(ar.getComparison());
			mongoSave.insertHomolog(ar.getPrinciple(), homolog, ar.getSimilarityPercentage());
		}
		
		alignmentdb.close();
		geneDb.close();
	}



	
	
	
	
}
