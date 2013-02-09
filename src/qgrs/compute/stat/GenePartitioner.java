package qgrs.compute.stat;

import java.sql.Connection;
import java.util.HashSet;

public abstract class GenePartitioner {

	protected final Analysis runner;
	
	public GenePartitioner(Analysis runner){
		this.runner = runner;
	}
	
	public abstract HashSet<GenePartition> partition(Connection c);
}
