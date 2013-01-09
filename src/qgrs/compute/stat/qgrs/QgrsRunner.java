package qgrs.compute.stat.qgrs;

import qgrs.compute.stat.Runner;
import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.PartitionAnalyzer;
import qgrs.compute.stat.PartitionResultRecorder;

public abstract class QgrsRunner extends Runner {

	final QgrsCriteria qgrsCriteria = this.buildQgrsCriteria();
	
	public QgrsRunner() {
		super();
	}

	protected abstract QgrsCriteria buildQgrsCriteria();

	@Override
	protected PartitionResultRecorder buildStatementBuilder() {
		return new QgrsPartitionResultRecorder(this.getTableName());
	}

	@Override
	protected PartitionAnalyzer createProcessor(GenePartition partition) {
		return new QgrsAnalyzer(partition, qgrsCriteria);
	}
	
}
