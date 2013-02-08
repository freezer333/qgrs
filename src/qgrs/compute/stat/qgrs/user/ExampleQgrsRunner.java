package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsCriteria;
import qgrs.compute.stat.qgrs.QgrsRunner;

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
	protected QgrsCriteria buildQgrsCriteria() {
		return new SimpleQgrsCriteria(17, 2, 1, new QgrsHomologyCriteria());
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new ExampleGenePartitioner();
	}
	
	
	
	public static void main(String [] args) throws Exception {
		System.out.println("Running example qgrs engine");
		ExampleQgrsRunner e = new ExampleQgrsRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

	

}
