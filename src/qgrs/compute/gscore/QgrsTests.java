package qgrs.compute.gscore;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class QgrsTests{
	
	
	
	@Test
	public void testScoring1() {
		QgrsCandidate c = new QgrsCandidate(3, 1, 1, 1);
		c.setMaxLength(30);
		assertEquals(42, c.getScore());
		
	}	
	@Test
	public void testScoring2() {
		QgrsCandidate c = new QgrsCandidate(2, 2, 1, 14);
		c.setMaxLength(30);
		assertEquals(20, c.getScore());
	}
	
	@Test
	public void testScoring3() {
		//GGGGTGGGGTGGGGTGGGG
		QgrsCandidate c = new QgrsCandidate(4, 1, 1, 1);
		c.setMaxLength(30);
		assertEquals(63, c.getScore());
	}
	
	@Test
	public void testScoring4() {
		//GGGTGGGTGGCAGAGCTGGGCTGGG
		QgrsCandidate c = new QgrsCandidate(3, 1, 10, 2);
		c.setMaxLength(30);
		assertEquals(33, c.getScore());
	}
	
	@Test
	public void testScoring5() {
		//GGGCGGGCTGGGTTGG
		QgrsCandidate c = new QgrsCandidate(2, 2, 3, 3);
		c.setMaxLength(30);
		assertEquals(20, c.getScore());
	}
	
	
}
