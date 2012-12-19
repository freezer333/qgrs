package qgrs.db.query.gene;

import java.sql.ResultSet;

import org.jdom.Element;

import qgrs.db.query.SingleGeneResult;

public class GeneQueryResult {
	private SingleGeneResult result = new SingleGeneResult();
	
	public GeneQueryResult(ResultSet rs) {
		try {
			result.geneId = rs.getString("accessionnumber");
			
			result.geneSymbol = rs.getString("genesymbol");
			
			result.geneSpecies = rs.getString("species");
			
			result.qgrsCount = rs.getInt("qgrsCount");
			
			result.qgrsHCount = rs.getInt("qgrsHcount");
			
			result.geneHcount = rs.getInt("geneHCount");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Element getXmlElement(){
		return result.getXmlElement();
	}
}
