package framework.diagnostic;

public class MemoryReporter {
	
	
	public static void memoryReport() {
		if ( DiagnosticProperties.PerformanceDiagnostics ) {
			System.out.println("Currently " + (Runtime.getRuntime().freeMemory() / (1024*1024))+ " MB are free out of " +( Runtime.getRuntime().totalMemory()  / (1024*1024))+ " MB available");
		}
	}
}
