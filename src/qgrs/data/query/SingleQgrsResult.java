package qgrs.data.query;

import org.jdom.Element;

public class SingleQgrsResult {
	public String geneSymbol;
	public String geneSpecies;
	public String qgrsId;
	public String qgrsSequence;
	public String qgrsRegion;
	public String qgrsPosition;
	public String qgrsTetrads;
	public String qgrsGScore;
	
	public int tetrad1;
	public int tetrad2;
	public int tetrad3;
	public int tetrad4;
	
	public int hCount = 0;
	
	public Element getXmlElement() {
		Element root = new Element("qgrs");
		root.addContent(new Element("geneSymbol").setText(geneSymbol));
		root.addContent(new Element("geneSpecies").setText(geneSpecies));
		root.addContent(new Element("qgrsId").setText(qgrsId));
		root.addContent(new Element("qgrsSequence").setText(qgrsSequence));
		root.addContent(new Element("qgrsRegion").setText(qgrsRegion));
		root.addContent(new Element("qgrsPosition").setText(qgrsPosition));
		root.addContent(new Element("qgrsTetrads").setText(qgrsTetrads));
		root.addContent(new Element("qgrsGScore").setText(qgrsGScore));
		
		root.addContent(new Element("tetrad1_normalized").setText(String.valueOf(tetrad1-tetrad1+1)));
		root.addContent(new Element("tetrad2_normalized").setText(String.valueOf(this.tetrad2-tetrad1+1)));
		root.addContent(new Element("tetrad3_normalized").setText(String.valueOf(this.tetrad3-tetrad1+1)));
		root.addContent(new Element("tetrad4_normalized").setText(String.valueOf(this.tetrad4-tetrad1+1)));

		root.addContent(new Element("hCount").setText(String.valueOf(this.hCount)));
		
		return root;
	}
}