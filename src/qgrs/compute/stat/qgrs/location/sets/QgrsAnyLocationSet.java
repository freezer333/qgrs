package qgrs.compute.stat.qgrs.location.sets;

import qgrs.compute.stat.qgrs.location.AnyLocation;

public class QgrsAnyLocationSet extends QgrsLocationSet {
	public QgrsAnyLocationSet() {
		super();
		this.add(new AnyLocation(0));
	}
}
