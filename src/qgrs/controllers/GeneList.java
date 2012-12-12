package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.data.query.gene.GeneQuery;
import qgrs.data.query.gene.GeneQueryExecution;
import qgrs.data.query.gene.GeneQueryResult;
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
	    /*
	    
	    GeneQuery geneQuery = dbCriteria.buildGeneQueryForSingleSideView();
	    GeneSequenceDb geneDb = new GeneSequenceDb (qContext.getDbConnection());
	    HomologyRecordDb hDb = new HomologyRecordDb(qContext.getDbConnection());
	    try {
	    int count = geneDb.getCount(geneQuery);
	    PageHelper pager = new PageHelper(dbCriteria, count);
	    
	    List<GeneSequence> sequences = geneDb.getAll(geneQuery, dbCriteria.getPageLimit(), pager.getComputedOffset());
	    HomologSide side = "comparison".equalsIgnoreCase(dbCriteria.get(QParam.Db_FilterSide)) ? HomologSide.comparison : HomologSide.principle;
		Map<String, Integer> qgrsCountMap = new ResultHelper().buildQgrsCountMap(sequences, geneDb, dbCriteria.buildQgrsQueryForSingleQgrs());
	    Map<String, Integer> homologCountMap = new ResultHelper().buildHomologCountMap(sequences, hDb, side, dbCriteria);
	    for ( GeneSequence s : sequences) {
	    	Element e = s.getXmlElement();
	    	int qgrsCount = qgrsCountMap.containsKey(s.getAccessionNumber()) ? qgrsCountMap.get(s.getAccessionNumber()) : 0;
	    	int homologCount = homologCountMap.containsKey(s.getAccessionNumber()) ? homologCountMap.get(s.getAccessionNumber()) : 0;
	    	e.addContent(new Element("qgrsCount").setText(String.valueOf(qgrsCount)));
	    	e.addContent(new Element("mrnaHomologueCount").setText(String.valueOf(homologCount)));
	    	root.addContent(e);
	    }
	   
	    root.addContent(dbCriteria.getXmlElement());
	    root.addContent(qContext.getSpeciesDropdownElement());
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.GeneList, pageXml));
	    } finally {
	    	hDb.close();
	    	geneDb.close();
	    }
	     */
	}

}
