package qgrs.data;

/*
 * An enumeration of all valid symbols we can have for a nucleotide base, including a "gap".
 * Does not include special characters like "n" yet.
 */

import java.io.Serializable;

public enum BaseSymbol implements Serializable{
	A("A"), C("C"), T("T"), G("G"), Gap("-"), N("N");
	
	private final String strVal;
	
	BaseSymbol (String s) {
		strVal = s;
	}
	
	public String toString() {
		return strVal; 
	}
	
	public static BaseSymbol parseSymbol(char c){
		String s = Character.toString(c).toUpperCase();
		for ( BaseSymbol bs : BaseSymbol.values() ) {
			if ( bs.toString().equals(s)) return bs;
		}
		return BaseSymbol.N;
	}
}
