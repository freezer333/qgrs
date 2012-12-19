package qgrs.db.query;

import org.jdom.Element;

import framework.web.util.StringUtils;

public class SingleGeneResult {

	public String geneId;
	public String geneSymbol;
	public String geneSpecies;
	public int qgrsCount;
	public int qgrsHCount;
	public int geneHcount;
	public String side;
	
	public SingleGeneResult() {
		this.side = "";
	}
	public SingleGeneResult(String side) {
		this.side = side;
	}
	public Element getXmlElement() {
		Element root = new Element("gene");
		root.addContent(new Element("geneId").setText(this.geneId));
		root.addContent(new Element("geneSymbol").setText(this.geneSymbol));
		root.addContent(new Element("geneSpecies").setText(this.geneSpecies));
		root.addContent(new Element("qgrsCount").setText(String.valueOf(qgrsCount)));
		root.addContent(new Element("qgrsHCount").setText(String.valueOf(qgrsHCount)));
		root.addContent(new Element("geneHCount").setText(String.valueOf(geneHcount)));
		if ( StringUtils.isDefined(this.side)) {
			root.setAttribute("side", this.side);
		}
		return root;
	}
	
}
