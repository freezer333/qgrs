package qgrs.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import qgrs.data.BaseSymbol;


public class BaseSymbolTest{
	
	private void assertParse(BaseSymbol expected, char input) {
		BaseSymbol bs;
		bs = BaseSymbol.parseSymbol(input);
		assertEquals(input +" is not parsed to N", expected, bs);
	}
	
	@Test
	public void testN() {
		assertParse(BaseSymbol.N, 'n');
		assertParse(BaseSymbol.N, 'N');
		assertParse(BaseSymbol.N, 'x');
	}
	
	@Test
	public void testUpperLowerCases() {
		assertParse(BaseSymbol.A, 'A');
		assertParse(BaseSymbol.A, 'a');
		
		assertParse(BaseSymbol.T, 'T');
		assertParse(BaseSymbol.T, 't');
		
		assertParse(BaseSymbol.C, 'C');
		assertParse(BaseSymbol.C, 'c');
		
		assertParse(BaseSymbol.G, 'G');
		assertParse(BaseSymbol.G, 'g');
		
	}
}
