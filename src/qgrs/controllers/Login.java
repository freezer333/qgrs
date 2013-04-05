package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class Login extends AbstractController {

	public Login() {
		super();
		this.supportedUrls.add("/app/login");
	}
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    String redirect = context.inSession("authredirect") ? (String)context.getFromSession("authredirect") : "start";
	    root.addContent(new Element("redirect").setText(redirect));
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.Login, pageXml));
	}
}
