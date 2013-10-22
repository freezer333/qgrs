package qgrs.data.analysis;

import static qgrs.data.analysis.FilterLogicOperator.Or;

import java.util.LinkedList;

import org.h2.util.StringUtils;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.MRNA;

public class MrnaFilter {

	private LinkedList<String> ontologyTerms = new LinkedList<String>();
	private LinkedList<String> fuzzyOntologyTerms = new LinkedList<String>();
	private LinkedList<String> geneNameSearchTerms = new LinkedList<String>();
	private LinkedList<String> homologList = new LinkedList<String>();
	private FilterLogicOperator ontologyOperator = Or;
	private FilterLogicOperator homologOperator = Or;
	private boolean regionRequried = true;
	private String name = null;
	
	private Range numPolyA = new Range(0, Integer.MAX_VALUE);
	
	
	public LinkedList<String> getHomologList() {
		return homologList;
	}

	public String getHomologString() {
		StringBuffer sb = new StringBuffer();
		for ( String s : this.homologList ) {
			sb.append(s);
			sb.append(" | ");
		}
		String retval = sb.toString();
		if ( retval.length() > 1) {
			return retval.substring(0, retval.lastIndexOf('|')-1);
		}
		return retval;
	}
	
	public MrnaFilter() {
		
	}
	public MrnaFilter(String name) {
		this.name = name;
	}
	
	public int getMinNumberOfPolyA() {
		return this.numPolyA.getStart();
	}
	public MrnaFilter setMinNumberOfPolyA(int minNumberOfPolyA) {
		this.numPolyA = new Range( minNumberOfPolyA, this.numPolyA.getEnd());
		return this;
	}
	public int getMaxNumberOfPolyA() {
		return this.numPolyA.getEnd();
	}
	public MrnaFilter setMaxNumberOfPolyA(int maxNumberOfPolyA) {
		this.numPolyA = new Range( this.numPolyA.getStart(), maxNumberOfPolyA);
		return this;
	}
	
	public MrnaFilter addOntologyTerm(String term) {
		this.ontologyTerms.add(term);
		return this;
	}
	public MrnaFilter addFuzzyOntologyTerm(String term) {
		this.fuzzyOntologyTerms.add(term);
		return this;
	}
	public MrnaFilter addGeneNameSearchTerm(String term) {
		this.geneNameSearchTerms.add(term);
		return this;
	}
	public MrnaFilter addHomolog(String homolog) {
		this.homologList.add(homolog);
		return this;
	}
	
	public MrnaFilter addOntologyTerms(String [] terms) {
		for ( String term : terms) 
			this.ontologyTerms.add(term);
		return this;
	}
	public MrnaFilter addFuzzyOntologyTerms(String [] terms) {
		for ( String term : terms) 
			this.fuzzyOntologyTerms.add(term);
		return this;
	}
	
	public MrnaFilter addGeneNameSearchTerms(String [] terms) {
		for ( String term : terms) 
			this.geneNameSearchTerms.add(term);
		return this;
	}
	
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		if (!StringUtils.isNullOrEmpty(name)) return name;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("#PolyA = " + this.numPolyA.toString());
		if ( this.homologList.size() > 0 ) {
			sb.append("H=");
			for ( String t : this.homologList ) {
				sb.append(t + "\t");
			}
		}
		if ( this.ontologyTerms.size() > 1 ) {
			sb.append("O=");
			for ( String t : ontologyTerms) {
				sb.append(t + "\t");
			}
		}
		if ( this.fuzzyOntologyTerms.size() > 1 ) {
			sb.append("O~=");
			for ( String t : fuzzyOntologyTerms) {
				sb.append(t + "\t");
			}
		}
		
		return sb.toString();
	}
	
	public boolean isRegionRequried() {
		return regionRequried;
	}

	public MrnaFilter setRegionRequried(boolean regionRequried) {
		this.regionRequried = regionRequried;
		return this;
	}

	private boolean filterByOntology(MRNA mrna) {
		if ( this.ontologyTerms.size() < 1 ) return true;
		if ( this.regionRequried && mrna.getCds() == null ) return false;
		
		if ( mrna.getOntologyData() == null ) return false;
		
		if ( this.ontologyOperator == Or) {
			for ( String term : this.ontologyTerms ) {
				if ( mrna.getOntologyData().has(term)) return true;
			}
			return false;
		}
		else {
			for ( String term : this.ontologyTerms ) {
				if ( !mrna.getOntologyData().has(term)) return false;
			}
			return true;
		}
	}
	private boolean filterByFuzzyOntology(MRNA mrna) {
		if ( this.fuzzyOntologyTerms.size() < 1 ) return true;
		if ( this.regionRequried && mrna.getCds() == null ) return false;
		
		if ( mrna.getOntologyData() == null ) return false;
		
		if ( this.ontologyOperator == Or) {
			for ( String term : this.fuzzyOntologyTerms ) {
				if ( mrna.getOntologyData().hasFuzzy(term)) return true;
			}
			return false;
		}
		else {
			for ( String term : this.fuzzyOntologyTerms ) {
				if ( !mrna.getOntologyData().hasFuzzy(term)) return false;
			}
			return true;
		}
	}
	
	private boolean filterByGeneName(MRNA mrna) {
		if ( this.geneNameSearchTerms.size() < 1 ) return true;
		if ( this.regionRequried && mrna.getCds() == null ) return false;
		
		
		if ( this.ontologyOperator == Or) {
			for ( String term : this.geneNameSearchTerms ) {
				if ( mrna.getName().toUpperCase().contains(term.toUpperCase())) return true;
			}
			return false;
		}
		else {
			for ( String term : this.geneNameSearchTerms ) {
				if ( !mrna.getName().contains(term)) return false;
			}
			return true;
		}
	}
	
	private boolean filterByHomolog(MRNA mrna) {
		if ( this.homologList.size() < 1 ) return true;
		if ( this.homologOperator == Or) {
			for ( String term : this.homologList ) {
				if ( mrna.hasHomolog(term) ) return true;
			}
			return false;
		}
		else {
			for ( String term : this.geneNameSearchTerms ) {
				if ( !mrna.hasHomolog(term)) return false;
			}
			return true;
		}
	}
	
	public boolean acceptable(MRNA mrna) {
		if ( this.getMinNumberOfPolyA() > 0 ) {
			if ( mrna.getPolyASignals() == null ) return false;
			if ( mrna.getPolyASignals().size() < this.getMinNumberOfPolyA() ) return false;
		}
		if ( this.getMaxNumberOfPolyA() < Integer.MAX_VALUE ) {
			if ( mrna.getPolyASignals() == null ) return false;
			if ( mrna.getPolyASignals().size() > this.getMaxNumberOfPolyA() ) return false;
		}
		if ( !this.filterByHomolog(mrna)) return false;
		if ( !this.filterByOntology(mrna))  return false;
		if ( !this.filterByFuzzyOntology(mrna))  return false;
		if ( !this.filterByGeneName(mrna))  return false;
		return true;
	}
	
	
	public FilterLogicOperator getHomologOperator() {
		return homologOperator;
	}
	public void setHomologOperator(FilterLogicOperator homologOperator) {
		this.homologOperator = homologOperator;
	}
	
	
}
