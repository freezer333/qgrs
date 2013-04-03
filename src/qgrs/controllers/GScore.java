package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import com.google.common.base.Strings;

import qgrs.compute.gscore.QgrsBuilder;
import qgrs.compute.gscore.QgrsCandidate;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.ErrorResponse;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class GScore extends AbstractController {

	public GScore() {
		super();
		this.supportedUrls.add("/app/gscore");
	}
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    String seq = context.getFromRequest("seq");
	    if ( !Strings.isNullOrEmpty(seq)) {
		    try {
		    	QgrsBuilder b = new QgrsBuilder(seq);
		    	for ( QgrsCandidate c : b.findQgrs() ) {
		    		root.addContent(c.getXmlElement());
		    	}
			}
		    catch (Exception e) {
		    	e.printStackTrace();
				return new ErrorResponse(XslViews.Error, context.getResourceResolver(), "Error generating page - " + e.getMessage());
		    }
	    }
	    root.addContent(new Element("seq").setText(seq));
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.GScore, pageXml));
	}
}
