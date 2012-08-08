package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.data.GeneSequence;
import qgrs.db.GeneSequenceDb;
import qgrs.model.DbCriteria;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class GeneDetails extends AbstractController {

	public GeneDetails() {
		super();
		this.supportedUrls.add("/app/geneDetails");
	}

	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    QgrsWebContext qContext = (QgrsWebContext)context;
	    DbCriteria dbCriteria = new DbCriteria(qContext);
	    
	    GeneSequenceDb geneDb = new GeneSequenceDb (qContext.getDbConnection());
	    GeneSequence s = geneDb.get(qContext.getString("id"));
	    root.addContent(s.getPageXmlElement(""));
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.GeneDetails, pageXml));	
	}
}
