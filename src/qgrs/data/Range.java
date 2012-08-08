package qgrs.data;

import java.sql.ResultSet;

import org.jdom.Element;


public class Range {
	private final int start;
	private final int end;
	
	public Range()
	{
		start=-1;
		end=-1;
	}
	public Range(int start, int end)
	{
		this.start=start;
		this.end=end;
	}
	
	public int getStart() {
		return start;
	}
	public int getEnd() {
		return end;
	}
	
	
	@Override
	public String toString() {
		return start + " - " + end;
	}
	
	public Element writeElement(Element element) {
		element.setAttribute("start", String.valueOf(this.start));
		element.setAttribute("end", String.valueOf(this.end));
		return element;
	}
	
	public static Range buildFromElement(Element element) {
		return new Range(Integer.parseInt(element.getAttributeValue("start")), 
				Integer.parseInt(element.getAttributeValue("end")));
	}
	
	public static Range buildFromResultSet(String prefix, ResultSet rs) throws Exception {
		return new Range(rs.getInt(prefix+"Start"), rs.getInt(prefix+"End"));
	}
	

}
