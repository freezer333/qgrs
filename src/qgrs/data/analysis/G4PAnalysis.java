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
	
	Jongo getJongo() {
		return jongo;
	}
	
	public void run(){
		Iterable<MRNA> all = principals.find().as(MRNA.class);
		
		//THIS IS JUST A COUNTER TO IMIT NUMBER OF GENES
		int counter = 0;
		//
		
		for(MRNA mrna : all){
			if ( counter % 1000 == 0 ) {
			System.out.println("Evaluating " + counter + " - > " + mrna.getAccessionNumber());
			}
			// THIS IS JUST SO WE CAN RUN ON FEWER GENES
			/*
			if(counter < 100) {
				counter ++;
			}
			else{
				break;
			}*/
			counter++;
			
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
		DB db = new MongoClient().getDB("qgrs");

		Jongo jongo = new Jongo(db);
		
		
		// all mrna
		MrnaFilter mrnaFilter = new MrnaFilter("All Human mRNA");
		//G4Filter g4filter = new G4Filter(0, Region.FivePrime, 2);
		G4Filter [] g4Filter = new G4Filter[2];
		g4Filter[0] = new G4Filter(0, Region.FivePrime, 3);
		g4Filter[1] = new G4Filter(0, Region.FivePrime, 4);
		//g4Filter[2] = new G4Filter(0, Region.FivePrime, 6);
		
		for ( G4Filter g4filter : g4Filter ) {
			System.out.println("Running G4 Filter Set");
			G4PAnalysis as = new G4PAnalysis(mrnaFilter, g4filter);
			//as.run();
			
			MongoSequenceProvider sp = new MongoSequenceProvider(jongo);
			mrnaFilter= new MrnaSequenceFilter(0.37, "Hi G-Rich (37%)", sp);
			as = new G4PAnalysis(mrnaFilter, g4filter);
			as.run();
			
			
			// Apoptosis mrna
			mrnaFilter= new MrnaFilter("Apoptosis");
			as = new G4PAnalysis(mrnaFilter, g4filter);
			mrnaFilter.addOntologyTerms(new String [] {"ubiquitin-protein ligase activity", "apoptotic process", "apoptotic signaling pathway", "induction of apoptosis", "execution phase of apoptosis", "negative regulation of apoptotic process", "positive regulation of apoptotic process"});
			//as.run();
			
			// Brain development mrna
			mrnaFilter= new MrnaFilter("Brain Development");
			as = new G4PAnalysis(mrnaFilter, g4filter);
			//mrnaFilter.addOntologyTerms(new String [] {"brain segmentation", "brain morphogenesis", "central complex development", "forebrain development","hindbrain development","midbrain development"});
			//as.run();
			
			// Epigenetics mrna
			mrnaFilter= new MrnaFilter("Epigentics");
			as = new G4PAnalysis(mrnaFilter, g4filter);
			mrnaFilter.addOntologyTerms(new String [] {"DNA-methyltransferase", "methyl-CpG binding", "methyl-CpNpN binding", "DNA hypermethylation", "DNA hypomethylation"});
			//as.run();
			
			// Negative Regulation of Cell Proliferation mrna
			mrnaFilter= new MrnaFilter("Negative Regulation of Cell Proliferation");
			as = new G4PAnalysis(mrnaFilter, g4filter);
			mrnaFilter.addOntologyTerms(new String [] {"negative Regulation of Cell Proliferation"});
			//as.run();
			
			// Oncogenes mrna
			mrnaFilter= new MrnaFilter("Oncogenes");
			as = new G4PAnalysis(mrnaFilter, g4filter);
			mrnaFilter.addGeneNameSearchTerms(new String [] {"oncogene"});
			//as.run();
			
			// Positive Regulation of Cell Proliferation mrna
			mrnaFilter= new MrnaFilter("Positive Regulation of Cell Proliferation");
			as = new G4PAnalysis(mrnaFilter, g4filter);
			mrnaFilter.addOntologyTerms(new String [] {"positive regulation of cell proliferation"});
			//as.run();
			
			// Regulation of Cell Cycle mrna
			mrnaFilter= new MrnaFilter("Regulation of Cell Cycle");
			as = new G4PAnalysis(mrnaFilter, g4filter);
			mrnaFilter.addOntologyTerms(new String [] {"regulation of cell cycle"});
			//as.run();
			
			// Transcription Factor Binding mrna
			mrnaFilter= new MrnaFilter("Transcription Factor Binding");
			as = new G4PAnalysis(mrnaFilter, g4filter);
			mrnaFilter.addOntologyTerms(new String [] {"transcription factor complex", "transcription factor binding"});
			//as.run();
		
		}
				
	}

}

