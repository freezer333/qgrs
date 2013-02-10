package qgrs.compute.stat.qgrs.series;

import qgrs.data.records.QgrsHomologyRecord;

public class QgrsHomologyCriteria {
	private final double minHomologScore;

	public String buildLabel() {
		return "Min Homology Score:  " + minHomologScore;
	}
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