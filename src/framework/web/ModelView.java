package framework.web;

import org.jdom.Document;




public class ModelView {
	Document pageXml;
	String xslFilename;
	
	public ModelView (String xslFilename, Document pageXml) {
		this.xslFilename = xslFilename;
		this.pageXml = pageXml;
	}

	public Document getPageXml() {
		return pageXml;
	}

	public void setPageXml(Document pageXml) {
		this.pageXml = pageXml;
	}

	public String getXslFilename() {
		return xslFilename;
	}

	public void setXslFilename(String xslFilename) {
		this.xslFilename = xslFilename;
	}

	
}

