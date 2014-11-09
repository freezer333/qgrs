package qgrs.data.analysis;

import java.util.LinkedList;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.G4;
import qgrs.data.mongo.primitives.jongo.G4H;
import qgrs.data.mongo.primitives.jongo.MRNA;

public class G4Filter {

	double min_h = 0;
	int tetradMin = 2;
	int tetradMax = Integer.MAX_VALUE;
	Range score2Range = new Range(13, Integer.MAX_VALUE);
	Range score3Range = new Range(17, Integer.MAX_VALUE);
	boolean mustBeIn3Prime = false;
	boolean mustBeInCds = false;
	boolean mustBeIn5Prime = false;
	boolean mustNotBeInCds = false;
	Region region;
	
	private LinkedList<String> homologList = new LinkedList<String>();
	
	
	public int getValidNtLength(MRNA mrna) {
		int retval = 0; 
		if ( this.region == Region.Any ) retval =  mrna.getSequenceLength();
		if ( this.region == Region.Cds ) retval =  mrna.getCds().getLength();
		if ( this.region == Region.FivePrime ) retval = mrna.getUtr5().getLength();
		if ( this.region == Region.ThreePrime ) retval = mrna.getUtr5().getLength();
		return retval == 0 ? 1 : retval;
	}
	
	
	
	
	public Range getScore2Range() {
		return score2Range;
	}
	public G4Filter setScore2Range(Range scoreRange) {
		this.score2Range = scoreRange;
		return this;
	}
	public Range getScore3Range() {
		return score3Range;
	}
	public G4Filter setScore3Range(Range scoreRange) {
		this.score3Range = scoreRange;
		return this;
	}
	public G4Filter setTetradRange(Range range) {
		this.tetradMin = range.getStart();
		this.tetradMax = range.getEnd();
		return this;
	}
	public int getTetradMin() {
		return tetradMin;
	}
	public void setTetradMin(int tetradMin) {
		this.tetradMin = tetradMin;
	}
	public int getTetradMax() {
		return tetradMax;
	}
	public void setTetradMax(int tetradMax) {
		this.tetradMax = tetradMax;
	}
	public G4Filter() {
	}
	public G4Filter (Region reg) {
		setRegion(reg);
	}
	public G4Filter (double minHomology, Region reg, int minTetrad) {
		setRegion(reg);
		setTetradMin(minTetrad);
		this.min_h = minHomology;
	}
	
	
	public void setRegion (Region reg) {
		mustBeInCds = mustBeIn3Prime = mustBeIn5Prime = mustNotBeInCds = false;
		if ( reg == Region.Cds ) mustBeInCds = true;
		if ( reg == Region.ThreePrime) mustBeIn3Prime = true;
		if ( reg == Region.FivePrime) mustBeIn5Prime = true;
		if ( reg == Region.NotCds) mustNotBeInCds = true;
		this.region = reg;
	}
	
	public String getRegionLabel() {
		if ( mustBeIn3Prime) return("3-UTR ");
		if ( mustBeInCds) return("CDS ");
		if ( mustBeIn5Prime) return("5-UTR ");
		if ( mustNotBeInCds) return("5' or 3'");
		return "Any";
	}
	public String getTetradLabel() {
		return "T=" + new Range(tetradMin, tetradMax).toString();
	}
	public String getHomologyLabel() {
		if (this.min_h > 0 ) return ("H>= " + this.min_h + " ");
		return "N/A";
	}
	
	@Override 
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if ( mustBeIn3Prime) sb.append("3-UTR ");
		if ( mustBeInCds) sb.append("CDS ");
		if ( mustBeIn5Prime) sb.append("5-UTR ");
		if ( mustNotBeInCds) sb.append("5' or 3'");
		sb.append("T"+this.tetradMin);
		if ( this.tetradMax < Integer.MAX_VALUE && this.tetradMax != this.tetradMin ) { 
			sb.append(this.tetradMax + " "); 
		} 
		else if (this.tetradMax > this.tetradMin) {
			sb.append("+ "); 
		}
		if (this.min_h > 0 ) sb.append("H" + this.min_h + " ");
		if (this.homologList.size() > 0 )  {
			for ( String h : this.homologList ) {
				sb.append( h + " ");
			}
		}
		return sb.toString();
	}
	public G4Filter(double minHomology) {
		this.min_h = minHomology;
		
	}
	public G4Filter (double minHomology, Region reg) {
		this.min_h = minHomology;
		if ( reg == Region.Cds ) mustBeInCds = true;
		if ( reg == Region.ThreePrime) mustBeIn3Prime = true;
		if ( reg == Region.FivePrime) mustBeIn5Prime = true;
		setRegion(reg);
	}
	
	
	private boolean filterTetradAndScore(int tetrads, int score) {
		if ( ! new Range(this.tetradMin, this.tetradMax).contains(tetrads)) return false;
		if ( tetrads == 2) {
			if ( ! this.score2Range.contains(score)) return false;
		}
		else if ( ! this.score3Range.contains(score)) return false;
		
		return true;
	}
	
	int findLargestTetrad(G4 g4) {
		int largest = 0;
		for ( G4H h : g4.getConservedG4s() ) {
			if ( filterByHomolog(h)) {
				if ( h.getOverallAbsoluteScore() >= min_h) {
					if ( h.getG4().getNumTetrads() > largest ) largest = h.getG4().getNumTetrads();
				}
			}
		}
		return largest;
	}
	int findLargestScore(G4 g4) {
		int largest = 0;
		for ( G4H h : g4.getConservedG4s() ) {
			if ( filterByHomolog(h)) {
				if ( h.getOverallAbsoluteScore() >= min_h) {
					if ( h.getG4().getScore() > largest ) largest = h.getG4().getScore();
				}
			}
		}
		return largest;
	}
	
	
	public Region getRegion() {
		return region;
	}
	public boolean acceptable(MRNA mrna, G4 g4) {
		if ( mustBeInCds && !g4.isInCds() ) return false;
		if ( mustBeIn3Prime && !g4.isIn3Prime()) return false;
		if ( mustBeIn5Prime && !g4.isIn5Prime()) return false;
		if ( mustNotBeInCds && g4.isInCds()) return false;
		
		
		int applicableTetrad = g4.getNumTetrads();
		int applicableScore = g4.getScore();
		if ( this.min_h > 0) {
			int ht = findLargestTetrad(g4);
			if ( ht < applicableTetrad) {
				applicableTetrad = ht;
			}
		}
		if ( this.min_h > 0) {
			int ht = findLargestScore(g4);
			if ( ht < applicableScore) {
				applicableScore = ht;
			}
		}
		if ( !filterTetradAndScore(applicableTetrad, applicableScore)) return false;
		
		return true;
	}
	
	private boolean filterByHomolog(G4H h) {
		if ( this.homologList.size() < 1 ) return true;
		for ( String term : this.homologList ) {
			if ( h.getMrna().getSpecies().equalsIgnoreCase(term) ) return true;
		}
		return false;
	}
	
	public LinkedList<String> getHomologList() {
		return homologList;
	}
	public void setHomologList(LinkedList<String> homologList) {
		this.homologList = homologList;
	}
	
	
	
}
