package qgrs.data;

import java.util.Collection;
import java.util.HashSet;

import org.jdom.Element;

import qgrs.data.records.OntologyRecord;

import com.google.code.morphia.annotations.Entity;

@Entity
public class OntologyData {
	public Collection<String> functions = new HashSet<String>();
	public Collection<String> processes = new HashSet<String>();
	public Collection<String> components = new HashSet<String>();
	
	
	private long _id;
	
	
	public OntologyData() {
		
	}
	
	
	
	public long get_id() {
		return _id;
	}



	public void set_id(long _id) {
		this._id = _id;
	}



	public Collection<String> getFunctions() {
		return functions;
	}



	public void setFunctions(Collection<String> functions) {
		this.functions = functions;
	}



	public Collection<String> getProcesses() {
		return processes;
	}



	public void setProcesses(Collection<String> processes) {
		this.processes = processes;
	}



	public Collection<String> getComponents() {
		return components;
	}



	public void setComponents(Collection<String> components) {
		this.components = components;
	}



	public void put(OntologyRecord or ){
		if ( or.type == GoType.Function ) {
			functions.add(or.term);
		}
		else if ( or.type == GoType.Component ) {
			components.add(or.term);
		}
		else if ( or.type == GoType.Process ) {
			processes.add(or.term);
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
		addList(root, functions, GoType.Function);
		addList(root, components, GoType.Component);
		addList(root, processes, GoType.Process);
		return root;
	}
	
}
