package qgrs.utils;

import java.io.IOException;

public abstract class Matrix {

	protected int rows;
	protected int cols;
	
	
	
	public Matrix(int row, int col) {
		this.rows = row;
		this.cols = col;
	}
	
	public abstract void open() throws IOException, Exception;
	public abstract void close() throws IOException;
	
	public abstract int get(int r, int c) ;
	public abstract void put(int r, int c, int value);
}
