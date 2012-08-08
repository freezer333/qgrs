package qgrs.utils;

public class FrameBuffer {
	public int [] data = new int [DirectFrame.FRAME_SIZE/4];
	
	public byte [] bytes = new byte [DirectFrame.FRAME_SIZE];
	
	public FrameBuffer() {
		
	}
}