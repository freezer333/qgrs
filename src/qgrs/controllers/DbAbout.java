package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.PageResponse;
import framework.web.response.Response;


public class DbAbout extends AbstractController {

	public DbAbout() {
		super();
		this.supportedUrls.add("/app/dbabout");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    pageXml.addContent(root);
	    ModelView mv = new ModelView(XslViews.DbAbout, pageXml);
	    return new PageResponse(mv);	
	}
}
