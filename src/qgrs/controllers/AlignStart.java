package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.db.Cache;
import qgrs.db.DatabaseConnection;
import qgrs.db.LocalGeneCache;
import qgrs.input.FlexibleInputProvider;
import qgrs.input.QParam;
import qgrs.input.ResultViewParams;
import qgrs.job.AlignmentJob;
import qgrs.model.JobContext;
import qgrs.model.QgrsWebContext;
import qgrs.output.XmlResultProcessor;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.PageResponse;
import framework.web.response.RedirectResponse;
import framework.web.response.Response;

public class AlignStart extends AbstractController {

	public AlignStart() {
		super();
		this.supportedUrls.add("/app/align-start");
		
	}
	
	private Response preCheckInput(AbstractWebContext context) {
		Cache localGeneCache = new LocalGeneCache(((QgrsWebContext)context).getDbConnection());
		FlexibleInputProvider provider = new FlexibleInputProvider(context, localGeneCache);
		String result = provider.preCheck();
		if ( result != null ) {
			context.put(QParam.AlertMessage, result);
			return new RedirectResponse("message");
		}
		return null;
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		System.out.println(context.getRequest().getRemoteHost());
		ResultViewParams rvp = new ResultViewParams(context);
		XmlResultProcessor xrp = new XmlResultProcessor();
		xrp.setViewParams(rvp);
		if ( JobContext.hasActiveJob(context)) {
			return new RedirectResponse("activeJob");
		}
		Response rr = this.preCheckInput(context);
		if ( rr != null ) {
			return rr;
		}
		
		//Cache cache = new NullCache();//new ReadWriteCache(new DatabaseConnection(context.getFreeConnection()));
		Cache cache = new LocalGeneCache(new DatabaseConnection(((QgrsWebContext)context).getFreeConnection()));
		AlignmentJob job = new AlignmentJob(new FlexibleInputProvider(context, cache), xrp, cache);
		
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
