package qgrs.data.analysis;

public class CountingSet {

	public MrnaFilter mrnaFilter;
	public 	G4Filter g4Filter;
	public G4Filter conserved;
	
	public double totalMrna = 0;
	public double totalG4 = 0;
	public double totalConservedG4 = 0;
	
	
	public CountingSet(MrnaFilter mrnaFilter, G4Filter g4Filter,
			G4Filter conserved) {
		super();
		this.mrnaFilter = mrnaFilter;
		this.g4Filter = g4Filter;
		this.conserved = conserved;
	}
	
	
	
}
