package qgrs.job;

import java.text.DecimalFormat;

public class ProgressReport {
	final String status;
	final boolean showPercentComplete;
	final String percentComplete;
	final JobStage fullStage;
	final String stage;
	
	public ProgressReport(JobStage stage, double percentComplete, String status) {
		this.status = status == null ? "" : status;
		this.fullStage = stage;
		DecimalFormat df = new DecimalFormat("0.0");
		this.percentComplete = df.format(percentComplete*100);
		this.showPercentComplete = percentComplete >= 0;
		this.stage = fullStage.getName();
	}
	
	

	public String getStage() {
		return stage;
	}



	public String getStatus() {
		return status;
	}

	public boolean isShowPercentComplete() {
		return showPercentComplete;
	}

	public JobStage getFullStage() {
		return fullStage;
	}

	public String getPercentComplete() {
		return percentComplete;
	}
	
	

	
	
	
}
