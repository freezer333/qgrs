package qgrs.controllers;

import java.text.SimpleDateFormat;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.compute.stat.db.AnalysisRecord;
import qgrs.model.DbCriteria;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.ErrorResponse;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class Stats extends AbstractController {

	public Stats() {
		super();
		this.supportedUrls.add("/app/analysis");
	}
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    QgrsWebContext qContext = (QgrsWebContext)context;
	    DbCriteria dbCriteria = new DbCriteria(qContext);
	    SimpleDateFormat df = new SimpleDateFormat("mm/dd/yyyy:hh:mm:ss");
	    try {
		    for (AnalysisRecord r : AnalysisRecord.getAllAnalysis_shallow(qContext.getDbConnection().getConnection())) {
		    	Element re = new Element("analysis");
		    	re.addContent(new Element("id").setText(r.id));
		    	re.addContent(new Element("description").setText(r.description));
		    	re.addContent(new Element("active").setText(String.valueOf(r.active)));
		    	re.addContent(new Element("date").setText(df.format(r.date)));
		    	root.addContent(re);
		    }
		}
	    catch (Exception e) {
	    	e.printStackTrace();
			return new ErrorResponse(XslViews.Error, context.getResourceResolver(), "Error generating page - " + e.getMessage());
	    }
	    root.addContent(dbCriteria.getXmlElement());
	    root.addContent(qContext.getSpeciesDropdownElement());
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.StatsHome, pageXml));
	}
}
