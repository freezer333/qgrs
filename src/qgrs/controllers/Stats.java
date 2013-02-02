package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.compute.stat.Runner;
import qgrs.compute.stat.qgrs.user.SuiteRunner;
import qgrs.model.DbCriteria;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.ErrorResponse;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class Stats extends AbstractController {

	public Stats() {
		super();
		this.supportedUrls.add("/app/analysis");
	}
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    QgrsWebContext qContext = (QgrsWebContext)context;
	    DbCriteria dbCriteria = new DbCriteria(qContext);
	    
	    try {
		    for (Runner r : SuiteRunner.runners ) {
		    	Element re = new Element("runner");
		    	re.addContent(new Element("tableName").setText(r.getTableName()));
		    	re.addContent(new Element("description").setText(r.getDescription()));
		    	root.addContent(re);
		    }
		}
	    catch (Exception e) {
	    	e.printStackTrace();
			return new ErrorResponse(XslViews.Error, context.getResourceResolver(), "Error generating page - " + e.getMessage());
	    }
	    root.addContent(dbCriteria.getXmlElement());
	    root.addContent(qContext.getSpeciesDropdownElement());
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.StatsHome, pageXml));
	}
}
