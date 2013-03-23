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
	private final int maxTetrads;
	
	/**
	 * Filters by the number of QGRSH pairs found.  Note, this
	 * merely counts the number of homologs (by unique species) - 
	 * it does not count QGRSH that fit another criteria.
	 */
	private final int minHomologs;
	private QgrsHomologyCriteria homologyCriteria = new QgrsHomologyCriteria();

	public SimpleQgrsCriteria(int order) {
		this(order, 17, 2, Integer.MAX_VALUE, 0, null);
	}


	public SimpleQgrsCriteria(int order, int minGScore, int minTetrads, int minHomolgs, QgrsHomologyCriteria homologyCriteria) {
		this(order, minGScore, minTetrads, Integer.MAX_VALUE, minHomolgs, homologyCriteria);
	}
	
	public SimpleQgrsCriteria(int order, int minGScore, int minTetrads, int maxTetrads, int minHomolgs, QgrsHomologyCriteria homologyCriteria) {
		super(order, buildLabel(minGScore, minTetrads, minHomolgs, homologyCriteria));
		this.minGScore = minGScore;
		this.minTetrads = minTetrads;
		this.minHomologs = minHomolgs;
		this.maxTetrads = maxTetrads;
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
	public boolean acceptQuadruplex(int tetrads, int gScore) {
		return 	gScore >= this.minGScore && 
				tetrads >= this.minTetrads && 
				tetrads <= this.maxTetrads;
	}


	@Override
	public boolean accept(QgrsHomologyProfile record) {
		return 
				this.acceptQuadruplex(record.principle.getNumTetrads(), record.principle.getScore()) && 
				record.getNumHomologsSpecies(this, this.homologyCriteria) >= this.minHomologs;
	}

}
