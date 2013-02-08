package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsCriteria;
import qgrs.compute.stat.qgrs.QgrsRunner;

public class SpeciesRunner extends QgrsRunner {

	
	
	/**
	 * This runner executes an analysis paritioning
	 * genes by species.
	 */
	
	public SpeciesRunner() {
		super(true);
	}
	
	@Override
	protected QgrsCriteria buildQgrsCriteria() {
		return new SimpleQgrsCriteria(17, 2, 0, new QgrsHomologyCriteria());
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new SpeciesGenePartitioner();
	}
	
	@Override
	public String getId() {
		return "Species";
	}

	@Override
	public String getDescription() {
		return "Analysis By Species";
	}

	public static void main(String [] args) throws Exception {
		System.out.println("Running example qgrs engine");
		SpeciesRunner e = new SpeciesRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}
}
