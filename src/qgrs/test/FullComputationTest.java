package qgrs.test;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import qgrs.data.cache.XmlWriteFileCache;
import qgrs.input.AccessionNumberInputProvider;
import qgrs.job.AlignmentJob;

@RunWith(Parameterized.class)
public class FullComputationTest {
	
	final String principle;
	final String comparison;
	
	
	public FullComputationTest(String principle, String comparison) {
		this.principle = principle;
		this.comparison = comparison;
	}
	
	@Parameters
	 public static Collection<Object[]> generateData()
	 {
	 	 return Arrays.asList(new Object[][] {
			 { "NM_006245.2", "NM_009358.3" },
			 { "NM_001127328.1", "NM_001110816.1" }
		 });
	 }
	   
	
	@Ignore
	@Test
	public void testComputation() throws Exception {
	
		String current = new java.io.File( "." ).getCanonicalPath() + "\\testData";
		
		String baseName = principle + "_x_" + comparison + ".xml";;
		String compareFilename = current + "\\comparison_xml\\" + baseName;
		String failureFilename = current + "\\comparison_failures\\" + baseName;
		
		File failFile = new File(failureFilename);
		File compareFile = new File(compareFilename);
		
		System.out.println("Comparing -> " + current + "\\" + baseName);
		
		// Run a full computation.
		AlignmentJob job = new AlignmentJob(new AccessionNumberInputProvider(principle, comparison), 
							null, new XmlWriteFileCache ( null, failFile));
		
		job.runJob();
		Thread.sleep(2000);
		
	
		
		// Compare XML produced with compareFilename
		
		
		// If they are equal, delete failureFilename
		
		// otherwise throw exception (assertion failure)
		
	}
	
}
