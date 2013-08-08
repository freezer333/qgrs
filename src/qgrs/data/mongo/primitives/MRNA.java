package qgrs.data.mongo.primitives;

import java.util.ArrayList;

import qgrs.data.OntologyData;

import com.mongodb.BasicDBObject;

public class MRNA extends BasicDBObject{


	public MRNA() {
		super();
		this.setG4s(new ArrayList<G4>());
	}

	public String getAccessionNumber() {
		return this.getString("accessionNumber");
	}

	public void setAccessionNumber(String accessionNumber) {
		this.put("accessionNumber", accessionNumber);
	}

	public String getName() {
		return this.getString("name");
	}

	public void setName(String name) {
		this.put("name", name);
	}

	public String getSpecies() {
		return this.getString("species");
	}

	public void setSpecies(String species) {
		this.put("species", species);
	}

	public ArrayList<G4> getG4s() {
		return (ArrayList<G4>) get("g4s");
	}

	public void setG4s(ArrayList<G4> g4s) {
		this.put("g4s", g4s);
	}

	
	public OntologyData getOntology() {
		return (OntologyData) get("ontologyData");
	}

	public void setOntology(OntologyData ontology) {
		this.put("ontologyData", ontology);
	}
	
	
	
}
