package qgrs.data.analysis;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class CountingSet {

	public MrnaFilter mrnaFilter;
	public 	G4Filter g4Filter;
	public G4Filter conserved;
	public MrnaSequenceFilter gcFilter;
	
	public double totalMrna = 0;
	public double totalG4 = 0;
	public double totalConservedG4 = 0;
	
	public DescriptiveStatistics pop_statistics = new DescriptiveStatistics();
	
	
	public CountingSet(MrnaFilter mrnaFilter, G4Filter g4Filter,
			G4Filter conserved) {
		super();
		this.mrnaFilter = mrnaFilter;
		this.g4Filter = g4Filter;
		this.conserved = conserved;
	}
	
	public CountingSet(MrnaFilter mrnaFilter, G4Filter g4Filter,
			G4Filter conserved, MrnaSequenceFilter gc) {
		super();
		this.mrnaFilter = mrnaFilter;
		this.g4Filter = g4Filter;
		this.conserved = conserved;
		this.gcFilter = gc;
	}
	
	public double meanConserved() {
		return this.totalConservedG4 / this.totalMrna;
	}

	public double zScore() {
		double num = this.totalConservedG4 / this.totalMrna - pop_statistics.getMean();
		double den = this.pop_statistics.getStandardDeviation();
		return num / den;
	}
	
	public boolean isSignificant() {
		if ( zScore() > 0 ) return zScore() > 1.96;
		else return zScore() < -1.96;
	}

	public MrnaFilter getMrnaFilter() {
		return mrnaFilter;
	}


	public G4Filter getG4Filter() {
		return g4Filter;
	}


	public G4Filter getConserved() {
		return conserved;
	}
	
	
	
}
