package qgrs.controllers;

import qgrs.model.JobContext;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.response.RedirectResponse;
import framework.web.response.Response;


public class AlignCancel extends AbstractController {

	public AlignCancel() {
		super();
		this.supportedUrls.add("/app/align-cancel");
		
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		JobContext.cancelActiveJob(context);
		return new RedirectResponse("start");
		
	}

}
