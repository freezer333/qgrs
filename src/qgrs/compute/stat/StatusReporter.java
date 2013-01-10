package qgrs.compute.stat;

import java.text.DecimalFormat;

public class StatusReporter {

	double totalPartitions;
	int partitionsCompleted = 0;
	
	public StatusReporter(int totalPartitions) {
		this.totalPartitions = totalPartitions;
	}
	
	public synchronized void recordPartitionComplete() {
		partitionsCompleted++;
		System.out.print("\r" + new DecimalFormat("0.0%").format(partitionsCompleted/totalPartitions) + " complete");
	}
}
