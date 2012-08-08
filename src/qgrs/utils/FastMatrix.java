package qgrs.utils;

import java.io.IOException;

import framework.diagnostic.DiagnosticProperties;
import framework.diagnostic.MemoryReporter;

public class FastMatrix extends Matrix {
	int [][] data ;
	int myCount ;
	static int count = 0;

	public FastMatrix(int row, int col) {
		
		super(row, col);
		myCount = count++;
		if ( DiagnosticProperties.PerformanceDiagnostics ) System.out.println("Creating matrix " + myCount);
	}

	@Override
	public void open() throws Exception {
		// TODO Auto-generated method stub
		int times = 0;
		while ( times < 15) {
			try {
				data = new int[rows][cols];
				return;
			}
			catch (Throwable t) {
				System.gc();
				Thread.currentThread().sleep(1000);
				times++;
				System.out.println("Waiting for enough memory to become available for matrix") ;
				MemoryReporter.memoryReport();
			}
		}
		
	}
	@Override
	protected void finalize() throws Throwable {
	    try {
	       if ( DiagnosticProperties.PerformanceDiagnostics )System.out.println("GARBAGE " + myCount);
	    } finally {
	        super.finalize();
	    }
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		data = null;
	}

	@Override
	public int get(int r, int c) {
		return data[r][c];
	}

	@Override
	public void put(int r, int c, int value) {
		data[r][c] = value;
	}

}
