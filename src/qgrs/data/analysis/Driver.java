package qgrs.data.analysis;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.LinkedList;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.MRNA;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class Driver {
	
	LinkedList<Analysis> analyses = new LinkedList<Analysis>();
	
	
	public Driver (Analysis ...analyses ) {
		for ( Analysis a : analyses) {
			this.analyses.add(a);
		}
	}
	public Driver (LinkedList<Analysis> analyses ) {
		this.analyses = analyses;
	}
	
	public void run(MongoCollection principals) {
		Iterable<MRNA> all = principals.find().as(MRNA.class);
	
		for ( MRNA mrna : all ) {
			for ( Analysis a : this.analyses ) {
				a.evaluate(mrna);
			}
		}
		
		for (Analysis a : this.analyses ) {
			a.report();
		}
	}
	
	
	
	public static void main(String [] args) throws UnknownHostException {
		DB db = new MongoClient().getDB("qgrs");

		Jongo jongo = new Jongo(db);
		MongoCollection principals = jongo.getCollection("principals");
		
		LinkedList<Analysis> as = new LinkedList<Analysis>();
		
		G4Filter g90 = new G4Filter(90, Region.ThreePrime);
		G4Filter g95 = new G4Filter(95, Region.ThreePrime);
		
		
		// Camille's polyA signal study
		as.add(new PolyADistanceAnalysis(new MrnaFilter("1+ polyA, 90+ qgrs homology").setMinNumberOfPolyA(1), g90, 0, 500, 20, DistanceDirection.Upstream));
		as.add(new PolyADistanceAnalysis(new MrnaFilter("2+ polyA, 90+ qgrs homology").setMinNumberOfPolyA(2), g90, 0, 500, 20, DistanceDirection.Upstream));
		as.add(new PolyADistanceAnalysis(new MrnaFilter("1+ polyA, 95+ qgrs homology").setMinNumberOfPolyA(1), g95, 0, 500, 20, DistanceDirection.Upstream));
		as.add(new PolyADistanceAnalysis(new MrnaFilter("2+ polyA, 95+ qgrs homology").setMinNumberOfPolyA(2), g95, 0, 500, 20, DistanceDirection.Upstream));
		
		as.add(new PolyADistanceAnalysis(new MrnaFilter("1+ polyA, 90+ qgrs homology").setMinNumberOfPolyA(1), g90, 0, 500, 20, DistanceDirection.Downstream));
		as.add(new PolyADistanceAnalysis(new MrnaFilter("2+ polyA, 90+ qgrs homology").setMinNumberOfPolyA(2), g90, 0, 500, 20, DistanceDirection.Downstream));
		as.add(new PolyADistanceAnalysis(new MrnaFilter("1+ polyA, 95+ qgrs homology").setMinNumberOfPolyA(1), g95, 0, 500, 20, DistanceDirection.Downstream));
		as.add(new PolyADistanceAnalysis(new MrnaFilter("2+ polyA, 95+ qgrs homology").setMinNumberOfPolyA(2),g95, 0, 500, 20, DistanceDirection.Downstream));
		
		
		as.add(new GroupedPolyADistanceAnalysis(new MrnaFilter("1+ polyA, 90+ qgrs homology").setMinNumberOfPolyA(1), g90, 0, 500, 100, DistanceDirection.Upstream));
		as.add(new GroupedPolyADistanceAnalysis(new MrnaFilter("1+ polyA, 95+ qgrs homology").setMinNumberOfPolyA(1), g95, 0, 500, 100, DistanceDirection.Upstream));
		as.add(new GroupedPolyADistanceAnalysis(new MrnaFilter("1+ polyA, 90+ qgrs homology").setMinNumberOfPolyA(1), g90, 0, 500, 100, DistanceDirection.Downstream));
		as.add(new GroupedPolyADistanceAnalysis(new MrnaFilter("1+ polyA, 95+ qgrs homology").setMinNumberOfPolyA(1), g95, 0, 500, 100, DistanceDirection.Downstream));
		
		
		
		MrnaFilter m = new MrnaFilter("2 polyA").setMinNumberOfPolyA(2).setMaxNumberOfPolyA(2);
		G4Filter [] g4s = {g90, g95};

		for (G4Filter g : g4s ) {
			as.add(new PolyADistanceAnalysis(m, g, 0, 500, 20, DistanceDirection.Upstream).setDistalOnly(true));
			as.add(new PolyADistanceAnalysis(m, g, 0, 500, 20, DistanceDirection.Downstream).setDistalOnly(true));
			as.add(new PolyADistanceAnalysis(m, g, 0, 500, 20, DistanceDirection.Upstream).setProximateOnly(true));
			as.add(new PolyADistanceAnalysis(m, g, 0, 500, 20, DistanceDirection.Downstream).setProximateOnly(true));
		}
		
		
		
		
		
		// Matt's 5' UTR study
		MrnaFilter filter;
		filter= new MrnaFilter("All Human mRNA");
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.FivePrime), 0, 1000, 20, DistanceDirection.Upstream));
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.Cds), 0, 1000, 20, DistanceDirection.Downstream));
		
		filter= new MrnaFilter("All Human mRNA - Aggregate");
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.FivePrime), 0, 10000, 10000, DistanceDirection.Upstream));
		
		filter= new MrnaFilter("Apoptosis");
		filter.addOntologyTerms(new String [] {"ubiquitin-protein ligase activity", "apoptotic process", "apoptotic signaling pathway", "induction of apoptosis", "execution phase of apoptosis", "negative regulation of apoptotic process", "positive regulation of apoptotic process"});
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.FivePrime), 0, 1000, 20, DistanceDirection.Upstream));
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.Cds), 0, 1000, 20, DistanceDirection.Downstream));
		
		filter= new MrnaFilter("Brain Development");
		filter.addOntologyTerms(new String [] {"brain segmentation", "brain morphogenesis", "central complex development", "forebrain development","hindbrain development","midbrain development"});
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.FivePrime), 0, 1000, 20, DistanceDirection.Upstream));
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.Cds), 0, 1000, 20, DistanceDirection.Downstream));
		
		filter= new MrnaFilter("Epigentics");
		filter.addOntologyTerms(new String [] {"DNA-methyltransferase", "methyl-CpG binding", "methyl-CpNpN binding", "DNA hypermethylation", "DNA hypomethylation"});
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.FivePrime), 0, 1000, 20, DistanceDirection.Upstream));
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.Cds), 0, 1000, 20, DistanceDirection.Downstream));
		
		filter= new MrnaFilter("Negative Regulation of Cell Proliferation");
		filter.addOntologyTerms(new String [] {"negative Regulation of Cell Proliferation"});
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.FivePrime), 0, 1000, 20, DistanceDirection.Upstream));
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.Cds), 0, 1000, 20, DistanceDirection.Downstream));
		
		filter= new MrnaFilter("Oncogenes");
		filter.addGeneNameSearchTerms(new String [] {"oncogene"});
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.FivePrime), 0, 1000, 20, DistanceDirection.Upstream));
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.Cds), 0, 1000, 20, DistanceDirection.Downstream));
		
		filter= new MrnaFilter("Positive Regulation of Cell Proliferation");
		filter.addOntologyTerms(new String [] {"positive regulation of cell proliferation"});
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.FivePrime), 0, 1000, 20, DistanceDirection.Upstream));
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.Cds), 0, 1000, 20, DistanceDirection.Downstream));
		
		filter= new MrnaFilter("Regulation of Cell Cycle");
		filter.addOntologyTerms(new String [] {"regulation of cell cycle"});
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.FivePrime), 0, 1000, 20, DistanceDirection.Upstream));
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.Cds), 0, 1000, 20, DistanceDirection.Downstream));
		
		filter= new MrnaFilter("Regulation of Cell Cycle");
		filter.addOntologyTerms(new String [] {"transcription factor complex", "transcription factor binding"});
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.FivePrime), 0, 1000, 20, DistanceDirection.Upstream));
		as.add(new FivePrimeAnalysis(filter, new G4Filter(90, Region.Cds), 0, 1000, 20, DistanceDirection.Downstream));
		
		
		
		
		
		/* Counting analysis (for paper) */
		Range [] tetradRanges = { new Range(2, Integer.MAX_VALUE), new Range(2, 2), new Range(3, 3), new Range(4, Integer.MAX_VALUE)};
		Region [] regions = { Region.Any, Region.FivePrime, Region.Cds, Region.ThreePrime};
		String [] organisms = { 
				"Pan troglodytes", 
				"Canis lupus familiaris", 
				"Mus musculus", 
				"Danio rerio", 
				"Drosophila melanogaster", 
				"Caenorhabditis elegans", 
				"Kluyveromyces lactis NRRL Y-1140"};
		CountingAnalysis ca = new CountingAnalysis();
		for ( Range r : tetradRanges) {
			for ( Region region : regions) {
				for ( String organism : organisms ) {
					ca.addCountingSet(makeCountingSet(organism, r, 95, region));
				}
			}
		}
		as.add(ca);
		
		/* Count for 5' UTR*/
		G4Filter g4;
		G4Filter conserved;
		MrnaFilter mrna;
		CountingAnalysis fivePrimeCounts = new CountingAnalysis("5 Prime Counts");
		make5PrimeCountingSet(fivePrimeCounts, "All Mrna - 5Prime", null, null);
		make5PrimeCountingSet(fivePrimeCounts, "Apoptosis",new String [] {"ubiquitin-protein ligase activity", "apoptotic process", "apoptotic signaling pathway", "induction of apoptosis", "execution phase of apoptosis", "negative regulation of apoptotic process", "positive regulation of apoptotic process"}, null);
		make5PrimeCountingSet(fivePrimeCounts, "Brain Development", new String [] {"brain segmentation", "brain morphogenesis", "central complex development", "forebrain development","hindbrain development","midbrain development"}, null);
		make5PrimeCountingSet(fivePrimeCounts, "Epigentics", new String [] {"DNA-methyltransferase", "methyl-CpG binding", "methyl-CpNpN binding", "DNA hypermethylation", "DNA hypomethylation"}, null);
		make5PrimeCountingSet(fivePrimeCounts, "Negative Regulation of Cell Proliferation", new String [] {"negative Regulation of Cell Proliferation"}, null);
		make5PrimeCountingSet(fivePrimeCounts, "Oncogenes", null, new String [] {"oncogene"});
		make5PrimeCountingSet(fivePrimeCounts, "Positive Regulation of Cell Proliferation", new String [] {"positive regulation of cell proliferation"}, null);
		make5PrimeCountingSet(fivePrimeCounts, "Regulation of Cell Cycle", new String [] {"regulation of cell cycle"}, null);
		make5PrimeCountingSet(fivePrimeCounts, "Regulation of Cell Cycle", new String [] {"transcription factor complex", "transcription factor binding"}, null);
		as.add(fivePrimeCounts);
		/*  Counting analysis - gscore conservation */
		CountingAnalysis scoreDistribution = new CountingAnalysis("GScore conservation");
		Collection<Range> scores = new LinkedList<Range>();
		String species = "Mus musculus";
		int bin = 1;
		for ( int i = 13; i < 103; i+=bin) {
			scores.add(new Range(i, i));
		}
		
		for ( Range t : tetradRanges) {
			for ( Range r : scores ){
				g4 = new G4Filter(/*Region.NotCds*/).setScore2Range(r).setScore3Range(r).setTetradRange(t);
				conserved = new G4Filter(95/*, Region.NotCds*/).setScore2Range(r).setScore3Range(r).setTetradRange(t);
				conserved.getHomologList().add(species);
				mrna = new MrnaFilter();
				//filter.addHomolog(species);
				scoreDistribution.addCountingSet(new CountingSet(mrna, g4, conserved));
			}
		}
		as.add(scoreDistribution);
		
		// Run them all.
		Driver driver = new Driver(as);
		driver.run(principals);
	}
	private static void make5PrimeCountingSet(CountingAnalysis fivePrimeCounts, String name, String [] functions, String [] searchTerms) {
		G4Filter g4 = new G4Filter(Region.FivePrime);
		G4Filter conserved = new G4Filter(90, Region.FivePrime);
		MrnaFilter mrna = new MrnaFilter(name);
		if ( functions != null ) {
			mrna.addOntologyTerms(functions);
		}
		if ( searchTerms != null) {
			mrna.addGeneNameSearchTerms(searchTerms);
		}
		fivePrimeCounts.addCountingSet(new CountingSet(mrna, g4, conserved));
		
	}
	
	static CountingSet makeCountingSet(String species, Range tetradRange,   int conservation) {
		MrnaFilter filter = new MrnaFilter();
		filter.addHomolog(species);
		G4Filter g4 = new G4Filter(0);
		g4.setTetradMin(tetradRange.getStart());
		g4.setTetradMax(tetradRange.getEnd());
		G4Filter conserved = new G4Filter(conservation);
		conserved.setTetradMin(tetradRange.getStart());
		conserved.setTetradMax(tetradRange.getEnd());
		conserved.getHomologList().add(species);
		return new CountingSet(filter, g4, conserved);
	}
	
	static CountingSet makeCountingSet(String species, Range tetradRange,  int conservation, Region r) {
		CountingSet s = makeCountingSet(species, tetradRange, conservation);
		s.g4Filter.setRegion(r);
		s.conserved.setRegion(r);
		return s;
	}
}
	