package qgrs.controllers;

import java.util.Arrays;
import java.util.Collection;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.compute.stat.db.AnalysisRecord;
import qgrs.compute.stat.db.RecordKey;
import qgrs.compute.stat.db.ResultRecord;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.authentication.Role;
import framework.web.response.ErrorResponse;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class StatsLocationDetails extends AbstractController {
	@Override
	public boolean requiresAuthentication() {
		return true;
	}
	@Override
	public Collection<Role> getAuthorizedRoles() {
		return Arrays.asList(new Role[] { new Role("user")});
	}
	public StatsLocationDetails() {
		super();
		this.supportedUrls.add("/app/locationdetails");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		QgrsWebContext qContext = (QgrsWebContext)context;
	    String id = context.getFromRequest("analysis");
	    String partition = context.getFromRequest("partition");
	    int series = Integer.parseInt(context.getFromRequest("series"));
	    int location = Integer.parseInt(context.getFromRequest("location"));
	    RecordKey key = new RecordKey(partition, series, location);
	    Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    pageXml.addContent(root);
	    try {
	    	AnalysisRecord ar = AnalysisRecord.loadAnalysis(id, qContext.getDbConnection().getConnection());
	    	ResultRecord result = ar.results.get(key);
	    	Element element = new Element("result");
			key.writeElement(element);
			result.writeElement(element);
			root.addContent(element);
			element.addContent(ar.buildResultRecordElement(key));
	    	return new PageResponse(new ModelView(XslViews.StatsLocationDetails, pageXml));
		}
	    catch (Exception e) {
	    	e.printStackTrace();
			return new ErrorResponse(XslViews.Error, context.getResourceResolver(), "Error generating page - " + e.getMessage());
	    }
	   
	}
}
