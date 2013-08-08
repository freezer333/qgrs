package qgrs.db.seed;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import qgrs.data.providers.PredictorInputProvider;
import qgrs.data.providers.RDBAlignmentProvider;
import qgrs.data.providers.RDBSequenceProvider;
import qgrs.data.providers.SequenceProvider;
import qgrs.db.AppProperties;
import qgrs.db.DatabaseConnection;
import qgrs.db.tasks.InputPair;
import qgrs.job.AlignmentJob;
import qgrs.output.MongoResultProcessor;
import framework.db.DatabaseConnectionParameters;

public class SeedV2 {
	
	static int complete = 0;
	static int errors = 0;
	static DatabaseConnection connection;
	static SequenceProvider provider;
	static RDBAlignmentProvider ap;
	
	static {
		DatabaseConnectionParameters params  = new DatabaseConnectionParameters(AppProperties.getSeedCacheConnectionStringFromPropsxml(), "sa", "sa");
		connection = new DatabaseConnection(params);
		provider = new RDBSequenceProvider(connection);
		ap = new RDBAlignmentProvider(connection);
		
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
	
	public static void processBatch(File inputFile)  {
		List<InputPair> initialPairs = new LinkedList<InputPair>();
		
		loadPairs(inputFile, initialPairs);
		List<InputPair> pairs = new LinkedList<InputPair>();
		
		for (InputPair candidate : initialPairs) {
			if ( SeedUtils.checkSpecies(candidate.principleSpecies, SeedUtils.PrincipleSpeciesSet) &&
					SeedUtils.checkSpecies(candidate.comparisonSpecies, SeedUtils.ComparisonSpeciesSet)) {
				pairs.add(candidate);
			}
			
		}
		
		int i = 1;
		for ( InputPair pair : pairs) {
			System.out.println("Seeding (" + inputFile.getName() + ") - Computing homology " + i + " of " + pairs.size() + " (" + new DecimalFormat("0.00%").format(((double)i)/pairs.size()) + ")");
			computeAndSend(pair);
			i++;
			
		}
		System.out.println("Seeding (" + inputFile.getName() + ") - " + complete + " computed, " + errors + " errors.");
		
	}
	
	
	/**
	 * This program performs the alignment and sending of each pair to the server. 
	 *
	 */
	public static void main(String[] args) throws Exception {
		int i = 1;
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				return arg0.getName().endsWith(".in");
			}
		};
		File [] inputs = new File(SeedUtils.InputPairDir).listFiles(filter);
		System.out.println("Loaded " + inputs.length + " input batches");
		long start = System.currentTimeMillis();
		for ( File f : inputs ) {
			processBatch(f);
			long elapsed = System.currentTimeMillis() - start;
			double percent = ((double)i)/inputs.length;
			double expectedTime = elapsed / percent;
			double msToComplete = expectedTime - elapsed;
			double hours = msToComplete / 1000/60/60;
			double days = hours / 24;
			System.out.println("Completed batch " + i + " of " + inputs.length + " -> " + new DecimalFormat("0.00%").format(((double)i)/inputs.length) + ").  Predicted finish in " + new DecimalFormat("0.00").format(hours) + " hours / " + new DecimalFormat("0.00").format(days) + " days.");
			f.renameTo(new File(f.getAbsoluteFile()+".complete"));
			i++;
		}
	}
	
	static void loadPairs(File inputFile,List<InputPair> pairs) {
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				if ( strLine.trim().length() > 0 ) {
					String [] split = strLine.split("\t");
					if ( split.length > 1 ) {
						pairs.add(new InputPair(split[0].trim(), split[2].trim(), split[1], split[3]));
					}
				}
			}
			in.close();
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public static void computeAndSend(InputPair pair) {
		try {
			int throttle = runNextAlignment(new PredictorInputProvider(pair.principle, pair.comparison, provider));
			complete++;
			if ( throttle != 0 ) {
				System.out.println("\tThrottling [" + throttle + "ms]");
				Thread.sleep(throttle);
			}
			
		}
		catch (Throwable t) {
			System.out.println("Error:  " + pair.principle + " x " + pair.comparison) ;
			t.printStackTrace();
			errors++;
		} 
	}
	
	private static int runNextAlignment(PredictorInputProvider inputProvider) throws Exception {
		AlignmentJob job = new AlignmentJob(inputProvider,new MongoResultProcessor(),	null, ap);
		job.runJob();
		return job.getRequiredThrottle();
		
	}
}
