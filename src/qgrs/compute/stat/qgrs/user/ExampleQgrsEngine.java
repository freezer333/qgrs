package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsCriteria;
import qgrs.compute.stat.qgrs.QgrsEngine;

public class ExampleQgrsEngine extends QgrsEngine {

	@Override
	protected QgrsCriteria buildQgrsCriteria() {
		return new SimpleQgrsCriteria(25, 3);
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new ExampleGenePartitioner();
	}
	
	
	public static void main(String [] args) throws Exception {
		System.out.println("Running example qgrs engine");
		ExampleQgrsEngine e = new ExampleQgrsEngine();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

}
