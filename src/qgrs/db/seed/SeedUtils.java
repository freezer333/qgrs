package qgrs.db.seed;


public class SeedUtils {

	public static final String InputPairDir = "seed_data/pairs/";
	
	public static final String [] PrincipleSpeciesSet = {"Homo sapiens"};
	public static final String [] ComparisonSpeciesSet = {"Pan troglodytes", "Canis lupus familiaris", "Danio rerio",  "Caenorhabditis elegans", "Kluyveromyces lactis"};//, "Bos taurus"};
	
	public static final String servername = "localhost";//"quadruplex.ramapo.edu";//
	public static final int serverport = 8080;//80;//;
	public static final String contextpath = "qgrs";//"qgrs2
	
	
	
	public static boolean checkSpecies(String species, String [] matchSet) {
		for ( String s : matchSet ) {
			if ( s.equalsIgnoreCase(species)) {
				return true;
			}
		}
		return false;
	}
	
}
