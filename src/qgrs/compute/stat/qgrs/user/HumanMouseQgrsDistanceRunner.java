package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsRunner;
import qgrs.compute.stat.qgrs.location.sets.QgrsDistanceFromCdsLocationSet;
import qgrs.compute.stat.qgrs.location.sets.QgrsLocationSet;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;

public class HumanMouseQgrsDistanceRunner extends QgrsRunner {

	/**
	 * Reports aggregate statistics about QGRS with at least one homolog (no matter what the score is)
	 */
	
	public HumanMouseQgrsDistanceRunner() {
		super(true);
	}
	
	@Override
	public String getId() {
		return "Human_MOUSE_ByDistance";
	}
	
	@Override
	public String getDescription() {
		return "STATS Human conserved with Mouse QGRS plotted by distance from CDS";
	}
	
	@Override
	protected QgrsSeriesSet buildSeriesSet() {
		return new SpeciesTetradQgrsSeriesSet("Mus musculus");
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new HumanComparisonPartitioner(this, "Mus musculus");
	}
	
	@Override
	protected QgrsLocationSet buildQgrsLocationSet() {
		return new QgrsDistanceFromCdsLocationSet();
	}
	
	public static void main(String [] args) throws Exception {
		HumanMouseQgrsDistanceRunner e = new HumanMouseQgrsDistanceRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

	

}
