package qgrs.compute.alignment;

import qgrs.data.BaseSymbol;

public class StandardAlignmentProperties implements AlignmentProperties {

	@Override
	public int getGapExtensionPenalty() {
		return -4;
	}

	@Override
	public int getGapOpenningPenalty() {
		return -50;
	}

	@Override
	public int getSimilarityScore(BaseSymbol s1, BaseSymbol s2) {
		if ( s1 == BaseSymbol.N || s2 == BaseSymbol.N ) {
			return 0;
		}
		if ( s1 == s2 ) {
			return 5;
		}
		else {
			return -4;
		}
	}

}
