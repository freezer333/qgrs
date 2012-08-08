package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.input.QParam;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class Message extends AbstractController {

	public Message() {
		super();
		this.supportedUrls.add("/app/message");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    pageXml.addContent(root);
	    Element message = new Element("message");
	    message.setText((String)context.get(QParam.AlertMessage));
	    root.addContent(message);
	    return new PageResponse(new ModelView("message.xsl", pageXml));	
	}
}
