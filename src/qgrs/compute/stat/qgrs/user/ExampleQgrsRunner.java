package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsRunner;
import qgrs.compute.stat.qgrs.location.sets.QgrsAnyLocationSet;
import qgrs.compute.stat.qgrs.location.sets.QgrsLocationSet;
import qgrs.compute.stat.qgrs.series.DefaultQgrsSeriesSet;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;

public class ExampleQgrsRunner extends QgrsRunner {

	/**
	 * This is a suitable runner for testing out new statistical analysis only.
	 * 
	 * IMPORTANT
	 * -------------------------------------------------------------------------------
	 * Once you've got a good idea of what you are trying to do, you should ALWAYS
	 * create your own implementation of QgrsRunner, with a distinct description
	 * -------------------------------------------------------------------------------
	 */
	
	public ExampleQgrsRunner() {
		super(false);
	}
	
	@Override
	public String getDescription() {
		return "Example Runner";
	}
	
	@Override
	public String getId() {
		return "Example";
	}
	
	@Override
	protected QgrsSeriesSet buildSeriesSet() {
		return new DefaultQgrsSeriesSet();
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new ExampleGenePartitioner(this);
	}
	
	@Override
	protected QgrsLocationSet buildQgrsLocationSet() {
		return new QgrsAnyLocationSet();
	}
	
	
	public static void main(String [] args) throws Exception {
		System.out.println("Running example qgrs engine");
		ExampleQgrsRunner e = new ExampleQgrsRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

	

	

	

}
