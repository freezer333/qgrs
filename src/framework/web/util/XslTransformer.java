
package framework.web.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Document;

import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.ResourceType;

public class XslTransformer {
	
	public static void transform(AbstractWebContext context, ModelView modelView) throws IOException, Exception {
		transform(context.getResponse().getOutputStream(), modelView.getPageXml(), context.getResourceResolver().getResourceFile(ResourceType.xsl, modelView.getXslFilename()));
	}
	private static void transform(OutputStream outStream, Document sourceDoc, File stylesheetFile) throws Exception{
        TransformerFactory tFactory = TransformerFactory.newInstance();
        org.jdom.output.DOMOutputter outputter = new org.jdom.output.DOMOutputter();
        org.w3c.dom.Document domDocument = outputter.output(sourceDoc);
        javax.xml.transform.Source xmlSource = new javax.xml.transform.dom.DOMSource(domDocument);
        StreamSource xsltSource =  new StreamSource(stylesheetFile);
        StreamResult xmlResult = new StreamResult(outStream);
        Transformer transformer = tFactory.newTransformer(xsltSource);
        transformer.transform(xmlSource, xmlResult);
	}
}