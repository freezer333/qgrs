package qgrs.compute.stat.qgrs;


import java.util.Collection;
import java.util.List;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.PartitionAnalyzer;
import qgrs.compute.stat.PartitionResult;
import qgrs.compute.stat.StatusReporter;
import qgrs.compute.stat.qgrs.series.QgrsCriteriaSeries;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;
import qgrs.data.GeneSequence;
import qgrs.data.records.QgrsHomologyProfile;
import qgrs.db.DatabaseConnection;
import qgrs.db.GeneSequenceDb;
import qgrs.db.HomologyRecordDb;

public class QgrsAnalyzer extends PartitionAnalyzer{

	final QgrsSeriesSet seriesSet;

	
	public QgrsAnalyzer(GenePartition parition, QgrsSeriesSet seriesSet, StatusReporter reporter) {
		super(parition,reporter);
		this.seriesSet = seriesSet;
	}
	
	
	
	
	@Override
	public PartitionResult call() throws Exception {
		QgrsPartitionResult result = new QgrsPartitionResult(this.parition, this.seriesSet);
		DatabaseConnection conn = new DatabaseConnection(getConnection());
		GeneSequenceDb geneDb = new GeneSequenceDb(conn);
		HomologyRecordDb qgrsDb = new HomologyRecordDb(conn);
		
		List<GeneSequence> genes = geneDb.getIn(this.parition.ids);
		
		// for each series, record the partition/series record in series table
		this.seriesSet.insert(conn.getConnection(), this.parition.analysisId, this.parition.partitionId);
		
		for ( GeneSequence seq : genes ) {
			Collection<QgrsHomologyProfile> qgrsList = qgrsDb.getQgrsHomologyProfiles(seq);
			for ( QgrsCriteriaSeries series : this.seriesSet ) {
				series.getLocations().startAccumulators();
				for ( QgrsHomologyProfile qgrs : qgrsList) {
					if ( series.accept(qgrs) ) {
						series.getLocations().record(qgrs.principle, seq);
					}
				}
				series.getLocations().finishAccumulators();
			}
		}
		this.statusReporter.recordPartitionComplete();
		
		conn.close();
		return result;
	}
	
	

}
