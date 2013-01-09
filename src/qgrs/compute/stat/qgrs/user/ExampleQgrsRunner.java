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
	
	@Override
	protected String getDescription() {
		return "Example Runner";
	}
	
	@Override
	protected QgrsCriteria buildQgrsCriteria() {
		return new SimpleQgrsCriteria(17, 2);
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new HumanAllPartitioner();
	}
	
	
	
	public static void main(String [] args) throws Exception {
		System.out.println("Running example qgrs engine");
		ExampleQgrsRunner e = new ExampleQgrsRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

	

}
