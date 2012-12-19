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
	final DatabaseConnection dbConnection;
	
	public QgrsWebContext(HttpServlet dispatchServlet, HttpServletRequest request, HttpServletResponse response, Connection dbConnection, AbstractDispatch dispatch) {
		super(dispatchServlet, request, response, dispatch);
		this.dbConnection = new DatabaseConnection(dbConnection);
	}
	@Override
	public String getContextName() {
		return "qgrs";
	}
	
	
	public Element getSpeciesDropdownElement() {
		Element speciesList = new Element("speciesList");
		speciesList.addContent(new Element("species").setAttribute("value", "").setAttribute("display", "Any"));//empty element for the blank entry
		HomologyRecordDb db = new HomologyRecordDb (this.getDbConnection());
		try {
			Collection <String> allSpecies = db.getComparisonSpecies();
			for ( String s : allSpecies) {
				speciesList.addContent(new Element("species").setAttribute("value", s).setAttribute("display", s));
			}
			return speciesList;
		}
		finally {
			db.close();
		}
		
	}
	
	@Override
	public void cleanup() {
		try {
			if ( this.dbConnection != null) {
				this.dbConnection.close();
			}
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
		
	}
	
	
	
	public DatabaseConnection getDbConnection() {
		return dbConnection;
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
