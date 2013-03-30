package qgrs.compute.stat.qgrs;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
					loc.fillStatement(ps, a, p, s);
					ps.addBatch();
				}
				catch (Exception e) {
					throw new RuntimeException (e);
				}
			}
		}
		
		
	}



	

	
	
	

}
