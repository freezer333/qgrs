package qgrs.compute.stat.qgrs;

import qgrs.data.records.QgrsHomologyProfile;

public interface QgrsCriteria {

	
	boolean accept(QgrsHomologyProfile record);
	
	
}
