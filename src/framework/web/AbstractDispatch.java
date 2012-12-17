package framework.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.h2.jdbcx.JdbcConnectionPool;

import framework.db.DatabaseConnectionParameters;
import framework.web.response.ErrorResponse;
import framework.web.response.Response;
import framework.web.util.ClassFinder;

/* Testing HG INTENTIONAL MERGE PROBLEM*/

public abstract class AbstractDispatch {
	protected List<AbstractController> controllers;
	protected JdbcConnectionPool connectionPool;

	protected HttpServlet servlet;
	
	protected AbstractDispatch(HttpServlet servlet) {
		this.servlet = servlet;
		this.loadControllersFromPackage(this.getControllerPackage());
		this.initializeDatabaseConnectionPool();
		
	}

	/* MERGE COMMENT */
	protected abstract DatabaseConnectionParameters getDbParams();
	protected abstract String getControllerPackage();
	protected abstract String getErrorView() ;
	protected abstract AbstractWebContext buildContext(HttpServlet dispatchServlet, HttpServletRequest request, HttpServletResponse response);

	protected void initializeDatabaseConnectionPool() {
		DatabaseConnectionParameters dcParams = this.getDbParams();
		if ( dcParams != null ) {
			this.connectionPool = JdbcConnectionPool.create(dcParams.getConnectionString(), 
					dcParams.getUsername(), dcParams.getPassword());
		}
	}
	public Connection getDbConnection() {
		try {
			return this.connectionPool.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	protected void loadControllersFromPackage(String packageName) {
		controllers = new LinkedList<AbstractController>();
		ClassFinder finder = new ClassFinder(Thread.currentThread().getContextClassLoader());
		try {
			for ( Class c : finder.getClasses(packageName) ) {
				if ( AbstractController.class.isAssignableFrom(c) ) {
					controllers.add((AbstractController)c.newInstance());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	protected JdbcConnectionPool loadConnectionPool() {
		return JdbcConnectionPool.create(		            "jdbc:h2:~/test", "sa", "sa");
	}

	protected List<AbstractParam> getPersistableParamList() {
		return new LinkedList<AbstractParam>();
	}

	/**
	 * @param context  
	 * @param controller 
	 */
	protected boolean redirectForAuthentication(AbstractWebContext context, AbstractController controller) {
		return false;
	}
	/**
	 * @param context  
	 */
	protected void applyRedirectForAuthentication(AbstractWebContext context) {
		throw new RuntimeException ("Cannot redirect for authentication using AbstractDispatchServlet");
	}

	protected AbstractController mapUrl(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		String [] splitResult = url.split(contextPath);
		String path = splitResult[1];
		for ( AbstractController c: controllers ) {
			if ( c.servesPath(path)) return c;
		}
		return null;
	}



	public void doGet(HttpServlet dispatchServlet, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AbstractWebContext context = this.buildContext(dispatchServlet, request, response);

		try {

			AbstractController controller = this.mapUrl(request);
			if ( this.redirectForAuthentication(context, controller)) {
				this.applyRedirectForAuthentication(context);
				return;
			}
			context.persistRequesParamsInSession(this.getPersistableParamList());

			Response controllerResponse = null;
			if ( controller == null ) {
				controllerResponse = new ErrorResponse(this.getErrorView(), context.getResourceResolver(), "No controller found to service this request");
			}
			else {
				controllerResponse = controller.processRequest(context);
			}
			try {
				controllerResponse.respond(context);
			}
			catch (Exception e){
				e.printStackTrace();
				Response r = new ErrorResponse(this.getErrorView(), context.getResourceResolver(), "Servlet error", e);
				try {
					r.respond(context);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		finally {
			context.cleanup();
		}
	}


	public void doPost(HttpServlet dispatchServlet, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(dispatchServlet, request, response);
	}

}

