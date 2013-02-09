package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsCriteria;
import qgrs.compute.stat.qgrs.QgrsRunner;

public class QgrsRunnerTest1 extends QgrsRunner {

	/**
	 * Camille & Matt's first test; 4 tetrads
	 */
	
	public QgrsRunnerTest1() {
		super(false);
	}
	
	@Override
	public String getDescription() {
		return "Test 1";
	}
	
	@Override 
	public String getId() {
		return "Test1";
	}
	
	@Override
	protected QgrsCriteria buildQgrsCriteria() {
		return new QgrsCriteriaTest1(17, 4);
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new PartitionTest1(this);
	}
	
	
	
	public static void main(String [] args) throws Exception {
		System.out.println("Running test");
		QgrsRunnerTest1 e = new QgrsRunnerTest1();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

	

}
