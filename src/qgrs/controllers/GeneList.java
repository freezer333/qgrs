package qgrs.controllers;

import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.data.GeneSequence;
import qgrs.data.query.GeneQuery;
import qgrs.db.GeneSequenceDb;
import qgrs.db.GeneSequenceDb.HomologSide;
import qgrs.db.HomologyRecordDb;
import qgrs.input.QParam;
import qgrs.model.DbCriteria;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
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
	}

}
