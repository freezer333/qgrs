package qgrs.data.analysis;

import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class URichDistrubution {
	
	
	public static void main(String[] args) throws UnknownHostException {
		DB db = new MongoClient().getDB("qgrs");
		Jongo jongo = new Jongo(db);
		MongoCollection uRichCollection = jongo.getCollection("uRich");
		
		
		for ( int i = 5; i < 500; i+=5) {
			String q = "{$and : [ {distanceFromPolyASite : {$gte : " + i +"}},  {distanceFromPolyASite : {$lte : " + (i+5) +"}}]} ";
			//System.out.println(q);
			long count = uRichCollection.count(q);
			System.out.println(i + "-" + (i+5) + "\t" + count);
			
		}
		
		
			
	}
	
	

}
