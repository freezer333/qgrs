package qgrs.data.mongo.primitives;

public class G4H {

	MRNA mrna;
	Alignment alignment;
	G4 g4;
	
	
	private String recordId;
	private double overlapScore;
	private double tetradScore;
	private double avgLoopScore;
	private double totalLengthScore;
	private double overallAbsoluteScore;
	private long _id;
	
	
	public G4H() {
		super();
	}
	
	
	public long get_id() {
		return _id;
	}


	public void set_id(long _id) {
		this._id = _id;
	}


	public MRNA getMrna() {
		return mrna;
	}
	public void setMrna(MRNA mrna) {
		this.mrna = mrna;
	}
	public Alignment getAlignment() {
		return alignment;
	}
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
	public G4 getG4() {
		return g4;
	}
	public void setG4(G4 g4) {
		this.g4 = g4;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String id) {
		this.recordId = id;
	}
	public double getOverlapScore() {
		return overlapScore;
	}
	public void setOverlapScore(double overlapScore) {
		this.overlapScore = overlapScore;
	}
	public double getTetradScore() {
		return tetradScore;
	}
	public void setTetradScore(double tetradScore) {
		this.tetradScore = tetradScore;
	}
	public double getAvgLoopScore() {
		return avgLoopScore;
	}
	public void setAvgLoopScore(double avgLoopScore) {
		this.avgLoopScore = avgLoopScore;
	}
	public double getTotalLengthScore() {
		return totalLengthScore;
	}
	public void setTotalLengthScore(double totalLengthScore) {
		this.totalLengthScore = totalLengthScore;
	}
	public double getOverallAbsoluteScore() {
		return overallAbsoluteScore;
	}
	public void setOverallAbsoluteScore(double overallAbsoluteScore) {
		this.overallAbsoluteScore = overallAbsoluteScore;
	}
	
	
	
	
	
	
	
}
