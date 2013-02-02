package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsCriteria;
import qgrs.compute.stat.qgrs.QgrsRunner;

public class HumanQgrsHRunner extends QgrsRunner {

	/**
	 * Reports aggregate statistics about QGRS - irrespective of homology
	 */
	
	@Override
	public String getDescription() {
		return "STATS Human Conserved QGRS";
	}
	
	@Override
	protected QgrsCriteria buildQgrsCriteria() {
		return new SimpleQgrsCriteria(17, 2, 1, new QgrsHomologyCriteria(0));
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new HumanAllPartitioner();
	}
	
	
	
	public static void main(String [] args) throws Exception {
		HumanQgrsHRunner e = new HumanQgrsHRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

	

	

}
