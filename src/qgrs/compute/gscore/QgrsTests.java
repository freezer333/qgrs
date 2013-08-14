package qgrs.compute.gscore;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class QgrsTests{
	
	
	
	@Test
	public void testScoring1() {
		QgrsCandidate c = new QgrsCandidate(3, 1, 1, 1);
		assertEquals(64, c.getScore());
		
	}	
	@Test
	public void testScoring2() {
		QgrsCandidate c = new QgrsCandidate(2, 2, 1, 14);
		assertEquals(12, c.getScore());
	}
	
	@Test
	public void testScoring3() {
		//GGGGTGGGGTGGGGTGGGG
		QgrsCandidate c = new QgrsCandidate(4, 1, 1, 1);
		assertEquals(84, c.getScore());
	}
	
	@Test
	public void testScoring4() {
		//GGGTGGGTGGCAGAGCTGGGCTGGG
		QgrsCandidate c = new QgrsCandidate(3, 1, 10, 2);
		assertEquals(58, c.getScore());
	}
	
	@Test
	public void testScoring5() {
		//GGGCGGGCTGGGTTGG
		QgrsCandidate c = new QgrsCandidate(2, 2, 3, 3);
		assertEquals(20, c.getScore());
	}
	
	
}
