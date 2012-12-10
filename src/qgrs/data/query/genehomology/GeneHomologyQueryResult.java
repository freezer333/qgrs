package qgrs.data.query.genehomology;

import java.sql.ResultSet;

import org.jdom.Element;

public class GeneHomologyQueryResult {

	
	public GeneHomologyQueryResult(ResultSet rs) {
		
	}
	
	public Element getXmlElement(){
		return new Element("pair");
	}
}
