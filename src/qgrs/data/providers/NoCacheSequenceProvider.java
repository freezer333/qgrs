package qgrs.data.providers;

import java.util.HashMap;

public class NoCacheSequenceProvider extends SequenceProvider {

	@Override
	protected HashMap<Key, Object> getCachedSequence(String accessionOrGi) {
		return null;
	}

}
