package qgrs.job;

public enum JobStage {
	
	Starting("Initializing", false, true), 
	Downloading("Fetching data from NCBI", true, false), 
	QGRS_ID("Identifying potential G-quadruplexes (QGRS) in sequences", true, false), 
	
	Alignment_Sync("Launching semi-global alignment", true, true), 
	Alignment_Calc("Calculating semi-global alignment matrix", false, false), 
	Alignment_Apply("Applying semi-global alignment", false, false), 
	QGRS_Homology("Computing QGRS Homology Scores", false, false), 
	Complete("Complete", true, true),
	Error("Error", true, false);
	
	public boolean isEvent() {
		return event;
	}
	private boolean event = false;
	private boolean passable = true;
	private String name = null;
	
	JobStage(String name, boolean event, boolean passable) {
		this.name = name; 
		this.event = event;
		this.passable = passable;
	}
	public boolean isPassable() {
		return passable;
	}
	public String getName() {
		return name;
	}
	
}
