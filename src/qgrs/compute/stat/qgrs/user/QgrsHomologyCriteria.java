package qgrs.compute.stat.qgrs.user;

import qgrs.data.records.QgrsHomologyRecord;

public class QgrsHomologyCriteria {
	private final double minHomologScore;

	
	public QgrsHomologyCriteria() {
		this.minHomologScore = 0.95;
	}
	
	public QgrsHomologyCriteria(double minHomologyScore) {
		this.minHomologScore = minHomologyScore;
	}


	public double getMinHomologScore() {
		return minHomologScore;
	}
	
	public boolean accept(QgrsHomologyRecord hrec) {
		return hrec.getOverallScore() >= this.minHomologScore;
	}
	
	
}