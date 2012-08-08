package qgrs.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.biojavax.bio.db.ncbi.GenbankRichSequenceDB;
import org.biojavax.bio.seq.RichSequence;
import org.jdom.Document;
import org.jdom.Element;

import qgrs.data.GQuadruplexRecord;
import qgrs.data.GeneSequence;
import qgrs.data.query.GeneQuery;
import qgrs.data.query.QgrsQuery;
import qgrs.db.GenbankRichSequenceTextDB;
import qgrs.db.GeneSequenceDb;
import qgrs.db.QgrsDb;
import qgrs.model.DbCriteria;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class QgrsList extends AbstractController {

	public QgrsList() {
		super();
		this.supportedUrls.add("/app/quadruplex-list");
	}
	
	private String getSequence(String id) {
		GenbankRichSequenceDB  ncbi = new GenbankRichSequenceTextDB();
		RichSequence rs;
		try {
			rs = ncbi.getRichSequence(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		 return rs.seqString();
	}
	
	
	
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    QgrsWebContext qContext = (QgrsWebContext)context;
	    DbCriteria dbCriteria = new DbCriteria(qContext);
	    GeneSequenceDb seqDb = new GeneSequenceDb(qContext.getDbConnection());
	    
	    GeneQuery geneQuery = dbCriteria.buildGeneQueryForSingleSideView();
	    QgrsQuery qgrsQuery = dbCriteria.buildQgrsQueryForSingleQgrs();
	    qgrsQuery.setGeneClause(geneQuery);
	    QgrsDb qgrsDb = new QgrsDb(qContext.getDbConnection());
	    
	    int count = qgrsDb.getRecordCount(qgrsQuery);
	    PageHelper pager = new PageHelper(dbCriteria, count);
	    List<GQuadruplexRecord> quads = qgrsDb.getRecords(qgrsQuery, dbCriteria.getPageLimit(), pager.getComputedOffset());
	    
	    HashMap<String, GeneSequence> geneMap = new ResultHelper().buildGeneMapFromQuadruplexes(quads, seqDb);
	    
	    for ( GQuadruplexRecord q : quads ) {
	    	root.addContent(q.getXmlElement(geneMap.get(q.getGeneAccessionNumber())));
	    }
	    qgrsDb.close();
	    seqDb.close();
	    root.addContent(dbCriteria.getXmlElement());
	    root.addContent(qContext.getSpeciesDropdownElement());
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.QgrsList, pageXml));	
	}


}
