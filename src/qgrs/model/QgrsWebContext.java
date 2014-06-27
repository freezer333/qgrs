package qgrs.model;

import java.sql.Connection;
import java.util.Collection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.db.DatabaseConnection;
import qgrs.db.HomologyRecordDb;
import framework.web.AbstractDispatch;
import framework.web.AbstractWebContext;

public class QgrsWebContext extends AbstractWebContext {
	
	public QgrsWebContext(HttpServlet dispatchServlet, HttpServletRequest request, HttpServletResponse response, AbstractDispatch dispatch) {
		super(dispatchServlet, request, response, dispatch);
	}
	@Override
	public String getContextName() {
		return "qgrs";
	}
	
	
	
	
	@Override
	public void cleanup() {
		
		
	}
	
	
	
	
	
	
	private Document buildNullPage() {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    pageXml.addContent(root);
	    return pageXml;
	}

	@Override
	public Document buildPageXml() {
		return buildNullPage();
	}

	@Override
	public Document buildErrorPageXml(String errorMessage, Throwable e) {
		return buildNullPage();
	}

}
