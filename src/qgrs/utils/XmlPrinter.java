package qgrs.utils;

import java.io.IOException;
import java.io.PrintStream;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XmlPrinter {
	final Document doc;
	final PrintStream out;
	
	public XmlPrinter(Document doc) {
		this.doc = doc;
		out = System.out;
	}
	
	
	
	public XmlPrinter(Document doc, PrintStream out) {
		super();
		this.doc = doc;
		this.out = out;
	}



	public void print() {
		XMLOutputter printer = new XMLOutputter(Format.getPrettyFormat());
		try {
			printer.output(doc, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
