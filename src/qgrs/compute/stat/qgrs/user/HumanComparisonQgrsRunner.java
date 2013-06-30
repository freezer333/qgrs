package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsRunner;
import qgrs.compute.stat.qgrs.location.sets.QgrsLocationSet;
import qgrs.compute.stat.qgrs.location.sets.QgrsRegionLocationSet;
import qgrs.compute.stat.qgrs.series.DefaultQgrsSeriesSet;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;

public class HumanComparisonQgrsRunner extends QgrsRunner {

	/**
	 * Reports aggregate statistics about QGRS with at least one homolog (no matter what the score is)
	 */
	private final String comparison;
	
	public HumanComparisonQgrsRunner(String comparison) {
		super(true);
		this.comparison = comparison;
	}
	
	@Override
	public String getId() {
		return "Human_" + comparison + "_ByRegion";
	}
	
	@Override
	public String getDescription() {
		return "STATS Human conserved with " + comparison + " QGRS plotted by region";
	}
	
	@Override
	protected QgrsSeriesSet buildSeriesSet() {
		return new SpeciesTetradQgrsSeriesSet(comparison);
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new HumanComparisonPartitioner(this, comparison);
	}
	
	@Override
	protected QgrsLocationSet buildQgrsLocationSet() {
		return new QgrsRegionLocationSet();
	}
	
	public static void main(String [] args) throws Exception {
		HumanComparisonQgrsRunner e = new HumanComparisonQgrsRunner("Mus musculus");
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

	

}
