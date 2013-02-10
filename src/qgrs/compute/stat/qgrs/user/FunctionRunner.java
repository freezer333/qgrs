package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsRunner;
import qgrs.compute.stat.qgrs.location.sets.QgrsAnyLocationSet;
import qgrs.compute.stat.qgrs.location.sets.QgrsLocationSet;
import qgrs.compute.stat.qgrs.series.DefaultQgrsSeriesSet;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;

public class FunctionRunner extends QgrsRunner {

	/**
	 * This runner executes an analysis paritioning
	 * genes by species.
	 */
	
	public FunctionRunner() {
		super(true);
	}
	
	@Override
	protected QgrsSeriesSet buildSeriesSet() {
		return new DefaultQgrsSeriesSet();
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new FunctionGenePartitioner(this);
	}

	@Override
	public String getDescription() {
		return "Analysis By Gene Function";
	}
	
	public String getId() {
		return "Function";
	}
	
	@Override
	protected QgrsLocationSet buildQgrsLocationSet() {
		return new QgrsAnyLocationSet();
	}

	public static void main(String [] args) throws Exception {
		System.out.println("Running example qgrs engine");
		FunctionRunner e = new FunctionRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}
}
