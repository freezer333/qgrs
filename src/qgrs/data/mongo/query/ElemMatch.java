package qgrs.data.mongo.query;

import java.util.ArrayList;
import java.util.Collection;

public class ElemMatch {

	
	public String arrayName;
	
	Collection<Object> matches = new ArrayList<Object>();
	
	public ElemMatch(String arrayName) {
		this.arrayName = arrayName;
	}
	
	//{ array: { $elemMatch: { value1: 1, value2: { $gt: 1 } } } }
	
	
}
