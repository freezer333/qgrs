package qgrs.model;


import qgrs.job.AlignmentJob;
import qgrs.job.Job;
import framework.web.AbstractWebContext;

public class JobContext {

	public static boolean hasActiveJob(AbstractWebContext context) {
		if ( context.getRequest().getSession().getAttribute("activeJob") != null ) {
			String id = (String) context.getRequest().getSession().getAttribute("activeJob");
			Job job = (Job)context.getRequest().getSession().getAttribute(id);
			if ( job != null ) {
				return job.isActive();
			}
		}
		return false;
		
	}
	public static void cancelActiveJob(AbstractWebContext context) {
		String id = (String) context.getRequest().getSession().getAttribute("activeJob");
		if ( id != null ) {
			AlignmentJob job = (AlignmentJob)context.getRequest().getSession().getAttribute(id);
			if ( job != null ) {
				job.cancel();
			}
			// Cancel Job
			context.getRequest().getSession().setAttribute(id, null);
			System.gc();
		}
	}
	public static void setActiveJob(AbstractWebContext context, Job job) {
		context.getRequest().getSession().setAttribute(job.getId().toString(), job);
		context.getRequest().getSession().setAttribute("activeJob", job.getId().toString());
	}
}
