package qgrs.compute.stat.qgrs;

import qgrs.compute.stat.Engine;
import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.PartitionAnalyzer;
import qgrs.compute.stat.PartitionResultStatementBuilder;

public abstract class QgrsEngine extends Engine {

	final QgrsCriteria qgrsCriteria = this.buildQgrsCriteria();
	
	public QgrsEngine() {
		super();
	}

	protected abstract QgrsCriteria buildQgrsCriteria();

	@Override
	protected PartitionResultStatementBuilder buildStatementBuilder() {
		return new QgrsPartitionResultStatementBuilder();
	}

	@Override
	protected PartitionAnalyzer createProcessor(GenePartition partition) {
		return new QgrsAnalyzer(partition, qgrsCriteria);
	}
	
}
