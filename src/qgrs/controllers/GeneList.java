package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.db.query.gene.GeneQuery;
import qgrs.db.query.gene.GeneQueryExecution;
import qgrs.db.query.gene.GeneQueryResult;
import qgrs.model.DbCriteria;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.ErrorResponse;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class GeneList extends AbstractController {

	public GeneList() {
		super();
		this.supportedUrls.add("/app/gene-list");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    QgrsWebContext qContext = (QgrsWebContext)context;
	    DbCriteria dbCriteria = new DbCriteria(qContext);
	    
	    try {
		    GeneQuery query = new GeneQuery();
		    GeneQueryExecution execution = new GeneQueryExecution();
		    execution.execute(qContext.getDbConnection(), query, dbCriteria);
		    Element results = new Element("results");
		    for ( GeneQueryResult result: execution.getResults() ) {
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
	    return new PageResponse(new ModelView(XslViews.GeneList, pageXml));
	}

}
