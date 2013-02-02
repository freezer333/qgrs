package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsCriteria;
import qgrs.compute.stat.qgrs.QgrsRunner;

public class HumanQgrsH95Runner extends QgrsRunner {

	/**
	 * Reports aggregate statistics about QGRS - with at least on QGRSH with 0.95+ score
	 */
	
	@Override
	protected String getDescription() {
		return "STATS Human Conserved QGRS 95";
	}
	
	@Override
	protected QgrsCriteria buildQgrsCriteria() {
		return new SimpleQgrsCriteria(17, 2, 1, new QgrsHomologyCriteria(0.95));
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new HumanAllPartitioner();
	}
	
	
	
	public static void main(String [] args) throws Exception {
		HumanQgrsH95Runner e = new HumanQgrsH95Runner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

	

}
