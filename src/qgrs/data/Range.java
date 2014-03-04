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
	public Range(int position)
	{
		this.start=position;
		this.end=position;
	}
	
	public int getLength() {
		return this.getEnd() - this.getStart() + 1;
	}
	
	public int getStart() {
		return start;
	}
	public int getEnd() {
		return end;
	}
	
	public boolean contains(int value) {
		return start <= value && end >= value;
	}
	public boolean overlapsWith(Range other) {
		return ( 
				other.getStart() >= start && other.getStart()  <= end 
				|| 
				other.getEnd() >= start && other.getEnd() <= end ) ;
	}
	
	@Override
	public String toString() {
		if ( start == Integer.MIN_VALUE && end < Integer.MAX_VALUE) {
			return "<" + end;
		}
		if ( start > Integer.MIN_VALUE && end == Integer.MAX_VALUE ) {
			return start + "+";
		}
		if ( start == Integer.MIN_VALUE && end == Integer.MAX_VALUE ) {
			return "";
		}
		if ( start == end ) {
			return "=" + start;
		}
		return "[" + start + " - " + end + "]";
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + end;
		result = prime * result + start;
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
		Range other = (Range) obj;
		if (end != other.end)
			return false;
		if (start != other.start)
			return false;
		return true;
	}
	

}
