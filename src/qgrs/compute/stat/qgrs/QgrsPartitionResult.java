package qgrs.compute.stat.qgrs;

import java.sql.PreparedStatement;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import qgrs.compute.stat.PartitionResult;

public class QgrsPartitionResult implements PartitionResult {
	public final QgrsRegionStats all = new QgrsRegionStats();
	public final QgrsRegionStats _3Prime = new QgrsRegionStats();
	public final QgrsRegionStats _5Prime = new QgrsRegionStats();
	public final QgrsRegionStats cds = new QgrsRegionStats();
	
	@Override
	public void addBatch(PreparedStatement ps) {
		System.out.println("------------------------------------");
		System.out.println("*   " + all);
		System.out.println("5'  " + _5Prime);
		System.out.println("CDS " + cds);
		System.out.println("3'  " + _3Prime);
	}
	
	

}
