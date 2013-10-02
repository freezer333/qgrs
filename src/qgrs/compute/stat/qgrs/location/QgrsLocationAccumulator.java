package qgrs.compute.stat.qgrs.location;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import qgrs.compute.stat.qgrs.QgrsLocationResults;
import qgrs.data.GeneSequence;
import qgrs.data.records.GQuadruplexRecord;

public abstract class QgrsLocationAccumulator implements Comparable {

	public final QgrsLocationResults results = new QgrsLocationResults();
	public final QgrsLocationResults normalizedResults = new QgrsLocationResults();
	
	private int numApplicableGenes = 0;
	private int numGenesWithQgrs = 0;
	
	private int count;
	private int order;
	final private String label;
	
	public QgrsLocationAccumulator(int order, String label) {
		this.order = order;
		this.label = label;
		this.count = 0;
	}
	
	
	public void fillStatement(PreparedStatement ps, String analysisId,
			String partitionId, int seriesId)
			throws SQLException {
		
		ps.setString(1, analysisId);
		ps.setString(2, partitionId);
		ps.setInt(3, seriesId);
		ps.setInt(4, getOrder());
		ps.setString(5, getLabel());
		ps.setInt(6, (int)results.getSum());
		
		ps.setDouble(7,  results.getMean());
		ps.setDouble(8,  results.getMedian());
		ps.setDouble(9,  results.getStandardDeviation());
		ps.setDouble(10,  results.getSkewness());
		
		ps.setInt(11,  getNumApplicableGenes());
		ps.setInt(12,  getNumGenesWithQgrs());
		
		ps.setDouble(13,  normalizedResults.getMean());
		ps.setDouble(14,  normalizedResults.getMedian());
		ps.setDouble(15,  normalizedResults.getStandardDeviation());
		ps.setDouble(16,  normalizedResults.getSkewness());
	}

	

	public int getNumApplicableGenes() {
		return numApplicableGenes;
	}


	public void setNumApplicableGenes(int numApplicableGenes) {
		this.numApplicableGenes = numApplicableGenes;
	}


	public int getNumGenesWithQgrs() {
		return numGenesWithQgrs;
	}


	public void setNumGenesWithQgrs(int numGenesWithQgrs) {
		this.numGenesWithQgrs = numGenesWithQgrs;
	}


	@Override
	public int compareTo(Object o) {
		QgrsLocationAccumulator other = (QgrsLocationAccumulator) o;
		return new Integer(this.order).compareTo(new Integer(other.order));
	}





	public String getLabel() {
		return label;
	}


	public void setOrder(int order) {
		this.order = order;
	}


	public int getOrder() {
		return order;
	}


	public void startGene() {
		this.count = 0;
	}
	
	public void finishGene(GeneSequence seq) {
		results.addValue(count);
		double nt100Units = this.numXNucleotidesInLocation(100.0, seq);
		normalizedResults.addValue(count / nt100Units);
		this.numApplicableGenes++;
		if ( count > 0 ) {
			this.numGenesWithQgrs++;
		}
	}
	
	public void offer(GQuadruplexRecord qgrs, GeneSequence sequence) {
	if ( isWithin(qgrs, sequence)) {
			count++;
		}
	/*	//below used only for Camille's Multiple PolyA Program
		count += isWithin(qgrs, sequence); */
	} 
	
	//REGULAR
	public abstract boolean isWithin(GQuadruplexRecord qgrs, GeneSequence sequence);
/*	//Below for Camille's code only
	public abstract int isWithin(GQuadruplexRecord qgrs, GeneSequence sequence); */
	
	public abstract boolean isApplicable(GeneSequence sequence);
	public abstract double numXNucleotidesInLocation(double x, GeneSequence sequence);
	
}
