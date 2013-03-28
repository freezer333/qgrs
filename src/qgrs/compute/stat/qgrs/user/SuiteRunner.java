package qgrs.compute.stat.qgrs.user;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import qgrs.compute.stat.qgrs.QgrsRunner;

public class SuiteRunner {

	public final static List<QgrsRunner> runners = Arrays.asList(
			new HumanMouseQgrsDistanceRunner(), 
			new HumanMouseQgrsRunner(), 
			new OntologyQgrsRunner()
			);
	
	public static void main(String[] args) throws Exception {
		
		ExecutorService executorService = Executors.newFixedThreadPool(runners.size());
		executorService.invokeAll(runners);
		System.exit(0);
	}

}
