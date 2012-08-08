package qgrs.job;

public class CancelFlag {

	volatile boolean raised = false;
	
	public boolean isRaised() {
		return raised;
	}
	
	public void raise() { raised = true; } 
	
}
