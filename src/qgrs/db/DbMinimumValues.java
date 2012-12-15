package qgrs.db;

public class DbMinimumValues {

	final int minGScore = 35;
	final float minHomology = 0.90f;
	
	
	public boolean acceptableGScore(int score) {
		return score >= minGScore;
	}
	public boolean acceptableQgrsHomology(double score) {
		return score >= minHomology;
	}
	
	
}
