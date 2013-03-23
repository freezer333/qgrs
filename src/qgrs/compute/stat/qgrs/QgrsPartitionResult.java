package qgrs.compute.stat.qgrs;

import java.sql.PreparedStatement;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.PartitionResult;
import qgrs.compute.stat.qgrs.location.QgrsLocationAccumulator;
import qgrs.compute.stat.qgrs.series.QgrsCriteriaSeries;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;

public class QgrsPartitionResult extends PartitionResult {
	
	final QgrsSeriesSet seriesSet;
	
	public QgrsPartitionResult(GenePartition partition, QgrsSeriesSet seriesSet) {
		super(partition);
		this.seriesSet = seriesSet;
	}
	
	

	@Override
	public void addBatch(PreparedStatement ps) {
		
		String a = this.partition.getAnalysisId();
		String p = this.partition.partitionId;
		
		for ( QgrsCriteriaSeries q : this.seriesSet ) {
			int s = q.getOrder();
			for ( QgrsLocationAccumulator loc : q.getLocations() ) {
				try {
					ps.setString(1, a);
					ps.setString(2, p);
					ps.setInt(3, s);
					ps.setInt(4, loc.getOrder());
					ps.setString(5, loc.getLabel());
					ps.setInt(6, (int)loc.results.getSum());
					ps.setDouble(7,  loc.results.getMean());
					ps.setDouble(8,  loc.results.getMedian());
					ps.setDouble(9,  loc.results.getStandardDeviation());
					ps.addBatch();
				}
				catch (Exception e) {
					throw new RuntimeException (e);
				}
			}
		}
		
		
	}

	
	
	

}
