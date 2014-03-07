package qgrs.data.analysis;

public class NRich {

	public char n;
	public int distanceFromPolyASite;
	public int numUs;
	public int startNt;
	public String accessionNumber;
	public int polyASiteNumber;
	public String sequence; 
	
	public NRich() {
	
	}
	
	// this needs to be cleaned up - we've got +5 everywhere!  The +5 is because we are trying
	// to only count starting from 5 nts downstead of polyA site.
	// need to implement a better solution to this all.
	public NRich(char n, int distance, int numUs, String  slice) {
		this.n = n;
		this.distanceFromPolyASite = distance + 5;
		this.numUs = numUs;
		this.startNt = -1;
		this.sequence = slice;
	}
	
	
	
	
	
}

