package qgrs.utils;


public class MatrixFactory {

	
	public static Matrix build(int r, int c) {
		int count = 0;
		while ( count < 5 ) {
			try {
				return new FastMatrix(r, c);
			} catch (Throwable t) {
				System.gc();
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			count++;
		}
		return new FastMatrix(r, c);
	}
	
	
	/* Test harness for matrix code */
	public static void main(String args[]) throws Exception {
		final int size = 10000;
		long start = System.nanoTime();
		Matrix m = build(size, size);
		m.open();
		for ( int i = 0; i < size; i++ ) {
			for ( int j = 0; j < size; j++ ) {
				
				m.put(i, j, (i+1) * (j+1));
				
			}
		}
		
		//print(m, size);
		for ( int i = 0; i < size; i++ ) {
			for ( int j = 0; j < size; j++ ) {
				if ( m.get(i, j) != (i+1) * (j+1))  {
					throw new RuntimeException("Value at " + i + ", " + j + " expected " + (i+1) * (j+1) + " , got " +m.get(i, j));
				}
			}
		}
		m.close();
		
		System.out.println("Complete in " + (System.nanoTime() - start)/1000000000.0);
	}
}
