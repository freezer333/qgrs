package qgrs.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class Frame {
	public static final int FRAME_SIZE = 80 * 1024 * 1024;
	int realRow;
	int rows;
	int cols;
	MappedByteBuffer buffer;
	boolean dirty = false;
	long lastUsed;
	
	int frameNumber;
	
	File f;
	RandomAccessFile raf;
	FileChannel channel;
	
	public Frame(int frameNumber, int realRow, int rows, int cols) throws IOException {
		this.realRow = realRow;
		this.rows = rows;
		this.cols = cols;
		this.frameNumber = frameNumber;
		//System.out.println("Frame created for rows " + realRow + "-" + (realRow+rows));
		
		f = File.createTempFile("seq", ".bin");
		System.out.println("Created frame file:  " + f.getAbsolutePath());
		f.deleteOnExit();
		raf = new RandomAccessFile(f, "rw"); 
		raf.setLength(FRAME_SIZE);
		channel = raf.getChannel();
	}
	public int getFrameNumber() {
		return frameNumber;
	}
	
	public void close() throws IOException {
		this.unload();
		System.gc();
		channel.close();
		raf.close();
		
		System.out.println("Deleting frame file:  " + f.getAbsolutePath());
		f.delete();
	}
	
	public boolean hasRow(int r) {
		return realRow <= r && (realRow+rows) > r;
	}
	
	public void load() throws IOException {
		buffer = channel.map(MapMode.READ_WRITE, 0, FRAME_SIZE);
		buffer.load();
		this.dirty = false;
	}
	public void unload() {
		if ( dirty && buffer != null) buffer.force();
		buffer = null;
		//System.gc();
	}
	
	public long getLastUsedTime() {
		return this.lastUsed;
	}
	
	private int index(int r, int c) {
		int row = r - realRow;
		return 4 * ( (row == 0 ) ? c : (row)*cols + c);
	}
	public int get(int r, int c) {
		if ( index(r, c) > FRAME_SIZE) {
			System.out.println("Reading index " + index(r, c) + " out of " + FRAME_SIZE);
			throw new RuntimeException("Bad index");
		}
		lastUsed = System.currentTimeMillis();
		return buffer.getInt(index(r, c));
	}
	public void put(int r, int c, int v ) {
		if ( index(r, c) > FRAME_SIZE) {
			System.out.println("Putting index " + index(r, c) + " > " + r  + ", " + c + " ^ " + this.realRow+ " out of " + FRAME_SIZE);
			throw new RuntimeException("Bad index");
		}
		try {
			lastUsed = System.currentTimeMillis();
			buffer.putInt(index(r, c), v);
			//System.out.println("put " + index(r, c) + " -> " + r + ", " + c + " = " + get(r, c));
			dirty = true;
		}
		catch(RuntimeException e) {
			e.printStackTrace();
			System.out.println(r + " , " + c);
		
		}
	}
}
