package qgrs.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.data.AlignmentRecord;
import qgrs.data.query.AlignmentListing;
import qgrs.db.AlignmentRecordDb;
import qgrs.model.DbCriteria;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
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
	    AlignmentRecordDb db = new AlignmentRecordDb (qContext.getDbConnection());
	    
	    List<AlignmentListing> listing = AlignmentListing.list(qContext.getDbConnection(), dbCriteria);
	    
	    for ( AlignmentListing s : listing ) {
	    	root.addContent(s.getXmlElement());
	    }
	    /*Collections.sort(listing, new AlignmentListingComparator_PrincipleSpecies());
	    int skipped = 0;
	    int added = 0;
	    PageHelper pager = new PageHelper(dbCriteria, listing.size());
	    for ( AlignmentListing s : listing) {
	    	if ( skipped < pager.getComputedOffset() ) {
	    		skipped++;
	    	}
	    	else if ( added < dbCriteria.getPageLimit())  {
	    		root.addContent(s.getXmlElement());
	    		added++;
	    	}
	    }*/
	    db.close();
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
