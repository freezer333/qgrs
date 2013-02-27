package qgrs.compute.stat.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdom.Element;

public class PartitionRecord {

	public final String partitionId;
	public final String analysisId;
	public final String description;
	public final int numSamples;
	
	public PartitionRecord(ResultSet rs) throws SQLException {
		partitionId = rs.getString("partitionId");
		analysisId = rs.getString("analysisId");
		description = rs.getString("description");
		numSamples = rs.getInt("numSamples");
	}
	
	
	public Element getXmlElement() {
		Element part = new Element("partition");
		part.addContent(new Element("partitionId").setText(this.partitionId));
		part.addContent(new Element("description").setText(this.description));
		part.addContent(new Element("numSamples").setText(String.valueOf(this.numSamples)));
		return part;
	}
}
