package qgrs.test;

import static org.junit.Assert.*;

import org.junit.Test;

import qgrs.compute.FamilyHomologyScorer;
import qgrs.compute.HomologyScoreResult;
import qgrs.data.Base;
import qgrs.data.GQuadruplex;
import qgrs.data.GeneSequence;

public class HomologyScoringTests {

	
	private final String p = "GGGTGGAGGGCAGTGGGAGCGGG";
	private final String c = "GGAAGGGCTGGGCGGATGG";
	
	GQuadruplex build(GeneSequence seq, int t1, int t2, int t3, int t4, int tetrads, int score){
		for ( Base b : seq.getBases()) {
			b.setIndexWithGaps(b.getIndexWithoutGaps());
		}
		GQuadruplex q = new GQuadruplex(seq, 0);
		q.setStart(seq.getBases().get(t1));
		q.setTetrad2Start(seq.getBases().get(t2));
		q.setTetrad3Start(seq.getBases().get(t3));
		q.setTetrad4Start(seq.getBases().get(t4));
		q.setLength(t4-t1+1);
		q.setScore(score);
		q.setNumTetrads(tetrads);
		return q;
	}
	
	
	@Test
	public void testScore() {
		FamilyHomologyScorer scorer = new FamilyHomologyScorer(null);
		
		GeneSequence principal = GeneSequence.buildFromDirectInput(p);
		GeneSequence comparison = GeneSequence.buildFromDirectInput(c);
		
		GQuadruplex pQ = build(principal, 0, 7, 14, 20, 3, 71);
		GQuadruplex cQ = build(comparison, 0, 4, 13, 17, 2, 31);
		
		
		HomologyScoreResult result = scorer.compareQuadruplexes(pQ, cQ, principal, comparison);
		// we're hardcoding this to have an overlap score of 1.0, since we don't want to test alignment right now....
		result.setOverlapScore(1);
		
		/*
		Homology Score: 0.8014
		Tetrad: 0.5	Loop: 0.159	Length: 0.710
		*/
		assertEquals("Tetrad Score", result.getTetradScore(), 0.5, 0.001);
		assertEquals("Loop Score", result.getAvgLoopScore(), 0.159, 0.001);
		assertEquals("Length Score", result.getTotalLengthScore(), 0.710, 0.001);
		assertEquals("Overall Score", result.getOverallScore(), 0.8014, 0.001);
	}
}
