package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.qgrs.QgrsCriteria;
import qgrs.data.records.QgrsHomologyProfile;


/**
 * Camille & Matt's first test
 * 
 */
public class QgrsCriteriaTest1 implements QgrsCriteria {

	private final int minGScore;
	private final int minTetrads;
	
	public QgrsCriteriaTest1() {
		this.minGScore = 17;
		this.minTetrads = 4;
	}


	public QgrsCriteriaTest1(int minGScore, int minTetrads) {
		super();
		this.minGScore = minGScore;
		this.minTetrads = minTetrads;
	}

	@Override
	public boolean accept(QgrsHomologyProfile record) {
		return record.principle.getScore() >= this.minGScore && record.principle.getNumTetrads() >= this.minTetrads;
	}

}
