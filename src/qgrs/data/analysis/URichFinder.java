package qgrs.data.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class URichFinder {
	String basePairs;

	public URichFinder(String bp) {
		basePairs = bp;
	}

	
	public URich getUs(int score) {
		URich bestU = null;
		int maxFound = -1;

		int length = basePairs.length();

		// checks if each char in the string is the
		// beginning of a Urich sequence
		for (int i = 0; i < (length - 5); i++) {
			// creates a hexamer
			String tmp = basePairs.substring(i, (i + 5));
			int s = findUHex(tmp);
			if (s >= score) // amount of Us is sufficient
			{
				if (s > maxFound) {
					bestU = new URich(i, s, tmp);
				}
			}
		}

		return bestU;
	}
	
	

	public Collection<URich> getAll(int score) {
		ArrayList<URich> retval = new ArrayList<URich>();
		int length = basePairs.length();
		for (int i = 0; i < (length - 5); i++) {
			// creates a hexamer
			String tmp = basePairs.substring(i, (i + 5));
			int s = findUHex(tmp);
			if (s >= score) // amount of Us is sufficient
			{
				retval.add(new URich(i, s, tmp));
			}
		}
		
		return cluster(retval);
		
	}
	
	Collection<URich> cluster(ArrayList<URich> originals) {
		Collection<URich> retval = new ArrayList<URich>();
		int lastEnd = -1;
		for ( int i = 0; i < originals.size(); i++ ) {
			URich u = originals.get(i);
			if ( u.distanceFromPolyASite > lastEnd ) {
				retval.add(u);
				// this is not an overlapped...
			}
			lastEnd = u.distanceFromPolyASite+5;
		}
		return retval;
	}

	// returns the amount of U's in a hexamer
	// for s to be a hexamer it must have 5 chars
	private int findUHex(String s) {
		int u = 0;

		for (int i = 0; i < 5; i++) {
			if (s.charAt(i) == 'U')
				u++;
		}

		return u;
	}

}
