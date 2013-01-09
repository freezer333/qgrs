package qgrs.compute.stat;

import java.util.HashSet;

public class GenePartition {
	
	public final String label;
	public final HashSet<String> ids;
	
	
	public GenePartition() {
		label = "";
		ids = new HashSet<String>();
	}
	
	public GenePartition(String label) {
		this.label = label;
		ids = new HashSet<String>();
	}
	
}
