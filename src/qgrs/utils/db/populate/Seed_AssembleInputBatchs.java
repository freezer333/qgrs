package qgrs.utils.db.populate;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

public class Seed_AssembleInputBatchs {

	final int BATCH_SIZE = 100;
	final File taxonomyFile = new File("seed_data/taxid_taxname");
	final File fullProteinFile = new File("seed_data/all_proteins.data");
	final File homologeneFile = new File("seed_data/homologene.data");
	final HashMap<String, String> taxMap = new HashMap<String, String>();
	final HashMap<String, String> protienMap = new HashMap<String, String>();
	int pairs = 0;
	
	class HomologeneRow {
		final public String group;
		final public String species;
		final public String mrna;
		
		public HomologeneRow(String group, String species, String mrna) {
			this.group = group;
			this.species = species;
			this.mrna = mrna;
		}
	}
	
	PrintWriter openNext(int batchNumber) throws IOException{
		File f = new File(SeedUtils.InputPairDir + "homologs_" +  batchNumber + ".in");
		PrintWriter fout = new PrintWriter( new FileWriter(f));
		return fout;
	}
	
	
	void buildHomologenePairs() {
		try {
			
			FileInputStream fstream = new FileInputStream(homologeneFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			HomologeneRow principle = null;
			HomologeneRow current;
			int count = 0;
			int batch = 0;
			PrintWriter fout = openNext(batch);
			while ((strLine = br.readLine()) != null)   {
				if ( count >= BATCH_SIZE) {
					fout.close();
					batch++;
					count = 0;
					fout = openNext(batch);
				}
				
				current = buildHomologeneRow(strLine);
				if ( current == null ) continue;
				
				if ( principle == null || !current.group.equalsIgnoreCase(principle.group)) {
					principle = current;
				}
				else {
					fout.println(principle.mrna + "\t" + 
									principle.species + "\t" + 
									current.mrna + "\t" + 
									current.species);
					pairs++;
					count++;
				}
				
			}
			fout.close();
			in.close();
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	HomologeneRow buildHomologeneRow(String line) {
		String [] split = line.split("\t");
		
		if ( split.length >= 5 ) {
			String g = split[0].trim();
			String s_id = split[1].trim();
			String p = split[5].trim();
			
			return new HomologeneRow(	g, 
										this.taxMap.get(s_id), 
										this.protienMap.get(p));
		}
		else return null;
	}
	
	
	
	void buildProtienMap() {
		try {
			FileInputStream fstream = new FileInputStream(fullProteinFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				if ( strLine.trim().length() > 0 ) {
					String [] split = strLine.split("\t");
					if ( split.length > 1 ) {
						protienMap.put(split[4].trim(), split[5].trim());
					}
				}
			}
			in.close();
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	void buildTaxonomyMap() {
		try {
			FileInputStream fstream = new FileInputStream(taxonomyFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				if ( strLine.trim().length() > 0 ) {
					String [] split = strLine.split("\t");
					if ( split.length > 1 ) {
						taxMap.put(split[0].trim(), split[1].trim());
					}
				}
			}
			in.close();
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public void execute() {
		buildTaxonomyMap();
		System.out.println("Seeding:  " + taxMap.keySet().size() + " taxonomies loaded");
		
		buildProtienMap();
		System.out.println("Seeding:  " + protienMap.keySet().size() + " protiens loaded");
		
		this.buildHomologenePairs();
		System.out.println("Seeding:  " + pairs + " pairs created");
		
	}
	
	
	public static void main(String[] args) {
		//SeedStage0.main(null);
		new Seed_AssembleInputBatchs().execute();
	}

}
