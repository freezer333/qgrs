package qgrs.data.mongo.primitives.jongo;

import java.util.Collection;
import java.util.LinkedList;


public class G4{
	
	String g4Id;
	int tetrad1;
	int tetrad2;
	int tetrad3;
	int tetrad4;
	int loop1Length;
	int loop2Length;
	int loop3Length;
	int totalLength;
	int numTetrads;
	int score;
	boolean in5Prime;
	boolean in3Prime;
	boolean inCds;
	String sequenceSlice;
	String geneAccessionNumber;
	
	int distanceToStartCodon;
	int distanceToStopCodon;
	
	Collection <G4H> conservedG4s;
	Collection <G4> overlappingMotifs;
	Collection <DistanceFromPolyA> distancesFromPolyASignals;
	
	public G4() {
		super();
		this.conservedG4s = new LinkedList<G4H>();
		this.overlappingMotifs = new LinkedList<G4>();
		this.distancesFromPolyASignals = new LinkedList<DistanceFromPolyA>();
	}

	public String getG4Id() {
		return g4Id;
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

	public int getDistanceToStartCodon() {
		return distanceToStartCodon;
	}

	public void setDistanceToStartCodon(int distanceToStartCodon) {
		this.distanceToStartCodon = distanceToStartCodon;
	}

	public int getDistanceToStopCodon() {
		return distanceToStopCodon;
	}

	public void setDistanceToStopCodon(int distanceToEndCodon) {
		this.distanceToStopCodon = distanceToEndCodon;
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

	public boolean isIn5Prime() {
		return in5Prime;
	}

	public void setIn5Prime(boolean in5Prime) {
		this.in5Prime = in5Prime;
	}

	public boolean isIn3Prime() {
		return in3Prime;
	}

	public void setIn3Prime(boolean in3Prime) {
		this.in3Prime = in3Prime;
	}

	public boolean isInCds() {
		return inCds;
	}

	public void setInCds(boolean inCds) {
		this.inCds = inCds;
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

	public Collection<G4H> getConservedG4s() {
		return conservedG4s;
	}

	public void setConservedG4s(Collection<G4H> conservedG4s) {
		this.conservedG4s = conservedG4s;
	}

	public Collection<G4> getOverlappingMotifs() {
		return overlappingMotifs;
	}

	public void setOverlappingMotifs(Collection<G4> overlappingMotifs) {
		this.overlappingMotifs = overlappingMotifs;
	}

	public Collection<DistanceFromPolyA> getDistancesFromPolyASignals() {
		return distancesFromPolyASignals;
	}

	public void setDistancesFromPolyASignals(
			Collection<DistanceFromPolyA> distancesFromPolyASignals) {
		this.distancesFromPolyASignals = distancesFromPolyASignals;
	}
	
	

	
}
