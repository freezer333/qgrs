package qgrs.compute;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jdom.Element;

import qgrs.data.AlignmentRecord;
import qgrs.data.BaseSymbol;
import qgrs.data.GeneSequence;
import qgrs.data.Range;

/*
 * Represents a pair consisting of THE principle and one of the comparisons. Could make a pair
 * of any two GeneSequence's but the private variables are named in accordance with the usage
 * in our program.
 */


public class GeneSequencePair {


	//General Alignment score represents the score after the two sequences have been aligned
	//(with a GeneralAligner, not a GQuadruplexAligner)
	
	private GeneSequence principle;
	private GeneSequence comparison;
	private AlignmentRecord alignmentRecord;
	
	
	
	
	
	
		
	public AlignmentRecord getAlignmentRecord() {
		this.alignmentRecord.setSimilarityPercentage(this.getSimilarityPercentage());
		this.alignmentRecord.setSimilarityScore(this.getSimilarityScore());
		return alignmentRecord;
	}


	public void setAlignmentRecord(AlignmentRecord alignmentRecord) {
		this.alignmentRecord = alignmentRecord;
	}


	public GeneSequencePair(GeneSequence a, GeneSequence b) {
		this.principle = a;
		this.comparison = b;
		this.alignmentRecord = new AlignmentRecord(a.getAccessionNumber(), b.getAccessionNumber());
	}
	
	
	public GeneSequencePair(GeneSequence a, GeneSequence b, Element xml) {
		this.principle = a;
		this.comparison = b;
		this.alignmentRecord = new AlignmentRecord(xml);
	}
	
	public GeneSequencePair(GeneSequence a, GeneSequence b, ResultSet rs) throws Exception {
		this.principle = a;
		this.comparison = b;
		this.alignmentRecord = new AlignmentRecord(rs);
		
	}
	


	public String getId() {
		return this.alignmentRecord.getId();
	}

	

	public Date getDateAligned() {
		return this.alignmentRecord.getDateAligned();
	}

	public void setDateAligned(Date dataAligned) {
		this.alignmentRecord.setDateAligned(dataAligned);
	}

	public String getAlignmentBuildKey() {
		return alignmentRecord.getAlignmentBuildKey();
	}

	public void setAlignmentBuildKey(String alignmentBuildKey) {
		this.alignmentRecord.setAlignmentBuildKey(alignmentBuildKey);
	}

	public int getSimilarityScore() {
		return this.alignmentRecord.getSimilarityScore();
	}

	public void setSimilarityScore(int similarityScore) {
		this.alignmentRecord.setSimilarityScore(similarityScore);
	}

	public void setSimilarityPercentage(float similarityPercentage) {
		this.alignmentRecord.setSimilarityPercentage(similarityPercentage);
	}
	public float getSimilarityPercentage() {
		// This is a costly computation, cache the results.
		if ( this.alignmentRecord.getSimilarityPercentage() < 0 ) {
			// Need to find the first and last gapped index that has bases (not gap) in both sequence
			int first = -1;
			int last = -1;
			float matches = 0;
			int maxIndex = Math.max(this.principle.getBases().size(), this.comparison.getBases().size());
			for ( int i = 0; i < maxIndex; i++ ) {
				if ( this.principle.getBases().size() > i && this.principle.getBases().get(i).getSymbol() != BaseSymbol.Gap &&
						this.comparison.getBases().size() > i && this.comparison.getBases().get(i).getSymbol() != BaseSymbol.Gap) {
					
					if ( first < 0 ) {
						first = i;
					}
					
					last = i;

					if ( this.principle.getBases().get(i).getSymbol() == this.comparison.getBases().get(i).getSymbol()) {
						matches++;
					}
				}
			}
			float smallest = Math.min(this.principle.getSequenceLength(), this.comparison.getSequenceLength());
			this.setSimilarityPercentage(matches / smallest);
		}
		return this.alignmentRecord.getSimilarityPercentage();
	}

	

	

	public GeneSequence getPrinciple() {
		return principle;
	}

	
	public GeneSequence getComparison() {
		return comparison;
	}


}
