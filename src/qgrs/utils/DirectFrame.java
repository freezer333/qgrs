package qgrs.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class DirectFrame {
	
	

	
	
	public static final int FRAME_SIZE = 80 * 1024 * 1024;
	int realRow;
	int rows;
	int cols;
	
	
	boolean dirty = false;
	boolean onDisk = false;
	long lastUsed;
	long reads = 0;
	long writes = 0;
	int frameNumber;
	
	//int [] data;
	FrameBuffer frameBuffer;
	
	File f;
	
	//long diskTime = 0;
	//long totalTime = 0;
	
	
	public DirectFrame(int frameNumber, int realRow, int rows, int cols) throws IOException {
		this.realRow = realRow;
		this.rows = rows;
		this.cols = cols;
		this.frameNumber = frameNumber;
		f = File.createTempFile("seq", ".bin");
		if ( BigMatrix.debug ) System.out.println("Created frame file:  " + f.getAbsolutePath());
		f.deleteOnExit();
	}
	public int getFrameNumber() {
		return frameNumber;
	}
	
	public void close() throws IOException {
		this.unload();
		if ( BigMatrix.debug ) System.out.println("Deleting frame file:  " + f.getAbsolutePath());
		if ( onDisk) f.delete();
		
		//if ( BigMatrix.debug ) System.out.println("Total Disk Caching Time: " + (diskTime/1000000000.0) + " seconds");
	}
	
	public boolean hasRow(int r) {
		return realRow <= r && (realRow+rows) > r;
	}
	
	int [] bytesToIntegers(byte [] data) throws IOException {
		int [] ints = new int[data.length/4];
		ByteArrayInputStream bais = new ByteArrayInputStream (data);
		DataInputStream dis = new DataInputStream (bais);
		for ( int i = 0; i < data.length/4; i++ ) {
			ints[i] = dis.readInt();
		}
		return ints;
	}
	
	byte[] integersToBytes(int[] values) throws IOException
	{
		
	   ByteArrayOutputStream baos = new ByteArrayOutputStream();
	   DataOutputStream dos = new DataOutputStream(baos);
	   for(int i=0; i < values.length; ++i)
	   {
	        dos.writeInt(values[i]);
	   }

	   return baos.toByteArray();
	}
	
	private void loadFromFile() throws IOException {
		FileInputStream in = new FileInputStream(this.f);
		in.read(frameBuffer.bytes);
		in.close();
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(frameBuffer.bytes);
		IntBuffer buffer = byteBuffer.asIntBuffer();
		buffer.get(frameBuffer.data);
		buffer = null;
		
	}
	
	private void writeToFile() throws IOException {
		FileOutputStream out = new FileOutputStream(this.f);
		byte [] byteArray = integersToBytes(frameBuffer.data);
		out.write(byteArray);
		out.close();
		onDisk = true;
	}
	
	
	public void load(FrameBuffer fb) throws IOException {
		this.frameBuffer = fb;
		if ( onDisk ) {
			loadFromFile();
		}
		this.dirty = false;
		reads=0;
		writes=0;
	}
	
	
	public FrameBuffer unload() {
		if ( dirty && frameBuffer != null) {
			try {
				writeToFile();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException (e);
			}
		}
		//data = null;
		if ( BigMatrix.debug ) {System.out.println("Unloaded after " + reads + " reads, " + writes + " writes");}
		System.gc();
		return frameBuffer;
	}
	
	/*public long getDiskTime() {
		return diskTime;
	}*/
	public long getLastUsedTime() {
		return this.lastUsed;
	}

	// Returns the index relative to the int buffer.
	private int index(int r, int c) {
		int row = r - realRow;
		return ( (row == 0 ) ? c : (row)*cols + c);
	}
	public int get(int r, int c, long tick) {
		reads++;
		lastUsed = tick;
		//return buffer.get(index(r, c));
		return frameBuffer.data[index(r, c)];
	}
	public void put(int r, int c, int v, long tick ) {
	try {
		writes++;
			lastUsed = tick;
			//buffer.put(index(r, c), v);
			frameBuffer.data[index(r, c)] = v;
			dirty = true;
		}
		catch(RuntimeException e) {
			e.printStackTrace();
			System.out.println(r + " , " + c);
		
		}
	}
	/*public long getTotalTime() {
		return totalTime;
	}*/
}
