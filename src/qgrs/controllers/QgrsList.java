package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.db.query.qgrs.QgrsQuery;
import qgrs.db.query.qgrs.QgrsQueryExecution;
import qgrs.db.query.qgrs.QgrsQueryResult;
import qgrs.model.DbCriteria;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.ErrorResponse;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class QgrsList extends AbstractController {

	public QgrsList() {
		super();
		this.supportedUrls.add("/app/quadruplex-list");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    
	    QgrsWebContext qContext = (QgrsWebContext)context;
	    DbCriteria dbCriteria = new DbCriteria(qContext);
	    
	    try {
		    QgrsQuery query = new QgrsQuery();
		    QgrsQueryExecution execution = new QgrsQueryExecution();
		    execution.execute(qContext.getDbConnection(), query, dbCriteria);
		    Element results = new Element("results");
		    for ( QgrsQueryResult result: execution.getResults() ) {
		    	results.addContent(result.getXmlElement());
		    }
		   
		    root.addContent(results);
		    
		}
	    catch (Exception e) {
	    	e.printStackTrace();
			return new ErrorResponse(XslViews.Error, context.getResourceResolver(), "Query failed - " + e.getMessage());
	    }
	    
	    
	    root.addContent(dbCriteria.getXmlElement());
	    root.addContent(qContext.getSpeciesDropdownElement());
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.QgrsList, pageXml));	
	}


}
