package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsRunner;
import qgrs.compute.stat.qgrs.location.sets.QgrsAnyLocationSet;
import qgrs.compute.stat.qgrs.location.sets.QgrsLocationSet;
import qgrs.compute.stat.qgrs.location.sets.QgrsRegionLocationSet;
import qgrs.compute.stat.qgrs.series.DefaultQgrsSeriesSet;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;

public class Length_5UTR_Runner extends QgrsRunner {

	public Length_5UTR_Runner() {
		super(false);
	}
	
	@Override
	public String getDescription() {
		return "5' UTR Length Runner";
	}
	
	@Override
	public String getId() {
		return "5' UTR Length";
	}
	
	@Override
	protected QgrsSeriesSet buildSeriesSet() {
		return new DefaultQgrsSeriesSet();
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new LengthPartition_5UTR(this);
	}
	
	@Override
	protected QgrsLocationSet buildQgrsLocationSet() {
		return new QgrsAnyLocationSet().join(new QgrsRegionLocationSet());
	}
	
	
	public static void main(String [] args) throws Exception {
		System.out.println("Running 5' UTR Length engine");
		Length_5UTR_Runner e = new Length_5UTR_Runner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

	

	

	

}
