package qgrs.controllers;

import java.util.Arrays;
import java.util.Collection;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.compute.stat.db.AnalysisRecord;
import qgrs.compute.stat.qgrs.QgrsRunner;
import qgrs.compute.stat.qgrs.WorkbookBuilder;
import qgrs.compute.stat.qgrs.user.SuiteRunner;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.authentication.Role;
import framework.web.response.ErrorResponse;
import framework.web.response.ExcelResponse;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class StatsSummary extends AbstractController {

	@Override
	public boolean requiresAuthentication() {
		return true;
	}
	@Override
	public Collection<Role> getAuthorizedRoles() {
		return Arrays.asList(new Role[] { new Role("user")});
	}
	
	public StatsSummary() {
		super();
		this.supportedUrls.add("/app/statssummary");
	}
	@Override
	public Response processRequest(AbstractWebContext context) {
		QgrsWebContext qContext = (QgrsWebContext)context;
	    String id = context.getFromRequest("id");
	    Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    try {
	    	AnalysisRecord r = AnalysisRecord.loadAnalysis(id, qContext.getDbConnection().getConnection());
	    	root.addContent(r.getXmlElement());
	 	    root.addContent(qContext.getSpeciesDropdownElement());
	 	    pageXml.addContent(root);
	 	    return new PageResponse(new ModelView(XslViews.StatsSummary, pageXml));
		}
	    catch (Exception e) {
	    	e.printStackTrace();
			return new ErrorResponse(XslViews.Error, context.getResourceResolver(), "Error generating page - " + e.getMessage());
	    }
	   
	}
}
