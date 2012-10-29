package qgrs.utils.db.populate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.biojava.bio.BioException;
import org.biojava.bio.seq.db.IllegalIDException;
import org.biojavax.bio.db.ncbi.GenbankRichSequenceDB;
import org.biojavax.bio.seq.RichSequence;

import qgrs.db.GenbankRichSequenceTextDB;

public class SeedStage2 {

	static int speciesSaved = 0;
	static int errors = 0;
	
	static GenbankRichSequenceDB  ncbi = new GenbankRichSequenceTextDB();

	
	/**
	 * This program populates the comparison species column of the seeding table
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		SeedStage1.main(null);
		
		Connection conn = SeedUtils.getConnection();
		// Select all genes, get species and populate.
		String q = "SELECT COUNT(ID) FROM GENE WHERE Species=''";
		ResultSet rs = conn.prepareStatement(q).executeQuery();
		while ( rs.next() ) {
			System.out.println("\tApplying species data to " + rs.getInt(1) + " records.");
			
		}
		q = "SELECT ID FROM GENE WHERE Species=''";
		
		rs = conn.prepareStatement(q).executeQuery();
		while ( rs.next() ) {
			try {
				String id = rs.getString(1);
				String species = getSpecies(id);
				setSpecies(id, species, conn);
				
				// Must throttle to no abuse NCBI
				Thread.sleep(1000);
				
			}
			catch (Exception e) {
				errors++;
				e.printStackTrace();
			}
		}
		
		System.out.println("Seeding Stage 2 Completed Successfully - " + speciesSaved + " species added, " + errors + " errors.");
		
	}
	
	public static String getSpecies(String id) throws IllegalIDException, BioException {
		RichSequence rs = ncbi.getRichSequence(id);
		return rs.getTaxon().getDisplayName();
	}
	
	public static void setSpecies(String id, String species, Connection conn) throws SQLException {
		String q = "UPDATE GENE SET Species=? WHERE ID=?";
		PreparedStatement ps = conn.prepareStatement(q);
		ps.setString(1, species);
		ps.setString(2, id);
		ps.executeUpdate();
		speciesSaved++;
	}

}
