package qgrs.controllers;

import java.net.UnknownHostException;

import org.jdom.Document;
import org.jdom.Element;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import qgrs.data.mongo.primitives.jongo.MRNA;
import qgrs.view.XslViews;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class DbStats extends AbstractController {

	public DbStats() {
		super();
		this.supportedUrls.add("/app/stats");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    
	    DB db;
		try {
			db = new MongoClient().getDB("qgrs");
			Jongo jongo = new Jongo(db);
			MongoCollection principals = jongo.getCollection("principals");
			MongoCollection uRichCollection = jongo.getCollection("uRich");
			
			long principalCount = principals.count();
			long uRichCount = uRichCollection.count();
			
			Element counts = new Element("counts");
			counts.setAttribute("principal", String.valueOf(principalCount));
			counts.setAttribute("urich", String.valueOf(uRichCount));
			root.addContent(counts);
		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.MongoStats, pageXml));	
	}

}
