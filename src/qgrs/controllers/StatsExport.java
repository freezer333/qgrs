package qgrs.controllers;

import qgrs.compute.stat.db.AnalysisRecord;
import qgrs.compute.stat.qgrs.QgrsRunner;
import qgrs.compute.stat.qgrs.WorkbookBuilder;
import qgrs.compute.stat.qgrs.user.SuiteRunner;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.response.ErrorResponse;
import framework.web.response.ExcelResponse;
import framework.web.response.Response;

public class StatsExport extends AbstractController {

	public StatsExport() {
		super();
		this.supportedUrls.add("/app/statsexport");
	}
	@Override
	public Response processRequest(AbstractWebContext context) {
		QgrsWebContext qContext = (QgrsWebContext)context;
	    String id = context.getFromRequest("id");
	    try {
	    	AnalysisRecord r = AnalysisRecord.loadAnalysis(id, qContext.getDbConnection().getConnection());
		    WorkbookBuilder export = new WorkbookBuilder(r);
		    return new ExcelResponse(id, export.makeWorkbook(qContext.getDbConnection().getConnection()), ExcelResponse.Type.xlsx);
		}
	    catch (Exception e) {
	    	e.printStackTrace();
			return new ErrorResponse(XslViews.Error, context.getResourceResolver(), "Error generating page - " + e.getMessage());
	    }
	}
}
