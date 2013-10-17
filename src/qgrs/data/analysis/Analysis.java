package qgrs.data.analysis;

import java.io.File;

import qgrs.data.mongo.primitives.jongo.MRNA;

public abstract class Analysis {

	protected MrnaFilter mrnaFilter;
	protected String root = "anaylis_results";
	protected String group = "";
	
	public Analysis (String groupName, MrnaFilter mrnaFilter) {
		this.mrnaFilter = mrnaFilter;
		this.group = groupName;
	}
	
	
	public abstract void evaluate(MRNA mrna) ;
	public abstract void report() ;
	
	protected void mkdirs() {
		new File(root).mkdir();
		new File(this.root + File.separator + group).mkdir();
	}
	protected String dirPrefix() {
		return  this.root + File.separator + group + File.separator;
	}
}
