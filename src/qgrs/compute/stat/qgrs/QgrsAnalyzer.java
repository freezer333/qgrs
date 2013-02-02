package qgrs.compute.stat.qgrs;


import java.util.Collection;
import java.util.List;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.PartitionAnalyzer;
import qgrs.compute.stat.PartitionResult;
import qgrs.compute.stat.StatusReporter;
import qgrs.data.GeneSequence;
import qgrs.data.records.QgrsHomologyProfile;
import qgrs.db.DatabaseConnection;
import qgrs.db.GeneSequenceDb;
import qgrs.db.HomologyRecordDb;

public class QgrsAnalyzer extends PartitionAnalyzer{

	final QgrsCriteria qgrsCriteria;
	
	public QgrsAnalyzer(GenePartition parition, QgrsCriteria qgrsCriteria, StatusReporter reporter) {
		super(parition,reporter);
		this.qgrsCriteria = qgrsCriteria;
	}

	
	/**
	 * This needs to be enhanced to take in qgrs-h, etc.
	 */
	@Override
	public PartitionResult call() throws Exception {
		QgrsPartitionResult result = new QgrsPartitionResult(this.parition.label);
		
		DatabaseConnection conn = new DatabaseConnection(getConnection());
		GeneSequenceDb geneDb = new GeneSequenceDb(conn);
		HomologyRecordDb qgrsDb = new HomologyRecordDb(conn);
		
		List<GeneSequence> genes = geneDb.getIn(this.parition.ids);
		
		for ( GeneSequence seq : genes ) {
			int qgrsCount = 0;
			int qgrs5PrimeCount = 0;
			int qgrsCdsCount = 0;
			int qgrs3PrimeCount = 0 ;
			
			Collection<QgrsHomologyProfile> qgrsList = qgrsDb.getQgrsHomologyProfiles(seq);
			for ( QgrsHomologyProfile qgrs : qgrsList) {
				if ( this.qgrsCriteria.accept(qgrs) ) {
					qgrsCount++;
					if ( qgrs.principle.isIn5Prime() ) qgrs5PrimeCount++;
					if ( qgrs.principle.isInCds() ) qgrsCdsCount++;
					if (qgrs.principle.isIn3Prime() ) qgrs3PrimeCount++;
				}
			}
			result.incrementSamples();
			result.all.addValue(qgrsCount);
			result._5Prime.addValue(qgrs5PrimeCount);
			result.cds.addValue(qgrsCdsCount);
			result._3Prime.addValue(qgrs3PrimeCount);
		}
		this.statusReporter.recordPartitionComplete();
		return result;
	}
	
	

}
