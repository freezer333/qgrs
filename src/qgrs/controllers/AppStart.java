package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import qgrs.input.ResultViewParams;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.ResourceType;
import framework.web.response.PageResponse;
import framework.web.response.Response;
import framework.web.util.StringUtils;

public class AppStart extends AbstractController {

	public AppStart() {
		super();
		this.supportedUrls.add("/app/start");
		
	}
	
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    pageXml.addContent(root);
	   
	    // Note, we don't use the existing context to construct
	    // the params on the start page, we want to use default
	    ResultViewParams rvp = new ResultViewParams();
	    root.addContent(rvp.getXml("MAX"));
	    
	    SAXBuilder sb = new SAXBuilder();
        try {
            Document pairDoc = sb.build(context.getResourceResolver().getResourceFile(ResourceType.xml, "pairs.xml"));
            Element pairRoot = pairDoc.getRootElement();
            pairDoc.removeContent(pairRoot);
            root.addContent(pairRoot);
            
            Document propsDoc = sb.build(context.getResourceResolver().getResourceFile(ResourceType.xml, "props.xml"));
            Element propsRoot = propsDoc.getRootElement();
            Element nextMaintenance = propsRoot.getChild("AlertMessage");
            if ( nextMaintenance != null ) {
            	if ( StringUtils.isDefined(nextMaintenance.getText())) {
            		Element nm = new Element("AlertMessage");
            		nm.setAttribute("show", "true");
            		nm.setText(nextMaintenance.getText());
            		root.addContent(nm);
            	}
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
	    return new PageResponse(new ModelView(XslViews.Start, pageXml));
	}
}
