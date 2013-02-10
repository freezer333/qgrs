package qgrs.compute.stat.qgrs.location.sets;

import qgrs.compute.stat.qgrs.location.Region3Prime;
import qgrs.compute.stat.qgrs.location.Region5Prime;
import qgrs.compute.stat.qgrs.location.RegionCds;

public class QgrsRegionLocationSet extends QgrsLocationSet {
	public QgrsRegionLocationSet() {
		super();
		this.add(new Region5Prime(0));
		this.add(new RegionCds(1));
		this.add(new Region3Prime(2));
	}
}
