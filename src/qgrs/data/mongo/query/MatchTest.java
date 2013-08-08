package qgrs.data.mongo.query;

import junit.framework.Assert;

import org.junit.Test;

public class MatchTest {

	@Test
	public void test() {
		Match e = new Match("accessionNumber","NM_201348.1");
		
		 Assert.assertTrue(e.toString().equals("{\"accessionNumber\":\"NM_201348.1\"}"));
	}
}
