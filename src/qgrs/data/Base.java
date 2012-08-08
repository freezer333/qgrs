package qgrs.data;
/* 
 * Represents a single nucleotide base
 * */

import java.io.Serializable;


public class Base implements Serializable{

	private BaseSymbol symbol;
	private int indexWithoutGaps;
	private int indexWithGaps;
	
	public int getIndexWithoutGaps() {
		return indexWithoutGaps;
	}
	public void setIndexWithoutGaps(int index) {
		this.indexWithoutGaps = index;
	}
	public int getIndexWithGaps() {
		return indexWithGaps;
	}
	public void setIndexWithGaps(int indexWithGaps) {
		this.indexWithGaps = indexWithGaps;
	}

	public Base() {
		
	}
	public Base(BaseSymbol symbol) {
		this.symbol = symbol;
	}
	
	public BaseSymbol getSymbol() {
		return symbol;
	}

	public void setSymbol(BaseSymbol symbol) {
		this.symbol = symbol;
	}
	
	
}
