package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsCriteria;
import qgrs.compute.stat.qgrs.QgrsRunner;

public class SpeciesRunner extends QgrsRunner {

	/**
	 * This runner executes an analysis paritioning
	 * genes by species.
	 */
	
	@Override
	protected QgrsCriteria buildQgrsCriteria() {
		return new SimpleQgrsCriteria(17, 2);
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new SpeciesGenePartitioner();
	}

	@Override
	protected String getDescription() {
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
