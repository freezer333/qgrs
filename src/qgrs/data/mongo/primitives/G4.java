package qgrs.data.mongo.primitives;

import java.util.ArrayList;

import com.mongodb.BasicDBObject;


public class G4 extends BasicDBObject{
	
	public G4() {
		super();
		this.setConservedG4s(new ArrayList<G4H>());
	}

	public String getG4Id() {
		return getString("g4Id");
	}

	

	public void setG4Id(String g4Id) {
		this.put("g4Id", g4Id);
	}

	public int getTetrad1() {
		return getInt("tetrad1");
	}

	public void setTetrad1(int tetrad1) {
		this.put("tetrad1", tetrad1);
	}

	public int getTetrad2() {
		return getInt("tetrad2");
	}

	public void setTetrad2(int tetrad2) {
		this.put("tetrad2", tetrad2);
	}

	public int getTetrad3() {
		return getInt("tetrad3");
	}

	public void setTetrad3(int tetrad3) {
		this.put("tetrad3",  tetrad3);
	}

	public int getTetrad4() {
		return getInt("tetrad4");
	}

	public void setTetrad4(int tetrad4) {
		this.put("tetrad4", tetrad4);
	}

	public int getLoop1Length() {
		return getInt("loop1Length");
	}

	public void setLoop1Length(int loop1Length) {
		this.put("loop1Length", loop1Length);
	}

	public int getLoop2Length() {
		return getInt("loop2Length");
	}

	public void setLoop2Length(int loop2Length) {
		this.put("loop2Length", loop2Length);
	}

	public int getLoop3Length() {
		return getInt("loop3Length");
	}

	public void setLoop3Length(int loop3Length) {
		this.put("loop3Length", loop3Length);
	}

	public int getTotalLength() {
		return getInt("totalLength");
	}

	public void setTotalLength(int totalLength) {
		this.put("totalLength", totalLength);
	}

	public int getNumTetrads() {
		return getInt("numTetrads");
	}

	public void setNumTetrads(int numTetrads) {
		this.put("numTetrads", numTetrads);
	}

	public int getScore() {
		return getInt("score");
	}

	public void setScore(int score) {
		this.put("score", score);
	}

	public int getDistanceFromPolyASignal() {
		return getInt("distanceFromPolyASignal");
	}

	public void setDistanceFromPolyASignal(int distanceFromPolyASignal) {
		this.put("distanceFromPolyASignal", distanceFromPolyASignal);
	}

	public boolean isIn5Prime() {
		return getBoolean("in5Prime");
	}

	public void setIn5Prime(boolean in5Prime) {
		this.put("in5Prime", in5Prime);
	}

	public boolean isInCds() {
		return getBoolean("inCds");
	}

	public void setInCds(boolean inCds) {
		this.put("inCds", inCds);
	}

	public boolean isIn3Prime() {
		return getBoolean("in3Prime");
	}

	public void setIn3Prime(boolean in3Prime) {
		this.put("in3Prime", in3Prime);
	}

	public String getSequenceSlice() {
		return getString("sequenceSlice");
	}

	public void setSequenceSlice(String sequenceSlice) {
		this.put("sequenceSlice", sequenceSlice);
	}

	public String getGeneAccessionNumber() {
		return getString("geneAccessionNumber");
	}

	public void setGeneAccessionNumber(String geneAccessionNumber) {
		this.put("geneAccessionNumber", geneAccessionNumber);
	}

	public ArrayList<G4H> getConservedG4s() {
		return (ArrayList<G4H>) get("conservedG4s");
	}

	public void setConservedG4s(ArrayList<G4H> conservedG4s) {
		this.put("conservedG4s", conservedG4s);
	}
	
	
	
	
	
}
