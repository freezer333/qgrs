package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class ActiveJob extends AbstractController {

	public ActiveJob() {
		super();
		this.supportedUrls.add("/app/activeJob");
		
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.ActiveJob, pageXml));	
	}
}
