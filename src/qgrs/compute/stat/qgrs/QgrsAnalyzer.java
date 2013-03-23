package qgrs.compute.stat.qgrs;


import java.text.DecimalFormat;
import java.util.Collection;

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
		System.out.println("Partition (" + this.parition.partitionId + ") - running");
		int i = 0;
		for ( String id : this.parition.ids ) {
			GeneSequence seq = geneDb.get(id);
			Collection<QgrsHomologyProfile> qgrsList = qgrsDb.getQgrsHomologyProfiles(seq);
			float p = i++ / (float)this.parition.ids.size();
			System.out.println("Partition (" + this.parition.partitionId + ") - " + new DecimalFormat("0.0%").format(p) + " complete");
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
