package qgrs.controllers;

import java.net.UnknownHostException;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.data.providers.MongoAlignmentProvider;
import qgrs.db.DatabaseConnection;
import qgrs.input.FlexibleInputProvider;
import qgrs.input.ResultViewParams;
import qgrs.job.AlignmentJob;
import qgrs.model.JobContext;
import qgrs.model.QgrsWebContext;
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
		
		
		DatabaseConnection c = new DatabaseConnection(((QgrsWebContext)context).getFreeConnection());
		AlignmentJob job;
		try {
			job = new AlignmentJob(new FlexibleInputProvider(context, c), xrp, new MongoAlignmentProvider());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ErrorResponse(XslViews.Error, context.getResourceResolver(), "Error acquiring alignment -> " + e.getMessage());
		}
		
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
