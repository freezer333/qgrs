package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.qgrs.QgrsCriteria;
import qgrs.data.records.GQuadruplexRecord;
import qgrs.data.records.QgrsHomologyProfile;


/**
 * Provides minimal filtering for qgrs by min g-score and min tetrads
 * 
 */
public class SimpleQgrsCriteria implements QgrsCriteria {

	private final int minGScore;
	private final int minTetrads;
	
	/**
	 * Filters by the number of QGRSH pairs found.  Note, this
	 * merely counts the number of homologs (by unique species) - 
	 * it does not count QGRSH that fit another criteria.
	 */
	private final int minHomologs;
	private QgrsHomologyCriteria homologyCriteria = new QgrsHomologyCriteria();

	public SimpleQgrsCriteria() {
		this.minGScore = 17;
		this.minTetrads = 2;
		this.minHomologs = 0;  // defaults to not requiring any homologs
		
	}


	public SimpleQgrsCriteria(int minGScore, int minTetrads, int minHomolgs, QgrsHomologyCriteria homologyCriteria) {
		super();
		this.minGScore = minGScore;
		this.minTetrads = minTetrads;
		this.minHomologs = minHomolgs;
		this.homologyCriteria = homologyCriteria;
	}
	
	

	@Override
	public boolean accept(QgrsHomologyProfile record) {
		return 
				record.principle.getScore() >= this.minGScore && 
				record.principle.getNumTetrads() >= this.minTetrads &&
				record.getNumHomologsSpecies(this.homologyCriteria) >= this.minHomologs;
	}

}
