package qgrs.utils.db.populate;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import qgrs.utils.db.InputPair;

public class SeedStage1 {

	static int pairsLoaded = 0;
	static int genesLoaded = 0;
	/**
	 * This program takes an input file and populates the seeding table with each gene ID pairs.
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		
		SeedStage0.main(null);
		
		File inputFile;
		List<InputPair> pairs = new LinkedList<InputPair>();
		
		
		inputFile = new File(SeedUtils.InputFilename);
		if ( !inputFile.exists() ) {
			throw new RuntimeException(inputFile.getAbsolutePath() + " does not exist");
		}
		
		loadPairs(inputFile, pairs);
		Connection conn = SeedUtils.getConnection();
		insertPairs(pairs, conn);
		
		System.out.println("Seeding Stage 1 Completed Successfully - " + pairsLoaded + " pairs, " + genesLoaded + " genes added.");
		
	}
	
	static void insertPairs(List<InputPair> pairs, Connection conn) throws Exception {
		int i = 0;
		for ( InputPair pair : pairs) {
			boolean pairExists = false;
			boolean principleExists = false;
			boolean comparisonExists = false;
			
			// add the pair (if it doesn't exist)
			String q = "SELECT COUNT(*) AS CT FROM SEED WHERE PrincipleId=? AND ComparisonId=?";
			PreparedStatement ps = conn.prepareStatement(q);
			ps.setString(1, pair.principle);
			ps.setString(2, pair.comparison);
			ResultSet rs = ps.executeQuery();
			
			if ( rs.next() ) {
				pairExists = rs.getInt("CT") > 0;
			}
			
			if ( !pairExists ) {
				q = "SELECT COUNT(*) AS CT FROM GENE WHERE ID=?";
				ps = conn.prepareStatement(q);
				ps.setString(1, pair.principle);
				rs = ps.executeQuery();
				if ( rs.next() ) {
					principleExists = rs.getInt("CT") > 0;
				}
				
				ps.setString(1, pair.comparison);
				rs = ps.executeQuery();
				if ( rs.next() ) {
					comparisonExists = rs.getInt("CT") > 0;
				}
				
				q = "INSERT INTO SEED (PrincipleId, ComparisonId, Status) VALUES(?, ?, ?)";
				ps = conn.prepareStatement(q);
				ps.setString(1, pair.principle);
				ps.setString(2, pair.comparison);
				ps.setString(3, StatusValue.Ready.toString());
				ps.executeUpdate();
				pairsLoaded++;
				
				q = "INSERT INTO GENE (ID, Species) VALUES(?, ?)";
				ps = conn.prepareStatement(q);
				
				if ( !principleExists) {
					ps.setString(1, pair.principle);
					ps.setString(2, "");
					ps.executeUpdate();
					genesLoaded++;
				}
				if ( !comparisonExists) {
					ps.setString(1, pair.comparison);
					ps.setString(2, "");
					ps.executeUpdate();
					genesLoaded++;
				}
				
			}
			
			//System.out.println("Seeded pair " + (i+1) + " of " + pairs.size() + " - " + new DecimalFormat("0.0").format((i+1.0)/pairs.size()*100) + "%");
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
						pairs.add(new InputPair(split[0].trim(), split[1].trim()));
					}
				}
			}
			in.close();
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}

}
