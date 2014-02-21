package qgrs.data.analysis;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class CountingSet {

	public MrnaFilter mrnaFilter;
	public 	G4Filter g4Filter;
	public G4Filter conserved;
	
	public double totalMrna = 0;
	public double totalG4 = 0;
	public double totalConservedG4 = 0;
	
	public double totalG4Per100nt = 0;
	public double totalConservedG4Per100nt = 0;
	
	
	
	public DescriptiveStatistics raw_pop_statistics = new DescriptiveStatistics();
	public DescriptiveStatistics normed_pop_statistics = new DescriptiveStatistics();
	
	
	public CountingSet(MrnaFilter mrnaFilter, G4Filter g4Filter,
			G4Filter conserved) {
		super();
		this.mrnaFilter = mrnaFilter;
		this.g4Filter = g4Filter;
		this.conserved = conserved;
	}
	
	public double meanConserved() {
		return this.totalConservedG4 / this.totalMrna;
	}
	
	public double meanConservedPer100nt() {
		return this.totalConservedG4Per100nt / this.totalMrna;
	}

	public double zScore() {
		double num = this.totalConservedG4 / this.totalMrna - raw_pop_statistics.getMean();
		double den = this.raw_pop_statistics.getStandardDeviation();
		return num / den;
	}
	
	public double zScoreNormalized() {
		double num = this.totalConservedG4Per100nt / this.totalMrna - normed_pop_statistics.getMean();
		double den = this.normed_pop_statistics.getStandardDeviation();
		return num / den;
	}
	
	public boolean isSignificant() {
		if ( zScore() > 0 ) return zScore() > 1.96;
		else return zScore() < -1.96;
	}
	
	public boolean isSignificantNormalized() {
		if ( zScoreNormalized() > 0 ) return zScoreNormalized() > 1.96;
		else return zScoreNormalized() < -1.96;
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
