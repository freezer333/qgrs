package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.qgrs.QgrsCriteria;
import qgrs.data.records.GQuadruplexRecord;


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
	public boolean accept(GQuadruplexRecord record) {
		return record.getScore() >= this.minGScore && record.getNumTetrads() >= this.minTetrads;
	}

}
