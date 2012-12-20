package qgrs.db.query.genehomology;

import java.sql.ResultSet;
import java.text.DecimalFormat;

import org.jdom.Element;

import qgrs.db.query.SingleGeneResult;

public class GeneHomologyQueryResult {

	private SingleGeneResult prin = new SingleGeneResult("principle");
	private SingleGeneResult comp = new SingleGeneResult("comparison");
	private float alignmentScore ;
	private String id;
	
	public GeneHomologyQueryResult(ResultSet rs) {
		try {
			prin.geneId = rs.getString("PRINCIPLE");
			comp.geneId = rs.getString("COMPARISON");
			
			prin.geneSymbol = rs.getString("PSymbol");
			comp.geneSymbol = rs.getString("CSymbol");
			
			prin.geneSpecies = rs.getString("PSpecies");
			comp.geneSpecies = rs.getString("CSpecies");
			
			prin.qgrsCount = rs.getInt("P_QgrsCount");
			prin.qgrsHCount = rs.getInt("HCOUNT");
			
			alignmentScore = rs.getFloat("SIMILARITYPERCENTAGE");
			id = rs.getString("ID");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Element getXmlElement(){
		Element p =  new Element("pair");
		p.addContent(this.prin.getXmlElement());
		p.addContent(this.comp.getXmlElement());
		p.addContent(new Element("id").setText(id));
		p.addContent(new Element("alignmentScore").setText(new DecimalFormat("0.0%").format(this.alignmentScore)));
		return p;
	}
}
