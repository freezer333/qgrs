package qgrs.compute.stat.qgrs.series;

import qgrs.compute.stat.qgrs.location.QgrsLocationAccumulator;
import qgrs.compute.stat.qgrs.location.sets.QgrsLocationSet;
import qgrs.data.records.QgrsHomologyProfile;

public abstract class QgrsCriteriaSeries {

	final int order;
	final String description;
	
	QgrsLocationSet locations;
	
	
	
	public abstract boolean accept(QgrsHomologyProfile record);
	public abstract boolean acceptQuadruplex(int tetrads, int gScore);

	

	public QgrsCriteriaSeries(int order, String description) {
		super();
		this.order = order;
		this.description = description;
	}



	public QgrsLocationSet getLocations() {
		return locations;
	}



	public void setLocations(QgrsLocationSet locations) {
		this.locations = locations;
	}



	public int getOrder() {
		return order;
	}



	public String getDescription() {
		return description;
	}
	
	
}
