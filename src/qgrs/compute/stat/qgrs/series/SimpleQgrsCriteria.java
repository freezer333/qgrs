package qgrs.compute.stat.qgrs.series;

import qgrs.data.records.GQuadruplexRecord;
import qgrs.data.records.QgrsHomologyProfile;


/**
 * Provides minimal filtering for qgrs by min g-score and min tetrads
 * 
 */
public class SimpleQgrsCriteria extends QgrsCriteriaSeries {

	private final int minGScore;
	private final int minTetrads;
	
	/**
	 * Filters by the number of QGRSH pairs found.  Note, this
	 * merely counts the number of homologs (by unique species) - 
	 * it does not count QGRSH that fit another criteria.
	 */
	private final int minHomologs;
	private QgrsHomologyCriteria homologyCriteria = new QgrsHomologyCriteria();

	public SimpleQgrsCriteria(int order) {
		super(order, buildLabel(17, 2, 0, null));
		this.minGScore = 17;
		this.minTetrads = 2;
		this.minHomologs = 0;  // defaults to not requiring any homologs
		
	}


	public SimpleQgrsCriteria(int order, int minGScore, int minTetrads, int minHomolgs, QgrsHomologyCriteria homologyCriteria) {
		super(order, buildLabel(minGScore, minTetrads, minHomolgs, homologyCriteria));
		this.minGScore = minGScore;
		this.minTetrads = minTetrads;
		this.minHomologs = minHomolgs;
		this.homologyCriteria = homologyCriteria;
	}
	
	
	static String buildLabel(int minGScore, int minTetrads, int minHomologs, QgrsHomologyCriteria homologyCriteria) {
		String retval = "Min G-Score:  " + minGScore;
		retval += ", Min Tetrads:  " + minTetrads;
		if ( homologyCriteria != null ) {
			retval += ", Min Homolog:  " + minHomologs;
			retval += (", " +  homologyCriteria.buildLabel() );
		}
		return retval;
	}
	

	@Override
	public boolean accept(QgrsHomologyProfile record) {
		return 
				record.principle.getScore() >= this.minGScore && 
				record.principle.getNumTetrads() >= this.minTetrads &&
				record.getNumHomologsSpecies(this.homologyCriteria) >= this.minHomologs;
	}

}
