package qgrs.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    
	private Dispatcher dispatch;

	
	@Override
	public void init() throws ServletException {
		super.init();
		dispatch = new Dispatcher(this);
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
