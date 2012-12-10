package qgrs.data.query;

public class QueryUtils {

	public static final String [] qgrs_h_regions_cols = {"P_in5UTR", "P_inCds", "P_in3UTR"};
	public static final String [] qgrs_regions_cols = {"IN5PRIME", "INCDS", "IN3PRIME"};
	
	public static String buildRegionConstraint(boolean in5Prime, boolean inCds, boolean in3Prime, String [] columns) {
		if ( !in5Prime && !inCds && !in3Prime ) {
			return "FALSE";
		}
		if (in5Prime && inCds && in3Prime ) {
			return "";
		}
		else {
			int count = 0;
			String retval = "(";
			if ( in5Prime ) {
				retval += (columns[0] + " = TRUE");
				count++;
			}
			if ( inCds ) {
				if ( count > 0 ) retval += " OR ";
				retval += (columns[1] + " = TRUE");
				count++;
			}
			if ( in3Prime ) {
				if ( count > 0 ) retval += " OR ";
				retval += (columns[2] + " = TRUE");
			}
			retval += ")";
			return retval;
		}
	}
}
