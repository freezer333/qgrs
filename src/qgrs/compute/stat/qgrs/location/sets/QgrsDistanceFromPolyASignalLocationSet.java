package qgrs.compute.stat.qgrs.location.sets;

import qgrs.compute.stat.qgrs.location.DistanceFromPolyASignal;

public class QgrsDistanceFromPolyASignalLocationSet extends QgrsLocationSet {
	public QgrsDistanceFromPolyASignalLocationSet() {
		super();
		
		for ( int i = -100; i < 100; i += 20 ) {
			this.add(new DistanceFromPolyASignal(i/20, i, i+20));
		}
	}
}
