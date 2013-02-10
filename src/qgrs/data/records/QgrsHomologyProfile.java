package qgrs.data.records;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import qgrs.compute.stat.qgrs.series.QgrsHomologyCriteria;

public class QgrsHomologyProfile {

	public final GQuadruplexRecord principle;
	private final Collection<QgrsHomologyRecord> homologs;
	
	
	public QgrsHomologyProfile(GQuadruplexRecord principle) {
		super();
		this.principle = principle;
		homologs = new LinkedList<QgrsHomologyRecord>();
	}


	public Collection<QgrsHomologyRecord> getHomologs() {
		return homologs;
	}
	
	public Collection<String> getHomologousSpecies(QgrsHomologyCriteria homologyCriteria) {
		HashSet<String> species = new HashSet<String>();
		for (QgrsHomologyRecord r : this.homologs ) {
			if ( homologyCriteria.accept(r)) {
				species.add(r.getC_species());
			}
		}
		return species;
	}
	
	public int getNumHomologsSpecies(QgrsHomologyCriteria homologyCriteria) {
		return this.getHomologousSpecies(homologyCriteria).size();
	}
	
	
}
