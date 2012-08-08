package qgrs.data.query;

import qgrs.data.GeneSequence;

public class AlignmentGeneInfo {
	public String geneSymbol;
	public String speciesName;
	public String geneId;
	public AlignmentGeneInfo(GeneSequence g) {
		geneSymbol = g.getGeneSymbol();
		speciesName = g.getSpecies();
		geneId = g.getAccessionNumber();
	}
}