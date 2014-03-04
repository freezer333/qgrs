package qgrs.data.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import qgrs.data.mongo.primitives.jongo.MRNA;
import qgrs.data.providers.MongoSequenceProvider;
import qgrs.data.providers.SequenceProvider;
import qgrs.data.providers.SequenceProvider.Key;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class SequenceExport {

	public static void main(String[] args) throws Exception {
		
		DB db = new MongoClient().getDB("qgrs");

		Jongo jongo = new Jongo(db);
		MongoCollection principals = jongo.getCollection("principals");
		Iterable<MRNA> all = principals.find().as(MRNA.class);
		SequenceProvider seqProvider = new MongoSequenceProvider(jongo);
		
		
		
		
		double maxCharsPerFile = 1e9;
		double currentCharsInFile = 0;
		int fileNumber = 1;
		BufferedWriter bw = openFile(fileNumber);
		int count = 0;
		for ( MRNA mrna : all ) {
			if ( currentCharsInFile > maxCharsPerFile ) {
				bw.close();
				bw = openFile(fileNumber++);
				currentCharsInFile = 0;
				System.out.println("------------------------\nNew File Openned\n---------------------");
			}
			
			bw.write(">\n" + mrna.getAccessionNumber() + "\n");
			HashMap<Key,Object> sequence = seqProvider.getSequence(mrna.getAccessionNumber());
			bw.write(sequence.get(Key.Sequence) + "\n");
			currentCharsInFile+= sequence.get(Key.Sequence).toString().length();
			count++;
			System.out.println("Wrote " + count + " (" + currentCharsInFile + ")\t" + mrna.getAccessionNumber() );
			
		}
		bw.close();
	}
	
	static BufferedWriter openFile(int number) throws IOException {
		File file = new File("mrna-"+number+".dat");
		 
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		return bw;
	}
	

}
