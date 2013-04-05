package qgrs.controllers;

import java.util.Arrays;
import java.util.Collection;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.authentication.Role;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class DbHome  extends AbstractController {
	
	@Override
	public boolean requiresAuthentication() {
		return true;
	}
	@Override
	public Collection<Role> getAuthorizedRoles() {
		return Arrays.asList(new Role[] { new Role("user")});
	}
	public DbHome() {
		super();
		this.supportedUrls.add("/app/dbhome");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.DbHome, pageXml));	
	}
}
