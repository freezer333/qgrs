package qgrs.view;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import qgrs.db.AppProperties;
import qgrs.input.QParam;
import qgrs.model.QgrsWebContext;
import framework.db.DatabaseConnectionParameters;
import framework.web.AbstractController;
import framework.web.AbstractDispatch;
import framework.web.AbstractParam;
import framework.web.AbstractWebContext;
import framework.web.ResourceResolver;

public class Dispatcher extends AbstractDispatch {

	
	
	public Dispatcher(HttpServlet servlet) {
		super(servlet);
		
	}
	
	@Override
	protected void applyRedirectForAuthentication(AbstractWebContext context) {
		throw new RuntimeException("Authentication not implemented for QGRS");
	}

	@Override
	protected boolean redirectForAuthentication(AbstractWebContext context, AbstractController controller) {
		return false;// no authentication yet for qgrs
	}

	@Override
	protected AbstractWebContext buildContext(HttpServlet dispatchServlet, HttpServletRequest request, HttpServletResponse response) {
		return new QgrsWebContext(dispatchServlet, request, response, this.getDbConnection(), this);
	}
	
	@Override
	protected List<AbstractParam> getPersistableParamList() {
		return QParam.asList();
	}

	@Override
	protected String getErrorView() {
		return XslViews.Error;
	}

	@Override
	protected String getControllerPackage() {
		return "qgrs.controllers";
	}

	@Override
	protected DatabaseConnectionParameters getDbParams() {
		DatabaseConnectionParameters params = new DatabaseConnectionParameters(AppProperties.getConnectionString(new ResourceResolver(this.servlet.getServletContext())), "sa", "sa");
		return params;
	}

}
