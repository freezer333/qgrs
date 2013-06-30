package qgrs.compute.stat.qgrs.user;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import qgrs.compute.stat.qgrs.QgrsRunner;

public class SuiteRunner {

	public final static List<HumanComparisonQgrsRunner> runners = Arrays.asList(
			new HumanComparisonQgrsRunner("Mus musculus") , 
			new HumanComparisonQgrsRunner("Caenorhabditis elegans") , 
			new HumanComparisonQgrsRunner("Canis lupus familiaris") , 
			new HumanComparisonQgrsRunner("Danio rerio") , 
			new HumanComparisonQgrsRunner("Drosophila melanogaster") , 
			new HumanComparisonQgrsRunner("Kluyveromyces lactis NRRL Y-1140") , 
			new HumanComparisonQgrsRunner("Pan troglodytes") 
			);
	public static void main(String[] args) throws Exception {
		
		
		for ( HumanComparisonQgrsRunner runner : runners ) {
			runner.execute();
		}
		
		
		System.exit(0);
	}

}
