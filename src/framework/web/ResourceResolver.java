package framework.web;

import java.io.File;

import javax.servlet.ServletContext;


public class ResourceResolver {

	private final String webroot;
	
	public ResourceResolver (ServletContext sc) {
		webroot = sc.getRealPath("/");
	}
	
	public File getResourceFile(ResourceType type, String filename) {
		String fullPath = this.webroot + "/" + type.toString() + "/" + filename;
		return new File (fullPath);
	}
	
}

