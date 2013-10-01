package qgrs.data.analysis;

import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.MRNA;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class PolyADistanceAnalysis {

	
	int min;
	int max;
	int increment;
	DistanceDirection direction;
	ArrayList<DistanceBin> bins;
	MrnaFilter mrnaFilter;
	
	public PolyADistanceAnalysis(MrnaFilter mrnaFilter, G4Filter g4Filter, int min, int max, int increment, DistanceDirection direction) {
		super();
		this.min = min;
		this.max = max;
		this.increment = increment;
		this.direction = direction;
		this.mrnaFilter = mrnaFilter;
		this.bins = new ArrayList<DistanceBin>();
		
		for ( int i = min; i < max; i+= increment) {
			bins.add(new DistanceBin(direction, i, i+increment, g4Filter));
		}
		
	}
	
	
	public void compute(MongoCollection principals) {
		Iterable<MRNA> all = principals.find().as(MRNA.class);
		for ( MRNA mrna : all ) {
			if ( this.mrnaFilter.acceptable(mrna)) {
				for ( DistanceBin bin : bins) {
					for ( Range poly : mrna.getPolyASignals())  {
						// won't count if the poly range is not supported by the mrna.
						bin.tally(mrna, poly);
					}
				}
			}
		}
	}
	
	public void report() {
		System.out.println("-----------------------------------");
		System.out.println("PolyA Signal analyis - " + this.direction.toString());
		System.out.println();
		System.out.println("nt. interval    % polyA signals with at least 1 qgrs\t# poly with 1 qgrs / #poly with valid interval");
		System.out.println("-----------------------------------");
		DecimalFormat f = new DecimalFormat("0.00%");
		for ( DistanceBin bin : bins) {
			System.out.println(String.format("%3d", bin.getMinBases()) + "-" + String.format("%3d", bin.getMaxBases()) + 
					":\t" + f.format(bin.getCount().measure()) + "\t\t\t(" + bin.getCount().getTotalWithG4() + "/" + bin.getCount().getTotal() + ")");
		}
		System.out.println("-----------------------------------");
	}
	
	
	public static void main(String [] args) throws UnknownHostException {
		DB db = new MongoClient().getDB("qgrs");

		Jongo jongo = new Jongo(db);
		MongoCollection principals = jongo.getCollection("principals");
		
		PolyADistanceAnalysis analysis = new PolyADistanceAnalysis(new MrnaFilter(), new G4Filter(), 0, 500, 20, DistanceDirection.Downstream);
		analysis.compute(principals);
		analysis.report();
		
		analysis = new PolyADistanceAnalysis(new MrnaFilter(), new G4Filter(), 0, 500, 20, DistanceDirection.Upstream);
		analysis.compute(principals);
		analysis.report();
	}
	
}
