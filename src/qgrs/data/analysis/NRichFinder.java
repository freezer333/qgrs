package qgrs.data.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class NRichFinder {
	String basePairs;
	char n;

	public NRichFinder(char n, String bp) {
		this.n = Character.toUpperCase(n);
		basePairs = bp;
	}

	
	public NRich getUs(int score) {
		NRich bestU = null;
		int maxFound = -1;

		int length = basePairs.length();

		// checks if each char in the string is the
		// beginning of a Nrich sequence
		for (int i = 0; i < (length - 5); i++) {
			// creates a hexamer
			String tmp = basePairs.substring(i, (i + 5));
			int s = findHex(tmp);
			if (s >= score) // amount of Us is sufficient
			{
				if (s > maxFound) {
					bestU = new NRich(n, i, s, tmp);
				}
			}
		}

		return bestU;
	}
	
	

	public Collection<NRich> getAll(int score) {
		ArrayList<NRich> retval = new ArrayList<NRich>();
		int length = basePairs.length();
		for (int i = 0; i < (length - 5); i++) {
			// creates a hexamer
			String tmp = basePairs.substring(i, (i + 5));
			int s = findHex(tmp);
			if (s >= score) // amount of Us is sufficient
			{
				retval.add(new NRich(n, i, s, tmp));
			}
		}
		
		return cluster(retval);
		
	}
	
	Collection<NRich> cluster(ArrayList<NRich> originals) {
		Collection<NRich> retval = new ArrayList<NRich>();
		int lastEnd = -1;
		for ( int i = 0; i < originals.size(); i++ ) {
			NRich u = originals.get(i);
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
	private int findHex(String s) {
		int n_found = 0;

		for (int i = 0; i < 5; i++) {
			if (s.charAt(i) == n)
				n_found++;
		}

		return n_found;
	}

}
