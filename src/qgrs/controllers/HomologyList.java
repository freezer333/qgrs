package qgrs.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.data.GQuadruplexRecord;
import qgrs.data.GeneSequence;
import qgrs.data.QgrsHomologyRecord;
import qgrs.data.query.AlignmentQuery;
import qgrs.data.query.GeneQuery;
import qgrs.data.query.HomologyQuery;
import qgrs.data.query.QgrsQuery;
import qgrs.db.GeneSequenceDb;
import qgrs.db.HomologyRecordDb;
import qgrs.db.QgrsDb;
import qgrs.input.QParam;
import qgrs.model.DbCriteria;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
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
		HomologyRecordDb hDb = new HomologyRecordDb(qContext.getDbConnection());
		QgrsDb qgrsDb = new QgrsDb(qContext.getDbConnection());
		GeneSequenceDb seqDb = new GeneSequenceDb(qContext.getDbConnection());
	    
		try {
			Document pageXml = new Document();
		    Element root = new Element("qgrs");
		    
		    DbCriteria dbCriteria = new DbCriteria(qContext);
		    
		    /* Build Query Object */
		    GeneQuery principleGeneQuery = dbCriteria.buildPrincipleGeneQuery();
			GeneQuery comparisonGeneQuery = dbCriteria.buildComparisonGeneQuery();
			
			QgrsQuery principleQgrsQuery = dbCriteria.buildPrincipleQgrsClause();
			QgrsQuery comparisonQgrsQuery = dbCriteria.buildComparisonQgrsClause();
			
			principleQgrsQuery.setGeneClause(principleGeneQuery);
			comparisonQgrsQuery.setGeneClause(comparisonGeneQuery);
			
			AlignmentQuery alignmentQuery = new AlignmentQuery(Float.parseFloat(dbCriteria.get(QParam.Db_MinAlignmentScore)), 
					principleGeneQuery, comparisonGeneQuery);
		    
			HomologyQuery homologyQuery = dbCriteria.buildQgrsHomologyQuery();
			homologyQuery.setPrincipleQgrsQuery(principleQgrsQuery);
			homologyQuery.setComparisonQgrsQuery(comparisonQgrsQuery);
			homologyQuery.setAlignmentQuery(alignmentQuery);
			
			
			
			int count =  hDb.getCount(homologyQuery);
			PageHelper pager = new PageHelper(dbCriteria, count);
		    List<QgrsHomologyRecord> results = hDb.get(homologyQuery, dbCriteria.getPageLimit(), pager.getComputedOffset());
		    Map<String, GQuadruplexRecord> quadMap = new ResultHelper().buildQuadruplexList(results, qgrsDb);
		    HashMap<String, GeneSequence> geneMap = new ResultHelper().buildGeneMapFromQuadruplexes(quadMap.values(), seqDb);
			
		    for ( QgrsHomologyRecord qhr : results ) {
		    	Element e = qhr.getXmlElement();
		    	Element p = new Element("principle");
		    	Element c = new Element("comparison");
		    	p.addContent(quadMap.get(qhr.getGq1Id()).getXmlElement());
		    	c.addContent(quadMap.get(qhr.getGq2Id()).getXmlElement());
		    	p.addContent(geneMap.get(quadMap.get(qhr.getGq1Id()).getGeneAccessionNumber()).getXmlElement());
		    	c.addContent(geneMap.get(quadMap.get(qhr.getGq2Id()).getGeneAccessionNumber()).getXmlElement());
		    	e.addContent(p);
		    	e.addContent(c);
		    	root.addContent(e);
		    }
		    	    
		    root.addContent(dbCriteria.getXmlElement());
		    root.addContent(qContext.getSpeciesDropdownElement());
		    pageXml.addContent(root);
		    return new PageResponse(new ModelView(XslViews.HomologyList, pageXml));
		} 
		finally {
			seqDb.close();
			qgrsDb.close();
		    hDb.close();

		}
	}

}
