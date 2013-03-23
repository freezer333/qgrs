package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsRunner;
import qgrs.compute.stat.qgrs.location.sets.QgrsLocationSet;
import qgrs.compute.stat.qgrs.location.sets.QgrsRegionLocationSet;
import qgrs.compute.stat.qgrs.series.DefaultQgrsSeriesSet;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;

public class HumanMouseQgrsRunner extends QgrsRunner {

	/**
	 * Reports aggregate statistics about QGRS with at least one homolog (no matter what the score is)
	 */
	
	public HumanMouseQgrsRunner() {
		super(true);
	}
	
	@Override
	public String getId() {
		return "Human_MOUSE";
	}
	
	@Override
	public String getDescription() {
		return "STATS Human conserved with Mouse QGRS";
	}
	
	@Override
	protected QgrsSeriesSet buildSeriesSet() {
		return new MouseTetradQgrsSeriesSet();
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new HumanMousePartitioner(this);
	}
	
	@Override
	protected QgrsLocationSet buildQgrsLocationSet() {
		return new QgrsRegionLocationSet();
	}
	
	public static void main(String [] args) throws Exception {
		HumanMouseQgrsRunner e = new HumanMouseQgrsRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

	

}
