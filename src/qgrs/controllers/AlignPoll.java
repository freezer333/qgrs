package qgrs.controllers;


import qgrs.input.QParam;
import qgrs.job.AlignmentJob;
import qgrs.job.JobStage;
import qgrs.job.ProgressReport;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.response.JsonResponse;
import framework.web.response.Response;
import framework.web.util.StringUtils;

public class AlignPoll extends AbstractController {

	public AlignPoll() {
		super();
		this.supportedUrls.add("/app/align-poll");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		String id = context.getString(QParam.JobId);
		if ( StringUtils.isDefined(id)) {
			AlignmentJob aj = (AlignmentJob) context.getRequest().getSession().getAttribute(id);
			if ( aj != null ) {
				return new JsonResponse(aj.getProgressReport());
			}
		}
		if ( context.getRequest().getSession().isNew()) {
			ProgressReport epr = new ProgressReport(JobStage.Error, -1, "Job [" + id + "] cannot be found, the session is new");
			return new JsonResponse(epr);
		}
		if ( id == null ) id = "NULL";
		ProgressReport epr = new ProgressReport(JobStage.Error, -1, "Job [" + id + "] cannot be found");
		return new JsonResponse(epr);
		
	}

}
