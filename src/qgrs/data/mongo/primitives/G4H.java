package qgrs.data.mongo.primitives;

import com.mongodb.BasicDBObject;

public class G4H extends BasicDBObject{

	
	
	
	public G4H() {
		super();
	}
	
	
	


	public MRNA getMrna() {
		return (MRNA) get("mrna");
	}
	public void setMrna(MRNA mrna) {
		this.put("mrna", mrna);
	}
	public Alignment getAlignment() {
		return (Alignment)get("alignment");
	}
	public void setAlignment(Alignment alignment) {
		this.put("alignment", alignment);
	}
	public G4 getG4() {
		return (G4)get("G4");
	}
	public void setG4(G4 g4) {
		this.put("g4", g4);
	}
	public String getRecordId() {
		return getString("recordId");
	}
	public void setRecordId(String recordId) {
		this.put("recordId", recordId);
	}
	public double getOverlapScore() {
		return getDouble("overlapScore");
	}
	public void setOverlapScore(double overlapScore) {
		this.put("overlapScore", overlapScore);
	}
	public double getTetradScore() {
		return getDouble("tetradScore");
	}
	public void setTetradScore(double tetradScore) {
		this.put("tetradScore", tetradScore);
	}
	public double getAvgLoopScore() {
		return getDouble("avgLoopScore");
	}
	public void setAvgLoopScore(double avgLoopScore) {
		this.put("avgLoopScore", avgLoopScore);
	}
	public double getTotalLengthScore() {
		return getDouble("totalLengthScore");
	}
	public void setTotalLengthScore(double totalLengthScore) {
		this.put("totalLengthScore", totalLengthScore);
	}
	public double getOverallAbsoluteScore() {
		return getDouble("overallAbsoluteScore");
	}
	public void setOverallAbsoluteScore(double overallAbsoluteScore) {
		this.put("overallAbsoluteScore", overallAbsoluteScore);
	}
	
	
	
	
	
	
	
}
