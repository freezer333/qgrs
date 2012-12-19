package framework.io;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipper {

	
	public byte [] deflate(String content)  {
		try {
			ByteArrayOutputStream o = new ByteArrayOutputStream();
			GZIPOutputStream out = new GZIPOutputStream(o);
			out.write(content.getBytes());
			out.close();
			return o.toByteArray();
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	/*public byte [] inflate(String content)  {
		try {
			ByteArrayInputStream o = new ByteArrayInputStream(content.getBytes()); 
			ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
			GZIPInputStream  in = new GZIPInputStream (o);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				outBytes.write(buf, 0, len);
			}
			in.close();
			return outBytes.toByteArray();
			
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}*/
	public String inflate(InputStream input)  {
		try {
			ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
			GZIPInputStream  in = new GZIPInputStream (input);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				outBytes.write(buf, 0, len);
			}
			in.close();
			return outBytes.toString();//new String(Base64.decode(outBytes.toByteArray()));
			
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	/*public static void main(String[] args) {
		GZipper zip = new GZipper();
		String content = "ABABABABABABABABABAB";
		String compressed = zip.deflate(content);
		System.out.println(content + " -> " + compressed + " -> " + zip.inflate(compressed));
		
	}*/
}
