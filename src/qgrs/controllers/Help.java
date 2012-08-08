package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.ResourceType;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class Help extends AbstractController {

	public Help() {
		super();
		this.supportedUrls.add("/app/help");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    Element helpKey = new Element("helpKey");
	    helpKey.setText(context.getFromRequest("helpKey"));
	    root.addContent(helpKey);
	    root.addContent(getGlossary(context));
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.Help, pageXml));
		
	}
	
	private Element getGlossary(AbstractWebContext context) {
		SAXBuilder sb = new SAXBuilder();
		try {
            Document pairDoc = sb.build(context.getResourceResolver().getResourceFile(ResourceType.xml, "glossary.xml"));
            Element glossary = pairDoc.getRootElement();
            pairDoc.removeContent(glossary);//detach so it can be added to pageXml
            return glossary;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
		
		return null;
	}

}
