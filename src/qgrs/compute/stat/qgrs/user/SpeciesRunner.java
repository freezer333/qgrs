package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsRunner;
import qgrs.compute.stat.qgrs.location.sets.QgrsLocationSet;
import qgrs.compute.stat.qgrs.location.sets.QgrsRegionLocationSet;
import qgrs.compute.stat.qgrs.series.DefaultQgrsSeriesSet;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;

public class SpeciesRunner extends QgrsRunner {
	
	public SpeciesRunner() {
		super(true);
	}
	
	@Override
	protected QgrsSeriesSet buildSeriesSet() {
		return new DefaultQgrsSeriesSet();
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new SpeciesGenePartitioner(this);
	}
	
	@Override
	public String getId() {
		return "Species";
	}

	@Override
	public String getDescription() {
		return "Analysis By Species";
	}
	
	@Override
	protected QgrsLocationSet buildQgrsLocationSet() {
		return new QgrsRegionLocationSet();
	}

	public static void main(String [] args) throws Exception {
		System.out.println("Running example qgrs engine");
		SpeciesRunner e = new SpeciesRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}
}
