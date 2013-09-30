package qgrs.data.mongo.primitives.jongo;


public class G4H{

	MRNA mrna;
	double alignmentPercentage;
	G4 g4;
	String recordId;
	double overlapScore;
	double tetradScore;
	double avgLoopScore;
	double totalLengthScore;
	double overallScore;
	
	
	public G4H() {
		super();
	}


	public MRNA getMrna() {
		return mrna;
	}


	public void setMrna(MRNA mrna) {
		this.mrna = mrna;
	}


	public double getAlignmentPercentage() {
		return alignmentPercentage;
	}


	public void setAlignmentPercentage(double alignmentPercentage) {
		this.alignmentPercentage = alignmentPercentage;
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


	public void setRecordId(String recordId) {
		this.recordId = recordId;
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


	public double getOverallScore() {
		return overallScore;
	}


	public void setOverallScore(double overallScore) {
		this.overallScore = overallScore;
	}
	
	
		
	
	
	
}
