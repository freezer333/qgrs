package qgrs.compute.stat;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class GenePartition {
	
	public final String analysisId;
	public final String partitionId;
	public final String description;
	public final HashSet<String> ids;
	
	
	
	public GenePartition(Analysis analysis, String partitionId) {
		this.analysisId = analysis.getId();
		this.partitionId = partitionId;
		this.description = partitionId;
		ids = new HashSet<String>();
	}
	
	public GenePartition(Analysis analysis, String partitionId, String description) {
		this.analysisId = analysis.getId();
		this.partitionId = partitionId;
		this.description = description;
		ids = new HashSet<String>();
	}



	public String getAnalysisId() {
		return analysisId;
	}



	public String getPartitionId() {
		return partitionId;
	}

	public String getDescription() {
		return description;
	}

	public Set<String> getGeneIds() {
		return Collections.unmodifiableSet(ids);
	}
	
	
	
}
