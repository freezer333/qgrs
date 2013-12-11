package qgrs.data.analysis;

public class URich {

	public int distanceFromPolyASite;
	public int numUs;
	public int startNt;
	public String accessionNumber;
	public int polyASiteNumber;
	public String sequence; 
	
	public URich() {
	
	}
	public URich(int distance, int numUs, String  slice) {
		this.distanceFromPolyASite = distance;
		this.numUs = numUs;
		this.startNt = -1;
		this.sequence = slice;
	}
	
	
}
