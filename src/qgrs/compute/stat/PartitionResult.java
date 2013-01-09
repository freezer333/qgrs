package qgrs.compute.stat;

import java.sql.PreparedStatement;

public abstract class PartitionResult {
	public final String partitionLabel;
	private int numSamples =0;
	
	public abstract void addBatch(PreparedStatement ps) ;
	
	public PartitionResult(String label) {
		partitionLabel = label;
	}
	
	
	public void incrementSamples() {
		this.numSamples++;
	}
	
	public int getNumSamples() {
		return numSamples;
	}
	
	
}
