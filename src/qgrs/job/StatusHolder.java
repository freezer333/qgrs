package qgrs.job;

public interface StatusHolder {
	String getStatus(JobStage stage) ;
	void setStatus(JobStage stage, double percent, String s) ;
}
