package qgrs.data.mongo.primitives;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Range extends BasicDBObject {

	public Range() {
		
	}
	public Range(qgrs.data.Range r) {
		this.setStart(r.getStart());
		this.setEnd(r.getEnd());
	}
	
	public Range(DBObject obj) {
		this.setStart(Integer.parseInt((String) obj.get("start")));
		this.setEnd(Integer.parseInt((String) obj.get("end")));
	}
	public Range(int start, int end) {
		this.setStart(start);
		this.setEnd(end);
	}
	
	public qgrs.data.Range asCoreRange() {
		return new qgrs.data.Range(this.getStart(), this.getEnd());
	}
	
	public int getStart() {
		return this.getInt("start");
	}
	public void setStart(int start) {
		this.put("start", start);
	}
	public int getEnd() {
		return this.getInt("end");
	}
	public void setEnd(int end) {
		this.put("end", end);
	}
}

