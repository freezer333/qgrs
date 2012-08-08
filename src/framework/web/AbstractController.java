package framework.web;

import java.util.HashSet;
import java.util.Set;

import framework.web.response.Response;

public abstract class AbstractController {
	// Holds a list of the URLS supported by this controller
	protected Set<String> supportedUrls = new HashSet<String>();
	
	
	public AbstractController() {
		String fullName = this.getClass().getName();
		fullName = fullName.substring(fullName.lastIndexOf('.')+1);
		fullName = fullName.substring(0, 1).toLowerCase() + fullName.substring(1);
		this.supportedUrls.add("/"+fullName);
		this.supportedUrls.add("/"+fullName+"/");
	}
	
	public boolean servesPath(String path) {
		if ( path.contains("?") ) {
			path = path.split("?")[0];
		}
		if ( path.endsWith("/")) {
			path = path.substring(0, path.length()-1);
		}
		return this.supportedUrls.contains(path);
	}
	
	abstract public Response processRequest(AbstractWebContext context);
}
