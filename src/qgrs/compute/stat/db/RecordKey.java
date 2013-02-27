package qgrs.compute.stat.db;

import org.jdom.Element;

public class RecordKey {
	
	final String partitionId;
	final int seriesId;
	final int locationId;
	
	public RecordKey(PartitionRecord p, SeriesRecord s, LocationRecord loc) {
		this.partitionId = p.partitionId;
		this.seriesId = s.seriesId;
		this.locationId = loc.id;
	}
	
	public void writeElement(Element e) {
		e.setAttribute("partitionId", partitionId);
		e.setAttribute("seriesId", String.valueOf(seriesId));
		e.setAttribute("locationId", String.valueOf(locationId));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + locationId;
		result = prime * result
				+ ((partitionId == null) ? 0 : partitionId.hashCode());
		result = prime * result + seriesId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordKey other = (RecordKey) obj;
		if (locationId != other.locationId)
			return false;
		if (partitionId == null) {
			if (other.partitionId != null)
				return false;
		} else if (!partitionId.equals(other.partitionId))
			return false;
		if (seriesId != other.seriesId)
			return false;
		return true;
	}
	
	
	
	
}
