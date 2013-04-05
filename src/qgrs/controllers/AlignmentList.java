package qgrs.controllers;

import java.util.Arrays;
import java.util.Collection;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.db.query.genehomology.GeneHomologyQuery;
import qgrs.db.query.genehomology.GeneHomologyQueryExecution;
import qgrs.db.query.genehomology.GeneHomologyQueryResult;
import qgrs.model.DbCriteria;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.authentication.Role;
import framework.web.response.ErrorResponse;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class AlignmentList extends AbstractController {
	@Override
	public boolean requiresAuthentication() {
		return true;
	}
	@Override
	public Collection<Role> getAuthorizedRoles() {
		return Arrays.asList(new Role[] { new Role("user")});
	}
	
	public AlignmentList() {
		super();
		this.supportedUrls.add("/app/align-list");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    QgrsWebContext qContext = (QgrsWebContext)context;
	    DbCriteria dbCriteria = new DbCriteria(qContext);
	    
	    try {
		    GeneHomologyQuery query = new GeneHomologyQuery();
		    GeneHomologyQueryExecution execution = new GeneHomologyQueryExecution();
		    execution.execute(qContext.getDbConnection(), query, dbCriteria);
		    Element results = new Element("results");
		    for ( GeneHomologyQueryResult result: execution.getResults() ) {
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
	    return new PageResponse(new ModelView(XslViews.AlignmentList, pageXml));	
	}
	
	
}
