package qgrs.utils.db.populate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import qgrs.db.XmlWritePostCache;
import qgrs.input.AccessionNumberInputProvider;
import qgrs.job.AlignmentJob;
import qgrs.utils.db.InputPair;

public class SeedStage3 {
	
	static int complete = 0;
	static int errors = 0;
	
	/**
	 * This program performs the alignment and sending of each pair to the server. 
	 *
	 */
	public static void main(String[] args) throws Exception {
		
		SeedStage2.main(null);
		
		List<InputPair> pairs = new LinkedList<InputPair>();
		// Get full list of pairs whose status is not COMPLETE, principle and comparison in species set.
		Connection conn = SeedUtils.getConnection();
		// Select all genes, get species and populate.
		String q = "SELECT * FROM SEED WHERE Status <> 'Complete'";
		ResultSet rs = conn.prepareStatement(q).executeQuery();
		while ( rs.next() ) {
			String principle = rs.getString("PrincipleId");
			String comparison = rs.getString("ComparisonId");
			String pSpecies = getSpecies(conn, principle);
			String cSpecies = getSpecies(conn, comparison);
			if ( SeedUtils.checkSpecies(pSpecies, SeedUtils.PrincipleSpeciesSet) &&
					SeedUtils.checkSpecies(cSpecies, SeedUtils.ComparisonSpeciesSet)) {
				InputPair pair = new InputPair(principle, comparison);
				pairs.add(pair);
			}
		}
		
		System.out.println("\tComputing " + pairs.size() + " homologies to send to server");
		for ( InputPair pair : pairs) {
			computeAndSend(pair, conn);
		}
		System.out.println("Seeding Stage 3 Completed Successfully - " + complete + " computed, " + errors + " errors.");
		
	}
	
	public static String getSpecies(Connection conn, String id ) throws SQLException{
		String q = "SELECT Species FROM GENE WHERE ID = ?";
		PreparedStatement ps = conn.prepareStatement(q);
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		while ( rs.next() ) {
			return rs.getString(1);	
		}
		return "";
	}
	
	
	public static void computeAndSend(InputPair pair, Connection conn) throws SQLException {
		String q = "UPDATE SEED SET Status='Complete' WHERE PrincipleId=? AND ComparisonId=?";
		
		try {
			runNextAlignment(new AccessionNumberInputProvider(pair.principle, pair.comparison));
			complete++;
			Thread.sleep(2000);
		}
		catch (Throwable t) {
			System.out.println("Error:  " + pair.principle + " x " + pair.comparison) ;
			t.printStackTrace();
			q = "UPDATE SEED SET Status='Failed' WHERE PrincipleId=? AND ComparisonId=?";
			errors++;
		} 
		
		PreparedStatement ps = conn.prepareStatement(q);
		ps.setString(1, pair.principle);
		ps.setString(2, pair.comparison);
		ps.executeUpdate();
	}
	
	private static void runNextAlignment(AccessionNumberInputProvider inputProvider) throws Exception {
		AlignmentJob job = new AlignmentJob(inputProvider, null, new XmlWritePostCache ( SeedUtils.servername, "qgrs", SeedUtils.serverport));
		job.runJob();
	}
}
