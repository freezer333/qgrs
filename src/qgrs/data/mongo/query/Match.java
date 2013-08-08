package qgrs.data.mongo.query;


public class Match {

	public String key;
	public Object value;
	
	
	public Match(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	@Override 
	public String toString() {
		return "{\""+key+"\":\""+value.toString()+"\"}";
	}
	
	
	
	
}
