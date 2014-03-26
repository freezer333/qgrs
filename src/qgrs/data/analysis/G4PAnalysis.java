package qgrs.data.analysis;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import qgrs.data.mongo.primitives.jongo.G4;
import qgrs.data.mongo.primitives.jongo.MRNA;
import qgrs.data.providers.MongoSequenceProvider;

import com.mongodb.DB;
import com.mongodb.MongoClient;

	

public class G4PAnalysis extends Analysis {

	DB db;
	Jongo jongo ;
	MongoCollection principals;
	MongoSequenceProvider sp;
	
	public G4PAnalysis () throws Exception{
		super(null,null);
		db = new MongoClient().getDB("qgrs");

		jongo = new Jongo(db);
		principals = jongo.getCollection("principals");
		
		sp = new MongoSequenceProvider(jongo);
		
	}
	
	public void run(){
		Iterable<MRNA> all = principals.find().as(MRNA.class);
		for(MRNA mrna : all){
			evaluate(mrna);
		}
	}
	@Override
	public void evaluate(MRNA mrna) {
		// TODO Auto-generated method stub
		double count = 0;
		System.out.print(mrna.getAccessionNumber() + "\t");
		for(int i = 0 ; i < mrna.getUtr5().getEnd(); i++ ){
			if(hasG4(i,mrna)){
				count++;
			}
		}
		System.out.println(count/mrna.getUtr5().getEnd());
	}

	boolean hasG4(int nt, MRNA mrna){
		for(G4 g4 : mrna.getG4s()){
			if(inG4(g4,nt)){
				return true;
			}else{
				for(G4 overlaps : g4.getOverlappingMotifs()){
					if(inG4(overlaps, nt)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	boolean inG4(G4 g4, int nt){
		return(g4.getTetrad1() <= nt && (g4.getTetrad4() + g4.getNumTetrads()) >= nt);
	}
	
	@Override
	public void report() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		G4PAnalysis a = new G4PAnalysis();
		a.run();
	}

}
