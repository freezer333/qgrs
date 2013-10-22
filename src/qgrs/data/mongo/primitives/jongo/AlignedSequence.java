package qgrs.data.mongo.primitives.jongo;

public class AlignedSequence {

	private String accessionNumber;
	private String giNumber;
	private String geneName;
	private String species;
	private String alignedSequence;
	private String symbol;
	
	
	public AlignedSequence() {}
	
	
	public String getSymbol() {
		return symbol;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public String getAccessionNumber() {
		return accessionNumber;
	}
	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}
	public String getGiNumber() {
		return giNumber;
	}
	public void setGiNumber(String giNumber) {
		this.giNumber = giNumber;
	}
	public String getGeneName() {
		return geneName;
	}
	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public String getAlignedSequence() {
		return alignedSequence;
	}
	public void setAlignedSequence(String alignedSequence) {
		this.alignedSequence = alignedSequence;
	}
	
	
	
}
