package qgrs.compute.stat.qgrs;

import java.sql.PreparedStatement;

import qgrs.compute.stat.PartitionResult;

public class QgrsPartitionResult extends PartitionResult {
	
	public final QgrsRegionStats all = new QgrsRegionStats();
	public final QgrsRegionStats _3Prime = new QgrsRegionStats();
	public final QgrsRegionStats _5Prime = new QgrsRegionStats();
	public final QgrsRegionStats cds = new QgrsRegionStats();
	
	
	public QgrsPartitionResult(String partitionLabel) {
		super(partitionLabel);
	}
	
	
	@Override
	public void addBatch(PreparedStatement ps) {
		int c = 1;
		try {
			ps.setString(c++, this.partitionLabel);
			ps.setInt(c++, this.getNumSamples());
			
			ps.setDouble(c++, this.all.getSum());
			ps.setDouble(c++, this.all.getMean());
			ps.setDouble(c++, this.all.getMedian());
			
			ps.setDouble(c++, this._5Prime.getSum());
			ps.setDouble(c++, this._5Prime.getMean());
			ps.setDouble(c++, this._5Prime.getMedian());
			
			ps.setDouble(c++, this.cds.getSum());
			ps.setDouble(c++, this.cds.getMean());
			ps.setDouble(c++, this.cds.getMedian());
			
			ps.setDouble(c++, this._3Prime.getSum());
			ps.setDouble(c++, this._3Prime.getMean());
			ps.setDouble(c++, this._3Prime.getMedian());
			
			ps.addBatch();
		}
		catch ( Exception e) {
			e.printStackTrace();
		}
	}

	
	
	

}
