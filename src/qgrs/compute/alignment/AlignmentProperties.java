package qgrs.compute.alignment;

import qgrs.data.BaseSymbol;

public interface AlignmentProperties {
	int getGapExtensionPenalty();
	int getGapOpenningPenalty();
	int getSimilarityScore(BaseSymbol s1, BaseSymbol s2) ;
}
