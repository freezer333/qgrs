package qgrs.utils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class BigMatrix extends Matrix {
	public static final boolean debug = true; 
	final int MB = 1024 * 1024;
	final int INT_SIZE = 4;
	int rowsInFrame;
	
	DirectFrame [] frames;

	List<DirectFrame> loadedFrames = new LinkedList<DirectFrame>();
	final int workingSet = 2;
	long tick = 0;
	
	public BigMatrix(int rows, int cols) {
		super(rows, cols);
		
	}
	
	
	@Override
	public void open() throws IOException  {
		
		int bytesNeeded = this.rows * this.cols * INT_SIZE;
		/*int numFrames = bytesNeeded / Frame.FRAME_SIZE + 
				((bytesNeeded % Frame.FRAME_SIZE == 0) ? 0 : 1);*/
		rowsInFrame = Frame.FRAME_SIZE / (this.cols * INT_SIZE) ;//+ 
				//(Frame.FRAME_SIZE % (this.cols * INT_SIZE) == 0 ? 0 : 1);
		int numFrames = rows / rowsInFrame + ((rows%rowsInFrame==0)?0:1);
		frames = new DirectFrame[numFrames];
		
		if ( debug ) {
			System.out.println("Bytes needed:      " + bytesNeeded);
			System.out.println("Frame Size:        " + Frame.FRAME_SIZE);
			System.out.println("Rows per Frame:    " + rowsInFrame);
			System.out.println("Bytes per row:     " + this.cols * INT_SIZE);
			System.out.println("Number of frames:  " + numFrames);
		}
		
		int row = 0;
		for ( int i = 0; i < numFrames; i++ ) {
			frames[i] = new DirectFrame(i, row, rowsInFrame, cols);
			row += rowsInFrame;
		}
	}

	@Override
	public void close() throws IOException{
		for ( DirectFrame f : this.frames ) {
			f.close();
		//	System.out.println("Total Matrix time:  " + f.getTotalTime()/1000000000.0 + " seconds.  Disk time:  " + f.getDiskTime() / 1000000000.0 + " seconds");
		}
	}
	
	DirectFrame loadFrame(int r, int c) {
		for ( DirectFrame f : this.loadedFrames ) {
			if ( f.hasRow(r)) return f;
		}
		FrameBuffer frameBuffer = null;
		if ( this.loadedFrames.size() >= this.workingSet) {
			
			DirectFrame leastRecent = this.loadedFrames.get(0);
			for ( DirectFrame f : this.loadedFrames ) {
				if ( f.getLastUsedTime() < leastRecent.getLastUsedTime() ) {
					leastRecent = f;
				}
			}
			if ( debug) {
				System.out.println("Page Fault:  Evicting frame:  " + leastRecent.getFrameNumber());
			}
			// evict a frame (least recently used
			frameBuffer = leastRecent.unload();
			this.loadedFrames.remove(leastRecent);
		}
		
		int f = r / rowsInFrame;
		try {
			if ( frameBuffer == null ) {
				frameBuffer = new FrameBuffer();
				System.out.println("CREATED FRAME BUFFER");
			}
			frames[f].load(frameBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		this.loadedFrames.add(frames[f]);
		if ( debug) System.out.println("                 Loading Frame:  " + frames[f].getFrameNumber());
		return frames[f];
		
		
	}

	@Override
	public int get(int r, int c) {
		tick++;
		DirectFrame f = loadFrame(r, c);
		return f.get(r,  c, tick);
	}

	@Override
	public void put(int r, int c, int value) {
		tick++;
		DirectFrame f = loadFrame(r, c);
		f.put(r, c, value, tick);

	}
	public static void print(Matrix m, int size) {
		for ( int i = 0; i < size; i++ ) {
			for ( int j = 0; j < size; j++ ) {
				System.out.print(m.get(i, j) + " ");
			}
			System.out.println();
		}
	}
	
	
	
}
