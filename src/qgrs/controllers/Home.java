package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.PageResponse;
import framework.web.response.Response;


public class Home extends AbstractController {

	public Home() {
		super();
		this.supportedUrls.add("/app/home");

	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.Home, pageXml));
		
	}
	
}
