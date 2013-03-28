package qgrs.compute;

public class HomologyScoreResult {
	private double avgLoopScore;
	private double overlapScore;
	private double tetradScore;
	private double totalLengthScore;
	
	
	static final float OVERLAP_WEIGHT = (float) 0.65; 
	static final float TETRAD_WEIGHT = (float) 0.2; 
	static final float LOOP_WEIGHT = (float) 0.1; 
	static final float LENGTH_WEIGHT = (float) 0.05; 
	
	
	public double getAvgLoopScore() {
		return avgLoopScore;
	}
	public void setAvgLoopScore(double avgLoopScore) {
		this.avgLoopScore = avgLoopScore;
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
	public double getTotalLengthScore() {
		return totalLengthScore;
	}
	public void setTotalLengthScore(double totalLengthScore) {
		this.totalLengthScore = totalLengthScore;
	}
	public double getOverallScore() {
		return (OVERLAP_WEIGHT * overlapScore) + (TETRAD_WEIGHT * tetradScore) +
				(LOOP_WEIGHT * avgLoopScore) + (LENGTH_WEIGHT * totalLengthScore);
	}
	
	
}