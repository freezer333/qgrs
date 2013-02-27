package qgrs.compute.stat.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdom.Element;

public class LocationRecord implements Comparable {

	public final int id;
	public final String label;
	
	public LocationRecord(ResultSet rs) throws SQLException {
		this.id = rs.getInt("resultId");
		this.label = rs.getString("label");
	}

	@Override
	public int compareTo(Object o) {
		return new Integer(id).compareTo(new Integer(((LocationRecord)o).id));
	}
	
	public Element getXmlElement() {
		Element loc = new Element("location");
		loc.addContent(new Element("id").setText(String.valueOf(id)));
		loc.addContent(new Element("label").setText(label));
		return loc;
	}
	
	
}
