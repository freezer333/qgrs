package qgrs.utils.db.populate;

import java.sql.Connection;


public class SeedStage0 {
	/**
	 * This program creates a fresh seeding table if it does not exist
	 */
	public static void main(String[] args) {
		Connection conn = null;
		conn = SeedUtils.getConnection();
		String q = "CREATE TABLE IF NOT EXISTS SEED (PrincipleId char(50), " +
				"ComparisonId char(50), " +
				"Status char(15))";
		SeedUtils.execute(conn, q);
		
		q = "CREATE TABLE IF NOT EXISTS GENE (ID char(50), " +
			"Species varchar(255))";
		SeedUtils.execute(conn, q);
		
		System.out.println("Seeding Stage 0 Completed Successfully - Seed and Gene tables exist and are ready.");
	}
}
