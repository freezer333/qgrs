package qgrs.data.mongo.primitives.jongo;

import qgrs.data.Range;

public class DistanceFromPolyA {
	int distance;
	Range polyASignal;
	
	
	public DistanceFromPolyA() {}
	
	public DistanceFromPolyA(G4 g4, Range polyA) {
		this.polyASignal = polyA;
		
		int start = g4.getTetrad1();
		int end = g4.getTetrad4() + g4.getNumTetrads();
		
		if ( end <= polyA.getStart() ) {
			//up stream
			this.distance = end - polyA.getStart();
		}
		else if ( start >= polyA.getEnd()) {
			// downstream
			this.distance = start - polyA.getEnd();
		}
		else {
			// collides with polyA
			this.distance = 0;
		}
		
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Range getPolyASignal() {
		return polyASignal;
	}

	public void setPolyASignal(Range polyASignal) {
		this.polyASignal = polyASignal;
	}
	
	
	
	
	
}
