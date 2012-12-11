package qgrs.data.query.genehomology;

import java.sql.ResultSet;
import java.text.DecimalFormat;

import org.jdom.Element;

import qgrs.data.query.SingleGeneResult;

public class GeneHomologyQueryResult {

	private SingleGeneResult prin = new SingleGeneResult("principle");
	private SingleGeneResult comp = new SingleGeneResult("comparison");
	private float alignmentScore ;
	private String id;
	
	public GeneHomologyQueryResult(ResultSet rs) {
		try {
			prin.geneId = rs.getString("P_AccessionNumber");
			comp.geneId = rs.getString("C_AccessionNumber");
			
			prin.geneSymbol = rs.getString("P_GeneSymbol");
			comp.geneSymbol = rs.getString("C_GeneSymbol");
			
			prin.geneSpecies = rs.getString("P_Species");
			comp.geneSpecies = rs.getString("C_Species");
			
			prin.qgrsCount = rs.getInt("P_QgrsCount");
			prin.qgrsHCount = rs.getInt("HCOUNT");
			
			alignmentScore = rs.getFloat("ALIGNMENTSCORE");
			id = rs.getString("alignmentId");
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
		p.addContent(new Element("alignmentScore").setText(new DecimalFormat("0.%").format(this.alignmentScore)));
		return p;
	}
}
