package framework.web;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jdom.Document;

import framework.web.authentication.Role;
import framework.web.authentication.User;
import framework.web.authentication.UserDb;
import framework.web.util.StringUtils;


public abstract class AbstractWebContext {
	final protected HttpServletRequest request;
	final protected HttpServletResponse response;
	final protected HttpServlet servlet;
	final protected ResourceResolver resourceResolver;
	final protected AbstractDispatch dispatcher;

	public AbstractWebContext(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, AbstractDispatch dispatcher) {
		this.request = request;
		this.response = response;
		this.servlet = servlet;
		this.resourceResolver = new ResourceResolver(this.servlet.getServletContext());
		this.dispatcher = dispatcher;
	}
	
	

	
	public abstract String getContextName();
	public abstract void cleanup();
	public abstract Document buildPageXml() ;
	public abstract Document buildErrorPageXml(String errorMessage, Throwable e) ;
	
	
	public ResourceResolver getResourceResolver() {
		return resourceResolver;
	}

	public HttpSession getSession() {
		return this.getRequest().getSession();
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpServlet getServlet() {
		return servlet;
	}

	public String getFromRequest(String param) {
		if ( this.inRequest(param))  {
			return request.getParameter(param).trim();
		}
		return null;
	}
	public Object getFromSession(String param) {
		return  request.getSession().getAttribute(param);
	}
	public Object get(String param) {
		if ( inRequest(param)) return getFromRequest(param);
		return getFromSession(param);
	}
	public String getString(String param) {
		if ( inRequest(param)) return getFromRequest(param);
		return (String) getFromSession(param);
	}

	public boolean inRequest(String param) {
		return request.getParameter(param) != null;
	}
	public boolean inSession(String param) {
		return request.getSession().getAttribute(param) != null;
	}

	public boolean isSet(String param) {
		return inRequest(param) || inSession(param);
	}

	public void put(String param, Object value) {
		request.getSession().setAttribute(param, value);
	}
	
	@SuppressWarnings("unchecked")
	public void persistRequesParamsInSession(List<AbstractParam> params) {
		Set keys = request.getParameterMap().keySet();
		for ( Object o : keys ) {
			String key = (String) o;
			String value = request.getParameter(key);
			AbstractParam ap = findInParamList(params, key);
			if ( StringUtils.isDefined(value) && ap != null) {
				put(ap, value);
			}
		}
	}
	
	protected AbstractParam findInParamList(List<AbstractParam> params, String pName) {
		for ( AbstractParam ap : params ) {
			if (pName.equals(ap.getName()) ) return ap;
		}
		return null;
	}
	public String getFromRequest(AbstractParam param) {
		return request.getParameter(param.getName());
	}
	public Object getFromSession(AbstractParam param) {
		return  request.getSession().getAttribute(param.getName());
	}
	public Object get(AbstractParam param) {
		if ( inRequest(param)) return getFromRequest(param);
		return getFromSession(param);
	}
	public String getString(AbstractParam param) {
		if ( inRequest(param)) return getFromRequest(param);
		return (String) getFromSession(param);
	}
	
	public boolean inRequest(AbstractParam param) {
		return request.getParameter(param.getName()) != null;
	}
	public boolean inSession(AbstractParam param) {
		return request.getSession().getAttribute(param.getName()) != null;
	}
	
	public boolean isSet(AbstractParam param) {
		return inRequest(param) || inSession(param);
	}
	
	public void put(AbstractParam param, Object value) {
		request.getSession().setAttribute(param.getName(), value);
	}
	
	public String getApplicationPath() {
		ServletContext servletContext = servlet.getServletContext();
		String realPath = servletContext.getRealPath("").replace('\\', '/');
		if (!realPath.endsWith("/"))
			realPath = realPath + "/";
		return realPath;
	}
	

}
