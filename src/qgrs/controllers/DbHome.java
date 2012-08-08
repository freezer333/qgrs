package qgrs.controllers;

import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.data.GeneSequence;
import qgrs.data.query.GeneQuery;
import qgrs.db.GeneSequenceDb;
import qgrs.db.GeneSequenceDb.HomologSide;
import qgrs.input.QParam;
import qgrs.model.DbCriteria;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class DbHome  extends AbstractController {
	public DbHome() {
		super();
		this.supportedUrls.add("/app/dbhome");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.DbHome, pageXml));	
	}
}
