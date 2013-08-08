package qgrs.data.mongo.primitives;

import java.util.ArrayList;


public class G4 {
	private String g4Id;
	private int tetrad1;
	private int tetrad2;
	private int tetrad3;
	private int tetrad4;
	private int loop1Length;
	private int loop2Length;
	private int loop3Length;
	private int totalLength;
	private int numTetrads;
	private int score;
	private int distanceFromPolyASignal;
	private boolean in5Prime;
	private boolean inCds;
	private boolean in3Prime;
	private String sequenceSlice;
	private String geneAccessionNumber;

	private ArrayList<G4H> conservedG4s;
	private long _id;
	
	
	public G4() {
		super();
		conservedG4s = new ArrayList<G4H>();
	}

	public String getG4Id() {
		return g4Id;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public void setG4Id(String g4Id) {
		this.g4Id = g4Id;
	}

	public int getTetrad1() {
		return tetrad1;
	}

	public void setTetrad1(int tetrad1) {
		this.tetrad1 = tetrad1;
	}

	public int getTetrad2() {
		return tetrad2;
	}

	public void setTetrad2(int tetrad2) {
		this.tetrad2 = tetrad2;
	}

	public int getTetrad3() {
		return tetrad3;
	}

	public void setTetrad3(int tetrad3) {
		this.tetrad3 = tetrad3;
	}

	public int getTetrad4() {
		return tetrad4;
	}

	public void setTetrad4(int tetrad4) {
		this.tetrad4 = tetrad4;
	}

	public int getLoop1Length() {
		return loop1Length;
	}

	public void setLoop1Length(int loop1Length) {
		this.loop1Length = loop1Length;
	}

	public int getLoop2Length() {
		return loop2Length;
	}

	public void setLoop2Length(int loop2Length) {
		this.loop2Length = loop2Length;
	}

	public int getLoop3Length() {
		return loop3Length;
	}

	public void setLoop3Length(int loop3Length) {
		this.loop3Length = loop3Length;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}

	public int getNumTetrads() {
		return numTetrads;
	}

	public void setNumTetrads(int numTetrads) {
		this.numTetrads = numTetrads;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getDistanceFromPolyASignal() {
		return distanceFromPolyASignal;
	}

	public void setDistanceFromPolyASignal(int distanceFromPolyASignal) {
		this.distanceFromPolyASignal = distanceFromPolyASignal;
	}

	public boolean isIn5Prime() {
		return in5Prime;
	}

	public void setIn5Prime(boolean in5Prime) {
		this.in5Prime = in5Prime;
	}

	public boolean isInCds() {
		return inCds;
	}

	public void setInCds(boolean inCds) {
		this.inCds = inCds;
	}

	public boolean isIn3Prime() {
		return in3Prime;
	}

	public void setIn3Prime(boolean in3Prime) {
		this.in3Prime = in3Prime;
	}

	public String getSequenceSlice() {
		return sequenceSlice;
	}

	public void setSequenceSlice(String sequenceSlice) {
		this.sequenceSlice = sequenceSlice;
	}

	public String getGeneAccessionNumber() {
		return geneAccessionNumber;
	}

	public void setGeneAccessionNumber(String geneAccessionNumber) {
		this.geneAccessionNumber = geneAccessionNumber;
	}

	public ArrayList<G4H> getConservedG4s() {
		return conservedG4s;
	}

	public void setConservedG4s(ArrayList<G4H> conservedG4s) {
		this.conservedG4s = conservedG4s;
	}
	
	
	
	
}
