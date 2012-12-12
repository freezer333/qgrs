package qgrs.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.web.ResourceResolver;

import qgrs.utils.db.DatabaseUpdate;

public class DispatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    
	private Dispatcher dispatch;

	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		dispatch = new Dispatcher(this);
		DatabaseUpdate du = new DatabaseUpdate(new ResourceResolver(this.getServletContext()));
		du.update();
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dispatch.doGet(this, req, resp);
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dispatch.doGet(this, req, resp);
	}
	
	
}
