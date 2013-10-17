package qgrs.data.mongo.primitives.jongo;

public class Homolog {

	MRNA mrna;
	int alignmentPercentage;
	
	public Homolog() {
		super();
	}
	
	public MRNA getMrna() {
		return mrna;
	}
	public void setMrna(MRNA mrna) {
		this.mrna = mrna;
	}
	public int getAlignmentPercentage() {
		return alignmentPercentage;
	}
	public void setAlignmentPercentage(int alignmentPercentage) {
		this.alignmentPercentage = alignmentPercentage;
	}
	
	
}
