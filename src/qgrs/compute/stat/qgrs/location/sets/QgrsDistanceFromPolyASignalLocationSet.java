package qgrs.compute.stat.qgrs.location.sets;

import qgrs.compute.stat.qgrs.location.DistanceFromPolyASignal;

public class QgrsDistanceFromPolyASignalLocationSet extends QgrsLocationSet {
	public QgrsDistanceFromPolyASignalLocationSet() {
		super();
		
		for ( int i = -200; i < 0; i += 20 ) {
			this.add(new DistanceFromPolyASignal(i/20, i, i+20));
		}
		
		for ( int i = 0; i < 200; i += 20) {
			this.add(new DistanceFromPolyASignal(i/20, i, i+20));
		}
	}
}
