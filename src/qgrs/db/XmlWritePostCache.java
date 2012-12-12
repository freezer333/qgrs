package qgrs.db;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.DecimalFormat;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import framework.io.GZipper;

public class XmlWritePostCache extends XmlWriteCache {

	public final URL serverUrl;
	
	public XmlWritePostCache(String serverUrl, String contextPath, int port) {
		super();
		try {
			this.serverUrl = new URL( "http", serverUrl, port, "/" + contextPath + "/app/populate");
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	
	
	@Override
	void handleXmlResult(Document xmlDoc) {
		try {
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			String rawXmlContent = outputter.outputString(xmlDoc);
			File f = File.createTempFile("post-result", ".xml");
			FileOutputStream fos = new FileOutputStream(f);
			GZipper zip = new GZipper();
			byte [] zipped = zip.deflate(rawXmlContent);
			//System.out.println("Compressed " + new DecimalFormat("0.00").format(rawXmlContent.length()/(1024.0*1024.0)) + "MB into " + new DecimalFormat("0.00").format(zipped.length/(1024.0*1024.0)) + "MB for posting");
			fos.write(zipped);
			fos.close();
			// write it to the file.
			
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

			HttpPost        post   = new HttpPost( serverUrl.toString() );
			MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );

			// For File parameters
			entity.addPart( "xml", new FileBody((f ), "application/zip" ));
			post.setEntity( entity );
			String response = EntityUtils.toString( client.execute( post ).getEntity(), "UTF-8" );
			client.getConnectionManager().shutdown();
			
			f.delete();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
