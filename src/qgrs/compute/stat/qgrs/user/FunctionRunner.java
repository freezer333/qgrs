package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsCriteria;
import qgrs.compute.stat.qgrs.QgrsRunner;

public class FunctionRunner extends QgrsRunner {

	/**
	 * This runner executes an analysis paritioning
	 * genes by species.
	 */
	
	@Override
	protected QgrsCriteria buildQgrsCriteria() {
		return new SimpleQgrsCriteria(17, 2, 0, new QgrsHomologyCriteria());
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new FunctionGenePartitioner();
	}

	@Override
	protected String getDescription() {
		return "Analysis By Gene Function";
	}

	public static void main(String [] args) throws Exception {
		System.out.println("Running example qgrs engine");
		FunctionRunner e = new FunctionRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}
}
