package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsCriteria;
import qgrs.compute.stat.qgrs.QgrsRunner;

public class HumanQgrsRunner extends QgrsRunner {

	/**
	 * Reports aggregate statistics about QGRS with at least one homolog (no matter what the score is)
	 */
	
	public HumanQgrsRunner() {
		super(true);
	}
	
	@Override
	public String getId() {
		return "Human_All";
	}
	
	@Override
	public String getDescription() {
		return "STATS Human QGRS";
	}
	
	@Override
	protected QgrsCriteria buildQgrsCriteria() {
		return new SimpleQgrsCriteria(17, 2, 0, new QgrsHomologyCriteria(0));
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new HumanAllPartitioner();
	}
	
	
	
	public static void main(String [] args) throws Exception {
		HumanQgrsRunner e = new HumanQgrsRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

	

}
