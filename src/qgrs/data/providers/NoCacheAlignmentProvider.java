package qgrs.data.providers;

import qgrs.compute.GeneSequencePair;

public class NoCacheAlignmentProvider extends AlignmentProvider {

	@Override
	protected AlignmentProviderResult getAlignmentFromCache(
			GeneSequencePair pair) {
		return null;
	}

}
