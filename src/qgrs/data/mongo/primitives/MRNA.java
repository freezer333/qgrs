package qgrs.data.mongo.primitives;

import java.util.ArrayList;

import qgrs.data.OntologyData;

import com.google.code.morphia.annotations.Embedded;

public class MRNA {

	private long _id;
	private String accessionNumber;
	private String name;
	private String species;
	
	private ArrayList<G4> g4s;
	
	private OntologyData ontology;

	
	public MRNA() {
		super();
		g4s = new ArrayList<G4>();
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getAccessionNumber() {
		return accessionNumber;
	}

	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public ArrayList<G4> getG4s() {
		return g4s;
	}

	public void setG4s(ArrayList<G4> g4s) {
		this.g4s = g4s;
	}

	public OntologyData getOntology() {
		return ontology;
	}

	public void setOntology(OntologyData ontology) {
		this.ontology = ontology;
	}
	
	
	
}
