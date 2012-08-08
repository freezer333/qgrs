package qgrs.utils;

import java.io.IOException;
import java.io.OutputStream;

public class StringOutputStream extends OutputStream {

	  StringBuilder mBuf = new StringBuilder();

	  public void write(int b) throws IOException {
	    mBuf.append((char) b);
	  }

	  @Override
	  public String toString() {
	    return mBuf.toString();
	  }
	}