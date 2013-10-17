package qgrs.data.mongo.primitives.jongo;

import java.util.Collection;
import java.util.LinkedList;

import org.jongo.marshall.jackson.oid.Id;

import qgrs.data.GeneSequence;
import qgrs.data.OntologyData;
import qgrs.data.Range;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MRNA{
	
	@Id
	String _id;
	String accessionNumber;
	String name;
	OntologyData ontologyData;
	String species;
	String giNumber;
	String symbol;
	int sequenceLength;
	@JsonProperty("5UTR")
	Range utr5;
	@JsonProperty("3UTR")
	Range utr3;
	Range cds;
	Collection<Range> polyASignals;
	Collection<Range> polyASites;
	Collection<G4> g4s;
	Collection<Homolog> homologs;
	
	public static MRNA buildFromGene(GeneSequence gene) {
		MRNA seq = new MRNA();
		seq.setAccessionNumber(gene.getAccessionNumber());;
		seq.setName(gene.getGeneName());
		seq.setOntologyData(gene.getOntologyData());
		seq.setSpecies(gene.getSpecies());
		seq.setSequenceLength(gene.getSequenceLength());
		seq.setGiNumber(gene.getGiNumber());
		seq.setSymbol(gene.getGeneSymbol());
		seq.setUtr5(gene.getUtr5());
		seq.setUtr3(gene.getUtr3());
		seq.setCds(gene.getCds());
		
		for ( qgrs.data.Range r : gene.getPolyASignals()) {
			seq.getPolyASignals().add(r);
		}
		for ( qgrs.data.Range r : gene.getPolyASites()) {
			seq.getPolyASites().add(r);
		}
		
		return seq;
	}
	
	
	

	public MRNA() {
		super();
		this.g4s = new LinkedList<G4>();
		this.polyASignals = new LinkedList<Range>();
		this.polyASites = new LinkedList<Range>();
		this.homologs =new LinkedList<Homolog>();
	}

	public boolean hasHomolog(String species) {
		for ( Homolog h : this.homologs ) {
			if ( h.getMrna().getSpecies().equalsIgnoreCase(species)) return true;
		}
		return false;
	}


	public int getSequenceLength() {
		return sequenceLength;
	}




	public void setSequenceLength(int sequenceLength) {
		this.sequenceLength = sequenceLength;
	}




	public String get_id() {
		return _id;
	}




	public void set_id(String _id) {
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




	public OntologyData getOntologyData() {
		return ontologyData;
	}




	public void setOntologyData(OntologyData ontologyData) {
		this.ontologyData = ontologyData;
	}




	public String getSpecies() {
		return species;
	}




	public void setSpecies(String species) {
		this.species = species;
	}




	public String getGiNumber() {
		return giNumber;
	}




	public void setGiNumber(String giNumber) {
		this.giNumber = giNumber;
	}




	public String getSymbol() {
		return symbol;
	}




	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}




	public Range getUtr5() {
		return utr5;
	}




	public void setUtr5(Range utr5) {
		this.utr5 = utr5;
	}




	public Range getUtr3() {
		return utr3;
	}




	public void setUtr3(Range utr3) {
		this.utr3 = utr3;
	}




	public Range getCds() {
		return cds;
	}




	public void setCds(Range cds) {
		this.cds = cds;
	}




	public Collection<Range> getPolyASignals() {
		return polyASignals;
	}




	public void setPolyASignals(Collection<Range> polyASignals) {
		this.polyASignals = polyASignals;
	}




	public Collection<Range> getPolyASites() {
		return polyASites;
	}




	public void setPolyASites(Collection<Range> polyASites) {
		this.polyASites = polyASites;
	}




	public Collection<G4> getG4s() {
		return g4s;
	}




	public void setG4s(Collection<G4> g4s) {
		this.g4s = g4s;
	}




	public Collection<Homolog> getHomologs() {
		return homologs;
	}




	public void setHomologs(Collection<Homolog> homologs) {
		this.homologs = homologs;
	}
	
	
		
	
}
