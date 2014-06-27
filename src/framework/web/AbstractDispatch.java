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

import framework.db.DatabaseConnectionParameters;
import framework.web.response.ErrorResponse;
import framework.web.response.RedirectResponse;
import framework.web.response.Response;
import framework.web.util.ClassFinder;


public abstract class AbstractDispatch {
	protected List<AbstractController> controllers;

	protected HttpServlet servlet;
	
	protected AbstractDispatch(HttpServlet servlet) {
		this.servlet = servlet;
		this.loadControllersFromPackage(this.getControllerPackage());
	}

	protected abstract DatabaseConnectionParameters getDbParams();
	protected abstract String getControllerPackage();
	protected abstract String getErrorView() ;
	protected abstract AbstractWebContext buildContext(HttpServlet dispatchServlet, HttpServletRequest request, HttpServletResponse response);

	
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

	

	protected List<AbstractParam> getPersistableParamList() {
		return new LinkedList<AbstractParam>();
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


	public abstract RedirectResponse getAuthRedirect();
	
	
	public void doGet(HttpServlet dispatchServlet, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AbstractWebContext context = this.buildContext(dispatchServlet, request, response);

		try {
			AbstractController controller = this.mapUrl(request);
			
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
	public static String getFullUrl(HttpServletRequest request) {
	    StringBuffer requestURL = request.getRequestURL();
	    String queryString = request.getQueryString();

	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	        return requestURL.append('?').append(queryString).toString();
	    }
	}
	

	public void doPost(HttpServlet dispatchServlet, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(dispatchServlet, request, response);
	}

}

