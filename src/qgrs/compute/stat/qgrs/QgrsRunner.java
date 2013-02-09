package qgrs.compute.stat.qgrs;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.PartitionAnalyzer;
import qgrs.compute.stat.PartitionResultRecorder;
import qgrs.compute.stat.Analysis;
import qgrs.compute.stat.StatusReporter;

public abstract class QgrsRunner extends Analysis {

	final QgrsCriteria qgrsCriteria = this.buildQgrsCriteria();
	
	public QgrsRunner(boolean active) {
		super(active);
	}

	protected abstract QgrsCriteria buildQgrsCriteria();

	@Override
	protected PartitionResultRecorder buildStatementBuilder() {
		return new QgrsPartitionResultRecorder(this.getTableName());
	}

	@Override
	protected PartitionAnalyzer createProcessor(GenePartition partition, StatusReporter reporter) {
		return new QgrsAnalyzer(partition, qgrsCriteria, reporter);
	}
	
}
