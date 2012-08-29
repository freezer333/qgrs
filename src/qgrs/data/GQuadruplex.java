package qgrs.data;
/*
 * Represents a QGRS along with its type and score
 */

import java.io.Serializable;
import java.sql.ResultSet;

import org.jdom.Element;


public class GQuadruplex implements Serializable{
	public final static int MINIMUM_SCORE = 13;
	public final static int MINIMUM_TETRAD = 2; 
	private Base start;
	private Base tetrad2Start;
	private Base tetrad3Start;
	private Base tetrad4Start;
	private Base end = null;
	
	
	private int numTetrads;
	private int length;
	private int score;
	
	private int loop1Length = -1;
	private int loop2Length = -1;
	private int loop3Length = -1;
	private float tetradAdjustmentScore = -1;
	private String sequenceSlice;
	private String id;
	
	private GeneSequence sequence;
	
	
	public GQuadruplex(GeneSequence sequence, int number){
		this.sequence = sequence;
		this.id = this.sequence.getAccessionNumber() + "." + number;
		
	}
	
	// This should only be used to create transfer objects for inserting
	// into the database, as sequence information is not present.
	public GQuadruplex(Element xml) {
		
	}
	
	public GQuadruplex(GQuadruplexRecord r, GeneSequence ungappedSequence){
		try {
			this.id = r.getId();
			this.sequence = ungappedSequence;
			this.start = this.sequence.getBases().get(r.getTetrad1());
			this.tetrad2Start = this.sequence.getBases().get(r.getTetrad2());
			this.tetrad3Start = this.sequence.getBases().get(r.getTetrad3());
			this.tetrad4Start = this.sequence.getBases().get(r.getTetrad4());
			this.loop1Length = r.getLoop1Length();
			this.loop2Length = r.getLoop2Length();
			this.loop3Length = r.getLoop3Length();
			this.length = r.getTotalLength();
			this.score = r.getScore();
			this.numTetrads = r.getNumTetrads();
		}
		catch ( Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public GQuadruplexRecord getRecord() {
		GQuadruplexRecord gq = new GQuadruplexRecord();
		gq.setId(this.getId());
		gq.setGeneAccessionNumber(this.sequence.getAccessionNumber());
		gq.setTetrad1(this.getStart().getIndexWithoutGaps());
		gq.setTetrad2(this.getTetrad2Start().getIndexWithoutGaps());
		gq.setTetrad3(this.getTetrad3Start().getIndexWithoutGaps());
		gq.setTetrad4(this.getTetrad4Start().getIndexWithoutGaps());
		gq.setLoop1Length(this.getLoop1Length());
		gq.setLoop2Length(this.getLoop2Length());
		gq.setLoop3Length(this.getLoop3Length());
		gq.setTotalLength(this.getLength());
		gq.setNumTetrads(this.getNumTetrads());
		gq.setScore(this.getScore());
		gq.setSequenceSlice(this.getSequenceSlice());
		gq.setIn5Prime(this.getRegion().contains("5'"));
		gq.setInCds(this.getRegion().contains("CDS"));
		gq.setIn3Prime(this.getRegion().contains("3'"));
		gq.applyAssertion();
		int minDistance = Integer.MAX_VALUE;
		for ( Range r : this.sequence.getPolyASignals() ) {
			int distance = Math.abs(this.start.getIndexWithoutGaps() - r.getStart());
			if ( distance < minDistance ) {
				minDistance = distance;
			}
		}
		gq.setDistanceFromPolyASignal(minDistance);
		return gq;
	}
	
	
	
	
	public boolean in5UTR() {
		return this.getRegion().contains("5'");
	}
	public boolean inCDS() {
		return this.getRegion().contains("CDS");
	}
	public boolean in3UTR() {
		return this.getRegion().contains("3'");
	}
	

	public String getSequenceSlice() {
		if ( this.sequenceSlice == null ) {
			StringBuilder b = new StringBuilder();
			int start = this.getSequence().getBases().indexOf(this.getStart());
			int symbols = 0;
			int i = start;
			while (symbols < length ) {
				if ( sequence.getBases().get(i).getSymbol() != BaseSymbol.Gap) {
					b.append(sequence.getBases().get(i).getSymbol().toString());
					symbols++;
				}
				i++;
			}
			
			this.sequenceSlice = b.toString();
		}
		return this.sequenceSlice;
	}
	
	public GeneSequence getSequence() {
		return sequence;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLoop1Length() {
		if ( loop1Length < 0 ) {
			loop1Length = findDistanceBetweenBases(this.getLoop1Start(), this.getTetrad2Start());
		}
		return loop1Length;
	}
	public int getLoop2Length() {
		if ( loop2Length < 0 ) {
			loop2Length = findDistanceBetweenBases(this.getLoop2Start(), this.getTetrad3Start());
		}
		return loop2Length;
	}
	public int getLoop3Length() {
		if ( loop3Length < 0 ) {
			loop3Length = findDistanceBetweenBases(this.getLoop3Start(), this.getTetrad4Start());
		}
		return loop3Length;
	}
	
	int findDistanceBetweenBases(Base start, Base end){
		return end.getIndexWithoutGaps() - start.getIndexWithoutGaps();
	}
	public boolean isAcceptable() {
		if ( this.getScore() < MINIMUM_SCORE ) return false;
		if ( this.getTetrads() == 2 && this.getLength() > 30 ) return false;
		float loop1 = findDistanceBetweenBases(this.getLoop1Start(), this.getTetrad2Start());
		float loop2 = findDistanceBetweenBases(this.getLoop2Start(), this.getTetrad3Start());
		float loop3 = findDistanceBetweenBases(this.getLoop3Start(), this.getTetrad4Start());
		int count = 0;
		if ( loop1 < 1 ) count++;
		if ( loop2 < 1 ) count++;
		if ( loop3 < 1 ) count++;
		return count < 2;
	}
	
	public void refreshLinks() {
		this.end = null;
	}
	public Base getEnd() {
		if ( end == null ) {
			int index1 = tetrad4Start.getIndexWithGaps();
			for ( int numGs = 0; numGs < numTetrads; index1++ ){
				if (sequence.getBases().get(index1).getSymbol() == BaseSymbol.G) {
					numGs++;
				}
			}
			end = sequence.getBases().get(index1 - 1);
		}
		return end;
	}
	
	public Base getLoop1Start() {
		return sequence.getBases().get(start.getIndexWithGaps() + numTetrads);
	}

	public Base getLoop2Start() {
		return sequence.getBases().get(tetrad2Start.getIndexWithGaps() + numTetrads);
	}

	public Base getLoop3Start() {
		return sequence.getBases().get(tetrad3Start.getIndexWithGaps() + numTetrads);
	}

	public String getRegion(){
		int start = this.getStart().getIndexWithoutGaps();
		int end = this.getEnd().getIndexWithoutGaps();
		
		if (sequence.isDirectInput() ) {
			return "N/A";
		}
		Range cds = sequence.getCds();
		if ( cds.getStart() < 0 || cds.getEnd() < 0 ) return "N/A";
		
		if ( start <cds.getStart() && end <= cds.getStart()) {
			return "5' UTR";
		}
		
		if (start < cds.getStart() && end > cds.getStart()) {
			return "5' UTR/CDS";
		}
		
		if ((start > cds.getStart())&& start < (cds.getEnd()) && (end > cds.getEnd())) {
			return "CDS/3' UTR";
		}
		
		if (start >= cds.getEnd()&& end > cds.getEnd()) {
			return "3' UTR";
		}
		
		return "CDS";
	}
	
	public void setStart(Base firstGGroupBeginning) {
		this.start = firstGGroupBeginning;
	}
	public Base getStart() {
		return start;
	}
	
	
	public String getGListWithGaps(){
		String retval = "";
		for ( int i = 0; i < this.getTetrads(); i++ ) {
			retval += (String.valueOf((this.getStart().getIndexWithGaps() + i)) + "-");
		}
		for ( int i = 0; i < this.getTetrads(); i++ ) {
			retval += (String.valueOf((this.getTetrad2Start().getIndexWithGaps() + i)) + "-");
		}
		for ( int i = 0; i < this.getTetrads(); i++ ) {
			retval += (String.valueOf((this.getTetrad3Start().getIndexWithGaps() + i)) + "-");
		}
		for ( int i = 0; i < this.getTetrads(); i++ ) {
			retval += (String.valueOf((this.getTetrad4Start().getIndexWithGaps() + i)) + "-");
		}
		return retval.substring(0, retval.length());
	}
	
	public void setTetrad2Start(Base secondGGroupBeginning) {
		this.tetrad2Start = secondGGroupBeginning;
	}
	public Base getTetrad2Start() {
		return tetrad2Start;
	}
	
	
	public void setTetrad3Start(Base thirdGGroupBeginning) {
		this.tetrad3Start = thirdGGroupBeginning;
	}
	public Base getTetrad3Start() {
		return tetrad3Start;
	}
	
	
	public void setTetrad4Start(Base fourthGGroupBeginning) {
		this.tetrad4Start = fourthGGroupBeginning;
	}
	public Base getTetrad4Start() {
		return tetrad4Start;
	}
	
	
	public void setNumTetrads(int gGroupLength) {
		this.numTetrads = gGroupLength;
	}
	public int getNumTetrads() {
		return numTetrads;
	}
	
	
	public void setLength(int qGRSLength) {
		length = qGRSLength;
	}
	public int getLength() {
		return length;
	}
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getTetrads() {
		return numTetrads;
	}
	
	
	public float getTetradAdjustmentScore() {
		if ( this.tetradAdjustmentScore < 0 ) {
			tetradAdjustmentScore = 0;
			if (isAdjustable(sequence, this.getStart().getIndexWithGaps(), this.getTetrads())){
				tetradAdjustmentScore += 0.25;
			}
			if (isAdjustable(sequence, this.getTetrad2Start().getIndexWithGaps(), this.getTetrads())){
				tetradAdjustmentScore += 0.25;
			}	
			if (isAdjustable(sequence, this.getTetrad3Start().getIndexWithGaps(), this.getTetrads())){
				tetradAdjustmentScore += 0.25;
			}
			if (isAdjustable(sequence, this.getTetrad4Start().getIndexWithGaps(), this.getTetrads())){
				tetradAdjustmentScore += 0.25;
			}
		}
		return tetradAdjustmentScore;
	}

	boolean isAdjustable(GeneSequence sequence, int start, int tetrads) {
		int end = start; 
		int numFound = 0;
		while (numFound < tetrads){
			if (sequence.getBases().get(end).getSymbol() == BaseSymbol.G) {
				numFound++;
			}
			end++;
		}
		int test = start - 1;
 		while (test >= 0 && (sequence.getBases().get(test).getSymbol() == BaseSymbol.Gap || sequence.getBases().get(test).getSymbol() == BaseSymbol.G)) {
			if (sequence.getBases().get(test).getSymbol() == BaseSymbol.G){
				return true;
			}
			test--;
		}
		test = end;
		while (test < sequence.getBases().size()&& (sequence.getBases().get(test).getSymbol() == BaseSymbol.Gap || sequence.getBases().get(test).getSymbol() == BaseSymbol.G)) {
			if (sequence.getBases().get(test).getSymbol() == BaseSymbol.G){
				return true;
			}
			if (sequence.getBases().get(test).getSymbol() == BaseSymbol.Gap){
				return false;
			}
			test++;
		}
		return false;
	}


}


