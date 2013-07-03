package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsRunner;
import qgrs.compute.stat.qgrs.location.sets.QgrsAnyLocationSet;
import qgrs.compute.stat.qgrs.location.sets.QgrsLocationSet;
import qgrs.compute.stat.qgrs.location.sets.QgrsRegionLocationSet;
import qgrs.compute.stat.qgrs.series.DefaultQgrsSeriesSet;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;

public class MultiplePolyASignalsRunner extends QgrsRunner{

	public MultiplePolyASignalsRunner() {
		super(false);
	}

	@Override
	public String getDescription() {
		return "Multiple Poly A Signals Runner";
	}

	@Override
	public String getId() {
		return "Multiple Poly A Signals";
	}

	@Override
	protected QgrsSeriesSet buildSeriesSet() {
		return new DefaultQgrsSeriesSet();
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new MultiplePolyASignalsPartitioner(this);
	}

	@Override
	protected QgrsLocationSet buildQgrsLocationSet() {
		return new QgrsAnyLocationSet().join(new QgrsRegionLocationSet());
	}


	public static void main(String [] args) throws Exception {
		System.out.println("Running Multiple Poly A Signals engine");
		MultiplePolyASignalsRunner e = new MultiplePolyASignalsRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}







}
