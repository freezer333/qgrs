package qgrs.data;

import java.util.Collection;
import java.util.HashSet;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.utils.XmlPrinter;

public class OntologyData {
	public final Collection<String> functionList = new HashSet<String>();
	public final Collection<String> processList = new HashSet<String>();
	public final Collection<String> componentList = new HashSet<String>();
	
	
	public void put(OntologyRecord or ){
		if ( or.type == GoType.Function ) {
			functionList.add(or.term);
		}
		else if ( or.type == GoType.Component ) {
			componentList.add(or.term);
		}
		else if ( or.type == GoType.Process ) {
			processList.add(or.term);
		}
	}
	
	private void addList(Element root, Collection<String> terms, GoType listType) {
		for ( String t : terms) {
			Element go = new Element("go");
			Element term = new Element("term");
			Element type = new Element("type");
			term.setText(t);
			type.setText(listType.toString());
			go.addContent(term);
			go.addContent(type);
			root.addContent(go);
		}
	}
	public Element getXmlElement() {
		Element root = new Element("ontologyData");
		addList(root, functionList, GoType.Function);
		addList(root, componentList, GoType.Component);
		addList(root, processList, GoType.Process);
		return root;
	}
	
}
