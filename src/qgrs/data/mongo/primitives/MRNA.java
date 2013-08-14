package qgrs.data.mongo.primitives;

import java.util.ArrayList;

import qgrs.data.OntologyData;

import com.mongodb.BasicDBObject;

public class MRNA extends BasicDBObject{

	
	
	
	
	

	public MRNA() {
		super();
		this.setG4s(new ArrayList<G4>());
		this.setPolyASignals(new ArrayList<Range>());
		this.setPolyASites(new ArrayList<Range>());
	}
	
	
	public MRNA asComparison() {
		this.remove("g4s");
		return this;
	}

	
	public Range getCds() {
		return (Range)this.get("cds");
	}

	public void setCds(Range cds) {
		this.put("cds", cds);
	}
	public Range get5UTR() {
		return (Range)this.get("5UTR");
	}

	public void set5UTR(Range utr5) {
		this.put("5UTR", utr5);
	}
	public Range get3UTR() {
		return (Range)this.get("3UTR");
	}

	public void set3UTR(Range utr3) {
		this.put("3UTR", utr3);
	}
	
	
	
	public ArrayList<Range> getPolyASignals() {
		return (ArrayList<Range>) get("polyASignals");
	}

	public void setPolyASignals(ArrayList<Range> signals) {
		this.put("polyASignals", signals);
	}
	
	public ArrayList<Range> getPolyASites() {
		return (ArrayList<Range>) get("polyASites");
	}

	public void setPolyASites(ArrayList<Range> sites) {
		this.put("polyASites", sites);
	}
	
	
	
	
	public String getAccessionNumber() {
		return this.getString("accessionNumber");
	}

	public void setAccessionNumber(String accessionNumber) {
		this.put("accessionNumber", accessionNumber);
	}
	
	
	public int getSequenceLength() {
		return this.getInt("sequenceLength");
	}

	public void setSequenceLength(int sequenceLength) {
		this.put("sequenceLength", sequenceLength);
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
	
	public String getGiNumber() {
		return this.getString("giNumber");
	}

	public void setGiNumber(String giNumber) {
		this.put("giNumber", giNumber);
	}
	
	public String getSymbol() {
		return this.getString("symbol");
	}

	public void setSymbol(String symbol) {
		this.put("symbol", symbol);
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
