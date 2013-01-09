package qgrs.compute.stat.qgrs;

import qgrs.data.records.GQuadruplexRecord;

public interface QgrsCriteria {

	
	boolean accept(GQuadruplexRecord record);
	
	
}
