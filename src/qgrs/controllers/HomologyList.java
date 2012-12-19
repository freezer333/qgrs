package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.db.query.qgrshomology.QgrsHomologyQuery;
import qgrs.db.query.qgrshomology.QgrsHomologyQueryExecution;
import qgrs.db.query.qgrshomology.QgrsHomologyQueryResult;
import qgrs.model.DbCriteria;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.ErrorResponse;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class HomologyList extends AbstractController {

	public HomologyList() {
		super();
		this.supportedUrls.add("/app/homology-list");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		
		QgrsWebContext qContext = (QgrsWebContext)context;
		   
		try {
			Document pageXml = new Document();
		    Element root = new Element("qgrs");
		    
		    DbCriteria dbCriteria = new DbCriteria(qContext);
		    
		    QgrsHomologyQuery query = new QgrsHomologyQuery();
		    QgrsHomologyQueryExecution execution = new QgrsHomologyQueryExecution();
		    execution.execute(qContext.getDbConnection(), query, dbCriteria);
		    Element pairs = new Element("pairs");
		    for ( QgrsHomologyQueryResult result: execution.getResults() ) {
		    	pairs.addContent(result.getXmlElement());
		    }
		   
		    root.addContent(pairs);
		    root.addContent(dbCriteria.getXmlElement());
		    root.addContent(qContext.getSpeciesDropdownElement());
		    pageXml.addContent(root);
		    return new PageResponse(new ModelView(XslViews.HomologyList, pageXml));
		} 
		catch (Exception e) {
			e.printStackTrace();
			return new ErrorResponse(XslViews.Error, context.getResourceResolver(), "Query failed - " + e.getMessage());
		}
		
	}

}
