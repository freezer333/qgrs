package qgrs.compute.stat.qgrs.location.sets;

import qgrs.compute.stat.qgrs.location.DistanceFromCds;

public class QgrsDistanceFromCdsLocationSet extends QgrsLocationSet {
	public QgrsDistanceFromCdsLocationSet() {
		super();
		
		for ( int i = 0; i < 1000; i += 20 ) {
			this.add(new DistanceFromCds(i/20, i));
		}
	}
}
