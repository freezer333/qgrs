package qgrs.compute;

import qgrs.data.GeneSequence;

/*
 * Implementations so far: CPortGquadruplexIdentifier.java
 */
public interface QgrsIdentifier {

	void findGQuadruplexes(GeneSequence sequence) throws Exception;
}
