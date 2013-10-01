package qgrs.data.analysis;

import qgrs.data.mongo.primitives.jongo.G4;
import qgrs.data.mongo.primitives.jongo.G4H;
import qgrs.data.mongo.primitives.jongo.MRNA;

public class G4Filter {

	final double min_h = 95;
	
	public boolean acceptable(MRNA mrna, G4 g4) {
		if ( g4.isIn3Prime() ) {
			for ( G4H h : g4.getConservedG4s() ) {
				if ( h.getOverallAbsoluteScore() >= min_h) {
					return true;
				}
			}
			return false;
		}
		return false;
	}
}
