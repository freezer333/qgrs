package qgrs.job;

import java.text.DecimalFormat;
import java.util.UUID;

import framework.web.util.StringUtils;


public abstract class Job implements Runnable, StatusHolder{

		private final UUID id;
		volatile private boolean active = false;
		volatile private String status;
		volatile private JobStage currentStage = JobStage.Starting;
		volatile private double percent = 0;
		volatile Throwable lastError;
		boolean [] sentCompletion = new boolean [JobStage.values().length];
		
		public Throwable getLastError() { return lastError; }
		
		public Job() {
			this.id = UUID.randomUUID();
			for ( boolean sc : sentCompletion ) {
				sc = false;
			}
		}
		
		public UUID getId() {
			return id;
		}
		@Override
		synchronized public String getStatus(JobStage stage) {
			if ( this.currentStage == JobStage.Complete ) 
				return "";
			String retval = this.currentStage.getName();
			if ( percent >= 0 || StringUtils.isDefined(status)) {
				retval += ":  ";
			}
			if ( percent >= 0  ){
				DecimalFormat df = new DecimalFormat("0.0");
				retval += (df.format(percent*100) + "%    ");
			}
			
			if ( StringUtils.isDefined(this.status) ) {
				retval += ("(" + status + ")");
			}
			return retval;
		}
		
		@Override
		synchronized public void setStatus(JobStage stage, double percent, String s) {
			this.currentStage = stage;
			this.percent = percent;
			status = s;
			
		}
		
		public ProgressReport getProgressReport() {
			if ( this.lastError != null ) {
				return new ProgressReport(JobStage.Error, -1, this.lastError.getMessage());
			}
			int thisIndex = this.currentStage.ordinal();
			int previousIndex = thisIndex -1;
			// Find the first stage that isn't passable and didn't have a completion sent
			for ( int i = 0; i < thisIndex; i++ ) {
				JobStage stage = JobStage.values()[i];
				if ( !sentCompletion[i] && !stage.isPassable() ) {
					this.sentCompletion[i] = true;
					if ( stage.isEvent()) {
						return new ProgressReport(stage, -1, null);
					}
					else {
						return new ProgressReport(stage, 1, null);
					}
				}
			}
			// If we make it through that, it means there weren't any outstanding completion notifications.
			this.sentCompletion[thisIndex] = true;
			return new ProgressReport(this.currentStage, this.percent, this.status);
		}
		
		public abstract void runJob() throws Exception;
		

		@Override
		public void run() {
			try {
				this.active = true;
				runJob();
			}
			catch (Exception e) {
				e.printStackTrace();
				this.setStatus(JobStage.Error, -1, e.getMessage());
				this.lastError = e;
				System.out.println(id.toString() + ":  " + this.status);
			}
			finally {
				this.active = false;
			}
			this.status = "Complete";
			System.out.println(id.toString() + ":  " + this.status);
		}

		public boolean isActive() {
			return active;
		}
}
