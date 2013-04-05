package qgrs.controllers;

import java.sql.Connection;
import java.sql.DriverManager;

import qgrs.db.AppProperties;
import framework.db.DatabaseConnectionParameters;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.response.RedirectResponse;
import framework.web.response.Response;

public class Authenticate extends AbstractController {

	public Authenticate() {
		super();
		this.supportedUrls.add("/app/authenticate");
	}
	
	
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		DatabaseConnectionParameters params = new DatabaseConnectionParameters(AppProperties.getUserDbConnectionString(context.getResourceResolver()), "sa", "sa");
		Connection conn = null;
		try {
			 	Class.forName("org.h2.Driver");
		     	conn = DriverManager.getConnection(params.getConnectionString(), params.getUsername(), params.getPassword());
		     	context.loginUserFromRequestParams(conn);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		finally {
			if ( conn != null ) {
				try { conn.close(); }
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	    String redirect = context.inSession("authredirect") ? (String)context.getFromSession("authredirect") : "start";
	    return new RedirectResponse(redirect);
	}
}
