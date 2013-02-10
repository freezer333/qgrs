package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsRunner;
import qgrs.compute.stat.qgrs.location.sets.QgrsLocationSet;
import qgrs.compute.stat.qgrs.location.sets.QgrsRegionLocationSet;
import qgrs.compute.stat.qgrs.series.DefaultQgrsSeriesSet;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;

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
	protected QgrsSeriesSet buildSeriesSet() {
		return new DefaultQgrsSeriesSet();
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new HumanAllPartitioner(this);
	}
	
	@Override
	protected QgrsLocationSet buildQgrsLocationSet() {
		return new QgrsRegionLocationSet();
	}
	
	public static void main(String [] args) throws Exception {
		HumanQgrsRunner e = new HumanQgrsRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

	

}
