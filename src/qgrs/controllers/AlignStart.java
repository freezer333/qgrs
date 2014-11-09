package qgrs.controllers;

import java.net.UnknownHostException;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.data.providers.NoCacheAlignmentProvider;
import qgrs.input.FlexibleInputProvider;
import qgrs.input.ResultViewParams;
import qgrs.job.AlignmentJob;
import qgrs.model.JobContext;
import qgrs.output.XmlResultProcessor;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.ErrorResponse;
import framework.web.response.PageResponse;
import framework.web.response.RedirectResponse;
import framework.web.response.Response;

public class AlignStart extends AbstractController {

	public AlignStart() {
		super();
		this.supportedUrls.add("/app/align-start");
		
	}
	
	
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		ResultViewParams rvp = new ResultViewParams(context);
		XmlResultProcessor xrp = new XmlResultProcessor();
		xrp.setViewParams(rvp);
		if ( JobContext.hasActiveJob(context)) {
			return new RedirectResponse("activeJob");
		}
		
		
		
		AlignmentJob job;
		job = new AlignmentJob(new FlexibleInputProvider(context), xrp, new NoCacheAlignmentProvider());
		
		JobContext.cancelActiveJob(context);
		Thread t = new Thread(job);
		t.start();
		JobContext.setActiveJob(context, job);
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    root.setAttribute("jobId", job.getId().toString());
	    pageXml.addContent(root);
	    root.addContent(rvp.getXml("MAX"));
	    return new PageResponse(new ModelView(XslViews.Progress, pageXml));
	}

}
