package qgrs.controllers;


import qgrs.input.QParam;
import qgrs.job.AlignmentJob;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.response.ErrorResponse;
import framework.web.response.Response;
import framework.web.util.StringUtils;

public class AlignError extends AbstractController {

	public AlignError() {
		super();
		this.supportedUrls.add("/app/align-error");
		
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		String id = context.getString(QParam.JobId);
		Throwable t = null;
		if ( StringUtils.isDefined(id)) {
			AlignmentJob aj = (AlignmentJob) context.getRequest().getSession().getAttribute(id);
			if ( aj != null ) {
				// Cancel the job
				t = aj.getLastError();
				aj.cancel();
			}
		}
		return new ErrorResponse(XslViews.Error, context.getResourceResolver(), "A computational error has occurred, the information below may be helpful for developers when tracking down the cause of the error", t);
		
	}

}
