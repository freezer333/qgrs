package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.qgrs.series.QgrsHomologyCriteria;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;
import qgrs.compute.stat.qgrs.series.SimpleQgrsCriteria;

public class SpeciesTetradQgrsSeriesSet extends QgrsSeriesSet {
	
	public SpeciesTetradQgrsSeriesSet (String species) {
		super();
		
		QgrsHomologyCriteria none = new QgrsHomologyCriteria(0.95);
		QgrsHomologyCriteria comparison = new QgrsHomologyCriteria(0.95);
		comparison.getAllowableSpecies().add(species);
		
		this.add(new SimpleQgrsCriteria(0, 17, 2, 0, none));
		this.add(new SimpleQgrsCriteria(1, 17, 2, 1, comparison));
		
		this.add(new SimpleQgrsCriteria(2, 17, 3, 0, none));
		this.add(new SimpleQgrsCriteria(3, 17, 3, 1, comparison));
		
		this.add(new SimpleQgrsCriteria(4, 17, 4, 0, none));
		this.add(new SimpleQgrsCriteria(5, 17, 4, 1, comparison));
		
		this.add(new SimpleQgrsCriteria(6, 17, 5, 0, none));
		this.add(new SimpleQgrsCriteria(7, 17, 5, 1, comparison));
	}
	
}
