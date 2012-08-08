package qgrs.data.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

import framework.web.util.StringUtils;

public class GeneQuery extends WhereClause {

	private String speciesList;
	private String symbol;
	private String id;
	
	
	public GeneQuery(String id, String speciesList, String symbol) {
		super();
		this.speciesList = speciesList;
		this.id = id;
		this.symbol = symbol;
	}
	
	public boolean hasCriteria() {
		if ( StringUtils.isDefined(this.speciesList)){ return true; }
		if ( StringUtils.isDefined(id)) { return true; }
		if ( StringUtils.isDefined(this.symbol)) { return true;}
		return false;
	}
	public GeneQuery() {
		super();
		this.speciesList = "";
		this.id = "";
		this.symbol = "";
	}
	
	
	public String getSpeciesList() {
		return speciesList;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getId() {
		return id;
	}

	private String id() {
		if ( StringUtils.isDefined(id)) {
			return (" (accessionNumber = " + p(id) + " OR " + "giNumber = " + p(id) +") ");
		}
		else return "";
	}
	
	private String symbol() {
		if ( StringUtils.isDefined(this.symbol)) {
			return " geneSymbol LIKE '%" + this.symbol + "%' ";
		}
		return "";
	}
	
	private String species() {
		if ( StringUtils.isDefined(this.speciesList)){
			return " species " + in ( Arrays.asList(speciesList.split(";")));
		}
		else {
			return null;
		}
		
	}

	
	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((speciesList == null) ? 0 : speciesList.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneQuery other = (GeneQuery) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (speciesList == null) {
			if (other.speciesList != null)
				return false;
		} else if (!speciesList.equals(other.speciesList))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}


	@Override
	public String toSql() {
		LinkedList<String> criteria = new LinkedList<String>();
		criteria.add(this.id());
		criteria.add(this.species());
		criteria.add(this.symbol());
		return where(criteria);
	}

	@Override
	public void set(PreparedStatement ps) throws SQLException {
		// TODO Auto-generated method stub

	}

}
