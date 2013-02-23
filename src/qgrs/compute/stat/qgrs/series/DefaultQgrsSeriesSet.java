package qgrs.compute.stat.qgrs.series;

public class DefaultQgrsSeriesSet extends QgrsSeriesSet {
	
	public DefaultQgrsSeriesSet () {
		super();
		this.add(new SimpleQgrsCriteria(0, 17, 2, 0, new QgrsHomologyCriteria(0)));
		this.add(new SimpleQgrsCriteria(1, 17, 2, 1, new QgrsHomologyCriteria(0.95)));
		
		
		QgrsHomologyCriteria fruitFly = new QgrsHomologyCriteria(0.95);
		fruitFly.getAllowableSpecies().add("Drosophila melanogaster");
		
		QgrsHomologyCriteria mouse = new QgrsHomologyCriteria(0.95);
		mouse.getAllowableSpecies().add("Mus musculus");
		
		this.add(new SimpleQgrsCriteria(2, 17, 2, 1, fruitFly));
		this.add(new SimpleQgrsCriteria(3, 17, 2, 1, mouse));
		
		
	}
	
}
