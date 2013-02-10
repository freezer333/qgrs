package qgrs.compute.stat;

import java.sql.PreparedStatement;

public abstract class PartitionResult {
	public final GenePartition partition;
	private int numSamples =0;
	
	public abstract void addBatch(PreparedStatement ps) ;
	
	public PartitionResult(GenePartition partition) {
		this.partition = partition;
	}
	
	
	public void incrementSamples() {
		this.numSamples++;
	}
	
	public int getNumSamples() {
		return numSamples;
	}
	
	
}
