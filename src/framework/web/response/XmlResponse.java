
package framework.web.response;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import framework.web.AbstractWebContext;


public class XmlResponse implements Response {

	Document xmlDoc;
	
	public XmlResponse(Document xmlDoc) {
		this.xmlDoc = xmlDoc;
	}
	
	
	
	@Override
	public void respond(AbstractWebContext context) throws Exception {
		String fileType = "attachment;filename=" + "datadictionary" + ".xml";
		context.getResponse().setHeader("Content-Disposition", fileType);
		context.getResponse().setContentType("text/xml");
		new XMLOutputter(Format.getPrettyFormat()).output(xmlDoc, context.getResponse().getWriter());
		context.getResponse().flushBuffer();
	}

}
