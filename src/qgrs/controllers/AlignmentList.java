package qgrs.controllers;

import java.util.Comparator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.data.query.AlignmentListing;
import qgrs.data.query.genehomology.GeneHomologyQuery;
import qgrs.data.query.genehomology.GeneHomologyQueryExecution;
import qgrs.data.query.genehomology.GeneHomologyQueryResult;
import qgrs.db.AlignmentRecordDb;
import qgrs.model.DbCriteria;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.ErrorResponse;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class AlignmentList extends AbstractController {

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
	    /*
	    
	    AlignmentRecordDb db = new AlignmentRecordDb (qContext.getDbConnection());
	    
	    List<AlignmentListing> listing = AlignmentListing.list(qContext.getDbConnection(), dbCriteria);
	    
	    for ( AlignmentListing s : listing ) {
	    	root.addContent(s.getXmlElement());
	    }
	   
	    db.close();
	   */ 
	    root.addContent(dbCriteria.getXmlElement());
	    root.addContent(qContext.getSpeciesDropdownElement());
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.AlignmentList, pageXml));	
	}
	
	class AlignmentListingComparator_PrincipleSpecies implements Comparator<AlignmentListing>{

		@Override
		public int compare(AlignmentListing arg0, AlignmentListing arg1) {
			AlignmentListing a1 = (AlignmentListing) arg0;
			AlignmentListing a2  =(AlignmentListing) arg1;
			return a1.getPrincipleGeneName().compareTo(a2.getPrincipleGeneName());
		}
		
		
	}

}
