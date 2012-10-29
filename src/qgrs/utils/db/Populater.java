package qgrs.utils.db;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import qgrs.db.XmlWritePostCache;
import qgrs.input.AccessionNumberInputProvider;
import qgrs.input.InputProvider;
import qgrs.job.AlignmentJob;
import framework.web.util.StringUtils;

public class Populater {

	private final File inputFile;
	private List<InputPair> pairs = new LinkedList<InputPair>();
	HashSet<String> p = new HashSet<String>();
	HashSet<String> c = new HashSet<String>();
	HashMap<String, PrintWriter> files = new HashMap<String, PrintWriter>();
	
	HashSet<String> pReject = new HashSet<String>();
	HashSet<String> cReject = new HashSet<String>();
	
	public Populater (String filename) {
		this.inputFile = new File(filename);
		if ( !inputFile.exists() ) {
			throw new RuntimeException(inputFile.getAbsolutePath() + " does not exist");
		}
		loadPairs();
		
		
		
		
		p.add("Homo sapiens");
		
		//c.add("Homo sapiens");
		c.add("Mus musculus");
		//c.add("Rattus norvegicus");
		
	}
	
	void loadPairs() {
		try {
			FileInputStream fstream = new FileInputStream(this.inputFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				if ( strLine.trim().length() > 0 ) {
					String [] split = strLine.split("\t");
					if ( split.length > 1 ) {
						this.pairs.add(new InputPair(split[0].trim(), split[1].trim()));
					}
				}
			}
			in.close();
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	private boolean checkPreviousReject(String pId, String cId) {
		if ( pReject.contains(pId)) {
			return false;
		}
		if ( cReject.contains(cId)) {
			return false;
		}
		return true;
	}
	private void checkSpecies(String pId, String cId, String principle, String comparison) throws InvalidPairException {
		if ( !p.contains(principle) ) {
			pReject.add(pId);
			throw new InvalidPairException(principle + " x " + comparison);
		}
		if ( !c.contains(comparison) ) {
			cReject.add(cId);
			throw new InvalidPairException(principle + " x " + comparison);
		}
	}
	private boolean checkSpecies_noexception(String pId, String cId, String principle, String comparison) {
		if ( !p.contains(principle) ) {
			pReject.add(pId);
			return false;
		}
		if ( !c.contains(comparison) ) {
			cReject.add(cId);
			return false;
		}
		return true;
	}
	
	private void writePair(InputPair pair, String comparisonSpecies) {
		PrintWriter pw = this.files.get(comparisonSpecies);
		if ( pw == null ) {
			try {
				pw = new PrintWriter(new File("species/" + comparisonSpecies + ".txt"));
				this.files.put(comparisonSpecies, pw);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pair.write(pw);
		pw.flush();
		
	}
	
	public void splitFiles() {
		int success = 0;
		int failure = 0;
		int rejects = 0;
		for ( InputPair pair : pairs ) {
			if ( checkPreviousReject(pair.principle, pair.comparison ) ) {
				try {
					InputProvider ip = new AccessionNumberInputProvider(pair.principle, pair.comparison);
					String cSpecies = ip.getInput().getComparisons().get(0).getSpecies();
					if ( ip.getInput().getPrinciple().getSpecies().equals("Homo sapiens") ) {
						//System.out.println(ip.getInput().getPrinciple().getSpecies() + " x " + cSpecies);
						writePair(pair, cSpecies);
						success++;
					}
					else {
						//System.out.println("Principal rejected:  " +ip.getInput().getPrinciple().getSpecies() );
						pReject.add(pair.principle);
						rejects++;
					}
					
				} 
				catch (Throwable t) {
					t.printStackTrace();
					failure++;
				}
				try {
					Thread.currentThread().sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				rejects++;
			}
			System.out.println(success + " / " + rejects + " / " + failure + " / " + pairs.size() + " / " + new DecimalFormat("0.000%").format((success+rejects+failure)/(1.0*pairs.size())));
		}
		for ( PrintWriter pw : this.files.values()) {
			pw.close();
		}
	}
	// "47086420 x 169646278"
	private String startoff = "22091458 x 118130386";
	private boolean waitForStartOff = false;
	
	
	public void populate() {
		int max = 10000;
		int count = 0;
		int success = 0;
		
		/*  Sometimes the DB goes down, so we have a retry list */
		List<InputPair> retrys = new LinkedList<InputPair> ();
		while ( pairs.size() > 0 ) {
			for ( InputPair pair : pairs ) {
				String statusString = success + " / " + count + " / " + pairs.size();
				count++;
				if ( waitForStartOff && StringUtils.isDefined(startoff)) {
					String pairString = pair.principle + " x " + pair.comparison;
					if ( pairString.equalsIgnoreCase(startoff)) {
						waitForStartOff = false;
					}
					else {
						System.out.println("Startoff not reached - skipping " + pairString + " - " + statusString);
						continue;
					}
					
				}
				try {
					String pSpecies = "";
					String cSpecies = "";
					if ( checkPreviousReject(pair.principle, pair.comparison ) ) {
						InputProvider ip = new AccessionNumberInputProvider(pair.principle, pair.comparison);
						pSpecies = ip.getInput().getPrinciple().getSpecies();
						cSpecies = ip.getInput().getComparisons().get(0).getSpecies();
						checkSpecies(pair.principle, pair.comparison,pSpecies, cSpecies) ; 
						runNextAlignment(new AccessionNumberInputProvider(pair.principle, pair.comparison));
						success++;
						System.out.println("Completed (" + (count+1) + "/" + pairs.size() + "):  " + pair.principle + " x " + pair.comparison + " - " + statusString) ;
						Thread.sleep(2000);
					}
					//else {
					//	System.out.println("  Skipped pair " + pair.principle + " x " + pair.comparison + " => " + pSpecies + " x " + cSpecies+ " - " + statusString);
					//}
				}
				catch (InvalidPairException t) {
					System.out.println("* Skipped pair " + pair.principle + " x " + pair.comparison + " => " + t.getMessage()+ " - " + statusString);
					try {
						Thread.sleep(3000);
					}
					catch (Exception e) {
					}
				}
				catch (Throwable t) {
					System.out.println("Error:  " + pair.principle + " x " + pair.comparison + " - " + statusString) ;
					t.printStackTrace();
					retrys.add(pair);
				} 
				finally {
					
				}
				
				System.out.println("--------------------------------------------------------------------");
				
				
			}
			System.out.println("Processes a full pass through.");
			if ( retrys.size() > 0 ) {
				System.out.println(" Retrying " + retrys.size() + " failed pairs");
				pairs = retrys;
				retrys = new LinkedList<InputPair>();
			}
			else {
				pairs.clear();
				System.out.println("Process Complete");
			}
		}
		
		System.out.println("Saved " + count + " pairs");
		
	}
	
	private void runNextAlignment(AccessionNumberInputProvider inputProvider) throws Exception {
		//AlignmentJob job = new AlignmentJob(inputProvider, null, new XmlWritePostCache ( "quadruplex.ramapo.edu", "qgrs3", 80));
		AlignmentJob job = new AlignmentJob(inputProvider, null, new XmlWritePostCache ( "localhost", "qgrs", 8080));
		job.runJob();
	}
	
	
	public static void main(String[] args) {
		//Populater p = new Populater("/Users/sfrees/workspace/qgrs/input.txt");
		Populater p = new Populater("C:\\qgrs-test\\input.txt");
		//Populater p = new Populater("C:\\dev\\qgrs\\qgrs\\Mus musculus.txt");
		//p.populate();
		p.splitFiles();
	}

}
