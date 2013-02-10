package qgrs.compute.stat.qgrs.series;

public class DefaultQgrsSeriesSet extends QgrsSeriesSet {
	
	public DefaultQgrsSeriesSet () {
		super();
		this.add(new SimpleQgrsCriteria(0, 17, 2, 0, new QgrsHomologyCriteria(0)));
		this.add(new SimpleQgrsCriteria(1, 17, 2, 1, new QgrsHomologyCriteria(0.95)));
	}
	
}
