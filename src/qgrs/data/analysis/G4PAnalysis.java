package qgrs.data.analysis;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
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
	G4Filter g4Filter;
	
	DescriptiveStatistics filtered = new DescriptiveStatistics();
	DescriptiveStatistics all = new DescriptiveStatistics();
	
	public G4PAnalysis (MrnaFilter mrnaFilter, G4Filter g4Filter) throws Exception{
		super(null,mrnaFilter);
		db = new MongoClient().getDB("qgrs");

		jongo = new Jongo(db);
		principals = jongo.getCollection("principals");
		
		sp = new MongoSequenceProvider(jongo);
		
		this.g4Filter = g4Filter;
		
	}
	
	public void run(){
		Iterable<MRNA> all = principals.find().as(MRNA.class);
		
		//THIS IS JUST A COUNTER TO IMIT NUMBER OF GENES
		int counter = 0;
		//
		
		for(MRNA mrna : all){
			System.out.println("Evaluating " + counter + " - > " + mrna.getAccessionNumber());
			
			// THIS IS JUST SO WE CAN RUN ON FEWER GENES
			if(counter < 100) {
				counter ++;
			}
			else{
				break;
			}
			
			evaluate(mrna);
			
		}
		
		System.out.println("===============================");
		System.out.println("G4P Analsysis complete");
		System.out.println("MRNA Filter");
		System.out.println(mrnaFilter.toString());
		System.out.println("G4 Filter");
		System.out.println(g4Filter.toString());
		System.out.println("-------------------------------");
		System.out.println("Mean G4P:  " + this.filtered.getMean());
		System.out.println("Mean Pop:  " + this.all.getMean());
		System.out.println("Std Pop:   " + this.all.getStandardDeviation());
		System.out.println("Z-Score:   " + zScore());
		System.out.println("Significant?  " + significant() );
		System.out.println("===============================");
		
		
	}
	
	double zScore() {
		double num = this.filtered.getMean() - all.getMean();
		double den = this.all.getStandardDeviation();
		return num / den;
	}
	boolean significant() {
		return Math.abs(zScore()) >= 1.96;
	}
	@Override
	public void evaluate(MRNA mrna) {
		
		double g4p = calcG4P(mrna);
		// NaN is returned if no 5' in mRNA
		if ( !Double.isNaN(g4p)) {
			all.addValue(g4p);
			if ( this.mrnaFilter.acceptable(mrna)){
				filtered.addValue(g4p);
			}
		}
	}
	
	private double calcG4P(MRNA mrna) {
		double count = 0;
		for(int i = 0 ; i < mrna.getUtr5().getEnd(); i++ ){
			if(hasG4(i,mrna)){
				count++;
			}
		}
		
		return count / mrna.getUtr5().getEnd();
		
	}

	boolean hasG4(int nt, MRNA mrna){
		for(G4 g4 : mrna.getG4s()){
			//added this if-statement to make sure G4 being used is acceptable
			if(this.g4Filter.acceptable(mrna, g4)){	
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


		
		/**
		 * 	Matt - set up new mRNA Filters (like your previous analyses and run them here
		 *  by replacing the mrna filter 
		 *  
		 *  Don't forget to remove 100 cap.
		 * 
		 * 
		 * 
		 */
		
		
		MrnaFilter mrnaFilter = new MrnaFilter();
		G4Filter g4filter = new G4Filter(0.0, Region.FivePrime);
		
		G4PAnalysis a = new G4PAnalysis(mrnaFilter, g4filter);
		a.run();
	}

}

