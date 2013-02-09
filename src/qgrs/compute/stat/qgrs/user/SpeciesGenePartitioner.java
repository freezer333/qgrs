package qgrs.compute.stat.qgrs.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.GenePartitioner;

public class SpeciesGenePartitioner implements GenePartitioner {

	/**
	 * This paritions the gene set by species
	 */
	
	
	@Override
	public HashSet<GenePartition> partition(Connection c) {
		HashSet<GenePartition> partitons = new HashSet<GenePartition>();
		
		Collection<String> speciesList = getSpecies(c);
		
		for (String species : speciesList) {
			partitons.add(getPartition(c, species));
		}
		return partitons;
	}
	
	Collection<String> getSpecies(Connection c) {
		Collection<String> list = new LinkedList<String>();
		System.out.println("WARNING:  Species partitioner in debug mode (limit is set to 100 genes / species");
		String q = "SELECT DISTINCT species FROM GENE LIMIT 100";
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			while ( rs.next() ) {
				list.add(rs.getString("species"));
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	private GenePartition getPartition(Connection c, String species) {
		String q = "SELECT accessionNumber FROM GENE where species='" + species + "'";
		GenePartition p = new GenePartition(species);
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			while ( rs.next() ) {
				p.ids.add(rs.getString("accessionNumber"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	
	
	

}
