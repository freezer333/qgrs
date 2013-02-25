package qgrs.compute.stat.qgrs.user;

import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.qgrs.QgrsRunner;
import qgrs.compute.stat.qgrs.location.sets.QgrsAnyLocationSet;
import qgrs.compute.stat.qgrs.location.sets.QgrsLocationSet;
import qgrs.compute.stat.qgrs.location.sets.QgrsRegionLocationSet;
import qgrs.compute.stat.qgrs.series.DefaultQgrsSeriesSet;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;

public class OntologyQgrsRunner extends QgrsRunner {

	public OntologyQgrsRunner() {
		super(false);
	}
	
	@Override
	public String getDescription() {
		return "Ontology Runner";
	}
	
	@Override
	public String getId() {
		return "Ontology";
	}
	
	@Override
	protected QgrsSeriesSet buildSeriesSet() {
		return new DefaultQgrsSeriesSet();
	}

	@Override
	protected GenePartitioner buildPartitioner() {
		return new OntologyGenePartitioner(this);
	}
	
	@Override
	protected QgrsLocationSet buildQgrsLocationSet() {
		return new QgrsAnyLocationSet().join(new QgrsRegionLocationSet());
	}
	
	
	public static void main(String [] args) throws Exception {
		System.out.println("Running ontology qgrs engine");
		OntologyQgrsRunner e = new OntologyQgrsRunner();
		e.execute();
		System.out.println("Analysis is complete");
		System.exit(0);
	}

	

	

	

}
