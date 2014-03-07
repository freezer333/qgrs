package qgrs.data.analysis;

import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class NRichDistrubution {
	
	public static String makeQ(char n, int start, int end) {
		String q = "{$and : [ {n : '" + n + "'}, {distanceFromPolyASite : {$gte : " + start +"}},  {distanceFromPolyASite : {$lte : " + (end) +"}}]} ";
		return q;
	}
	
	
	public static void main(String[] args) throws UnknownHostException {
		DB db = new MongoClient().getDB("qgrs");
		Jongo jongo = new Jongo(db);
		MongoCollection nRichCollection = jongo.getCollection("nRich");
		
		
		long u, g, a, c;

		System.out.println("Range\tU\tG\tA\tC");
		
		
		for ( int i = 0; i < 500; i+=5) {
			u = nRichCollection.count(makeQ('U', i, i+5));
			g = nRichCollection.count(makeQ('G', i, i+5));
			a = nRichCollection.count(makeQ('A', i, i+5));
			c = nRichCollection.count(makeQ('C', i, i+5));
			
			System.out.println(i + "-" + (i+5) + "\t" + u + "\t" + g + "\t" + a + "\t" + c);
			
		}
		
		
			
	}
	
	

}
