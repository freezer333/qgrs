package qgrs.compute;

import qgrs.data.GeneSequence;

/*
 * Implementations so far: CPortGquadruplexIdentifier.java
 */
public interface IQgrsIdentifier {

	void findGQuadruplexes(GeneSequence sequence) throws Exception;
}
