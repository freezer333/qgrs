package qgrs.data;

import java.util.Collection;
import java.util.HashSet;

import org.jdom.Element;

import com.fasterxml.jackson.annotation.JsonProperty;

import qgrs.data.records.OntologyRecord;

public class OntologyData {
	@JsonProperty("functions")
	public Collection<String> functionList = new HashSet<String>();
	@JsonProperty("processes")
	public Collection<String> processList = new HashSet<String>();
	@JsonProperty("components")
	public Collection<String> componentList = new HashSet<String>();
	
	
	public Collection<String> getFunctions() {
		return functionList;
	}

	public Collection<String> getProcesses() {
		return processList;
	}

	public Collection<String> getComponents() {
		return componentList;
	}

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
	
	public boolean has(String term) {
		for ( String function : this.functionList) {
			if ( function.equalsIgnoreCase(term.trim())) return true;
		}
		for ( String function : this.processList) {
			if ( function.equalsIgnoreCase(term.trim())) return true;
		}
		for ( String function : this.componentList) {
			if ( function.equalsIgnoreCase(term.trim())) return true;
		}
		
		return false;
	}
	public boolean hasFuzzy(String term) {
		for ( String s : this.functionList) {
			if ( s.toUpperCase().contains(term.trim().toUpperCase())) return true;
		}
		for ( String s : this.processList) {
			if ( s.toUpperCase().contains(term.trim().toUpperCase())) return true;
		}
		for ( String s : this.componentList) {
			if ( s.toUpperCase().contains(term.trim().toUpperCase())) return true;
		}
		
		return false;
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

	public Collection<String> getFunctionList() {
		return functionList;
	}

	public Collection<String> getProcessList() {
		return processList;
	}

	public Collection<String> getComponentList() {
		return componentList;
	}

	public void setFunctionList(Collection<String> functionList) {
		this.functionList = functionList;
	}

	public void setProcessList(Collection<String> processList) {
		this.processList = processList;
	}

	public void setComponentList(Collection<String> componentList) {
		this.componentList = componentList;
	}
	
	
	
	
}
