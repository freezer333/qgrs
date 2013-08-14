package qgrs.compute.gscore;

import java.util.LinkedList;

import qgrs.data.GQuadruplex;
import qgrs.data.GeneSequence;


public class QgrsFinder {

	public void populateQgrs(GeneSequence sequence) throws Exception {
		LinkedList<GQuadruplex> qgrs = new LinkedList<GQuadruplex>();
		QgrsBuilder b = new QgrsBuilder(sequence.getPureSequence());
		for ( QgrsCandidate c : b.findQgrs() ) {
			qgrs.add(new GQuadruplex(sequence, qgrs.size(), c));
		}
		sequence.setgQuads(qgrs);
	}
}
