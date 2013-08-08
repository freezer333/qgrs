package qgrs.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.jdom.Element;

import qgrs.data.records.OntologyRecord;

import com.google.code.morphia.annotations.Entity;
import com.mongodb.BasicDBObject;

@Entity
public class OntologyData extends BasicDBObject{
	

	
	
	public OntologyData() {
		this.setFunctions(new ArrayList<String>());
		this.setProcesses(new ArrayList<String>());
		this.setComponents(new ArrayList<String>());
	}
	
	
	
	


	public ArrayList<String> getFunctions() {
		return (ArrayList<String>)this.get("functions");
	}



	public void setFunctions(ArrayList<String> functions) {
		this.put("functions", functions);
	}
	
	public ArrayList<String> getProcesses() {
		return (ArrayList<String>)this.get("processes");
	}



	public void setProcesses(ArrayList<String> processes) {
		this.put("processes", processes);
	}
	
	public ArrayList<String> getComponents() {
		return (ArrayList<String>)this.get("components");
	}



	public void setComponents(Collection<String> components) {
		this.put("components", components);
	}



	


	public void put(OntologyRecord or ){
		if ( or.type == GoType.Function ) {
			this.getFunctions().add(or.term);
		}
		else if ( or.type == GoType.Component ) {
			this.getComponents().add(or.term);
		}
		else if ( or.type == GoType.Process ) {
			this.getProcesses().add(or.term);
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
		addList(root, this.getFunctions(), GoType.Function);
		addList(root, this.getComponents(), GoType.Component);
		addList(root, this.getProcesses(), GoType.Process);
		return root;
	}
	
}
