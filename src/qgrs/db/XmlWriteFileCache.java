package qgrs.db;

import java.io.File;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XmlWriteFileCache extends XmlWriteCache {

	private final File dir;
	private final File destination;
	
	public XmlWriteFileCache(File rootDir) {
		super();
		this.dir = rootDir;
		this.destination = null;
	}
	
	public XmlWriteFileCache(File rootDir, File dest) {
		super();
		this.dir = rootDir;
		this.destination = dest;
	}

	@Override
	void handleXmlResult(Document xmlDoc) {
		File f = destination == null ? 
				new File ( this.dir + "\\" + alignmentRecord.getPrinciple() + "_x_" + alignmentRecord.getComparison() + ".xml") :
					destination;
				
		this.writeXml(xmlDoc, f);
	}

	private void writeXml (Document xml, File file) {
		try {
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
	        java.io.FileWriter writer = new java.io.FileWriter(file);
	        out.output(xml, writer);
	        writer.flush();
	        writer.close();
	        System.out.println("wrote " + file.getAbsolutePath());
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}

}
