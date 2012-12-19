package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.db.AlignmentRecordDb;
import qgrs.db.GeneSequenceDb;
import qgrs.db.HomologyRecordDb;
import qgrs.db.QgrsDb;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class DatabaseStats extends AbstractController {

	public DatabaseStats() {
		super();
		this.supportedUrls.add("/app/dbStats");
	}

	
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    QgrsWebContext qContext = (QgrsWebContext)context;
	    
	    
	    AlignmentRecordDb alignmentdb = new AlignmentRecordDb (qContext.getDbConnection());
	    HomologyRecordDb hDb = new HomologyRecordDb(qContext.getDbConnection());
		QgrsDb qgrsDb = new QgrsDb(qContext.getDbConnection());
		GeneSequenceDb seqDb = new GeneSequenceDb(qContext.getDbConnection());
		
		
		
	   
		Element stat = new Element("stats");
		stat.addContent(new Element("numGene").setText(String.valueOf(seqDb.getCount())));
		stat.addContent(new Element("numGeneH").setText(String.valueOf(alignmentdb.getCount())));
		stat.addContent(new Element("numQgrs").setText(String.valueOf(qgrsDb.getCount())));
		stat.addContent(new Element("numQgrsH").setText(String.valueOf(hDb.getCount())));
		root.addContent(stat);
	    pageXml.addContent(root);
	    
	    alignmentdb.close();
	    hDb.close();
	    qgrsDb.close();
	    seqDb.close();
	    return new PageResponse(new ModelView(XslViews.DbStats, pageXml));	
	}
	
	

}
