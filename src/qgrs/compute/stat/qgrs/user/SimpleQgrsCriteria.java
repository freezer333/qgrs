package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.qgrs.QgrsCriteria;
import qgrs.data.records.GQuadruplexRecord;


/**
 * Provides minimal filtering for qgrs by min g-score and min tetrads
 * 
 */
public class SimpleQgrsCriteria implements QgrsCriteria {

	private final int minGScore;
	private final int minTetrads;
	
	public SimpleQgrsCriteria() {
		this.minGScore = 17;
		this.minTetrads = 2;
	}


	public SimpleQgrsCriteria(int minGScore, int minTetrads) {
		super();
		this.minGScore = minGScore;
		this.minTetrads = minTetrads;
	}

	@Override
	public boolean accept(GQuadruplexRecord record) {
		return record.getScore() >= this.minGScore && record.getNumTetrads() >= this.minTetrads;
	}

}
