package qgrs.compute.stat.qgrs.series;

import java.util.LinkedList;

import qgrs.data.records.QgrsHomologyRecord;

import com.google.common.base.Joiner;

public class QgrsHomologyCriteria {
	private final double minHomologScore;
	private final LinkedList<String> allowableSpecies = new LinkedList<String>();
	

	public String buildLabel() {
		String retval = "Min Homology Score:  " + minHomologScore;
		if ( this.allowableSpecies.size() == 0 ) {
			retval += " (any species)";
		}
		else {
			Joiner joiner = Joiner.on(',');
			retval += " (" + joiner.join(this.allowableSpecies) + ")";
		}
		return retval;
	}
	public QgrsHomologyCriteria() {
		this.minHomologScore = 0.95;
	}
	
	public QgrsHomologyCriteria(double minHomologyScore) {
		this.minHomologScore = minHomologyScore;
	}


	public LinkedList<String> getAllowableSpecies() {
		return allowableSpecies;
	}
	public double getMinHomologScore() {
		return minHomologScore;
	}
	
	public boolean accept(QgrsHomologyRecord hrec) {
		boolean scoreOk = hrec.getOverallScore() >= this.minHomologScore;
		if ( ! scoreOk ) {
			return false;
		}
		if ( this.allowableSpecies.size() == 0 ) {
			return true;
		}
		else {
			return this.allowableSpecies.contains(hrec.getC_species());
		}
	}
	
	
}