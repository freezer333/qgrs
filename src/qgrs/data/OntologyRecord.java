package qgrs.data;

import org.jdom.Element;


public class OntologyRecord {

	public final String term;
	public final GoType type;
	
	public OntologyRecord(String term, GoType type) {
		super();
		this.term = term;
		this.type = type;
	}
	public static OntologyRecord buildFromElement(Element element) {
		return new OntologyRecord(element.getChild("term").getText(), GoType.valueOf(element.getChild("type").getText())); 
	}
	
}
