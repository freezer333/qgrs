package qgrs.data.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import qgrs.data.GQuadruplex;

import framework.web.util.StringUtils;

public class HomologyQuery extends WhereClause {

	private double overlapScore;
	private double tetradScore;
	private double avgLoopScore;
	private double totalLengthScore;
	private double overallScore;
	private AlignmentQuery alignmentQuery;
	private QgrsQuery principleQgrsQuery;
	private QgrsQuery comparisonQgrsQuery;
	
	private final boolean FLATTEN_QUERY = true;
	
	
	public HomologyQuery setOverallScore(double value) {
		this.overallScore = value;
		return this;
	}
	
	public AlignmentQuery getAlignmentQuery() {
		return alignmentQuery;
	}

	public void setAlignmentQuery(AlignmentQuery alignmentQuery) {
		this.alignmentQuery = alignmentQuery;
	}

	public QgrsQuery getPrincipleQgrsQuery() {
		return principleQgrsQuery;
	}

	public void setPrincipleQgrsQuery(QgrsQuery principleQgrsQuery) {
		this.principleQgrsQuery = principleQgrsQuery;
	}

	public QgrsQuery getComparisonQgrsQuery() {
		return comparisonQgrsQuery;
	}

	public void setComparisonQgrsQuery(QgrsQuery comparisonQgrsQuery) {
		this.comparisonQgrsQuery = comparisonQgrsQuery;
	}
	
	

	public HomologyQuery(double overlapScore, double tetradScore,
			double avgLoopScore, double totalLengthScore, double overallScore) {
		super();
		this.overlapScore = overlapScore;
		this.tetradScore = tetradScore;
		this.avgLoopScore = avgLoopScore;
		this.totalLengthScore = totalLengthScore;
		this.overallScore = overallScore;
	}
	
	public HomologyQuery() {
		super();
		this.overlapScore = 0;
		this.tetradScore = 0;
		this.avgLoopScore = 0;
		this.totalLengthScore = 0;
		this.overallScore = 0;
	}
	
	private String getRegionCriteria(QgrsQuery gqQ, String prefix) {
		if ( !gqQ.isIn5Prime() && !gqQ.isInCds() && !gqQ.isIn3Prime() ) {
			return "FALSE";
		}
		if (gqQ.isIn5Prime() && gqQ.isInCds() && gqQ.isIn3Prime() ) {
			return "";
		}
		else {
			int count = 0;
			String retval = "(";
			if ( gqQ.isIn5Prime() ) {
				retval += (prefix + "_in5UTR = TRUE");
				count++;
			}
			if ( gqQ.isInCds() ) {
				if ( count > 0 ) retval += " OR ";
				retval += (prefix + "_inCDS = TRUE");
				count++;
			}
			if ( gqQ.isIn3Prime() ) {
				if ( count > 0 ) retval += " OR ";
				retval += (prefix + "_inCDS = TRUE");
			}
			retval += ")";
			return retval;
		}
	}
	private void addQgrsCriteria(Collection<String> criteria) {
		if ( this.getPrincipleQgrsQuery() != null ) {
			if ( StringUtils.isDefined(this.getPrincipleQgrsQuery().getId() )) {
				criteria.add (" gq1Id = " + p(this.getPrincipleQgrsQuery().getId()));
			}
			if ( this.getPrincipleQgrsQuery().getMinTetrad() > GQuadruplex.MINIMUM_TETRAD ) {
				criteria.add(" p_tetrads > " + p(this.getPrincipleQgrsQuery().getMinTetrad()));
			}
			if ( this.getPrincipleQgrsQuery().getMinScore() > GQuadruplex.MINIMUM_SCORE ) {
				criteria.add(" p_gScore > " + p(this.getPrincipleQgrsQuery().getMinScore()));
			}
			criteria.add(this.getRegionCriteria(this.getPrincipleQgrsQuery(), "p"));
		}
		if ( this.getComparisonQgrsQuery() != null ) {
			if ( StringUtils.isDefined(this.getComparisonQgrsQuery().getId() )) {
				criteria.add (" gq2Id = " + p(this.getComparisonQgrsQuery().getId()));
			}
			if ( this.getComparisonQgrsQuery().getMinTetrad() > GQuadruplex.MINIMUM_TETRAD ) {
				criteria.add(" c_tetrads > " + p(this.getComparisonQgrsQuery().getMinTetrad()));
			}
			if ( this.getComparisonQgrsQuery().getMinScore() > GQuadruplex.MINIMUM_SCORE ) {
				criteria.add(" c_gScore > " + p(this.getComparisonQgrsQuery().getMinScore()));
			}
			criteria.add(this.getRegionCriteria(this.getComparisonQgrsQuery(), "c"));
		}
	}
	private void addAlignmentCriteria(Collection<String> criteria) {
		if ( this.alignmentQuery == null )  return;
		if ( this.alignmentQuery.getScore() > 0 ) criteria.add(" alignmentScore >= " + p(this.alignmentQuery.getScore()/100));
		if ( this.alignmentQuery.getPrincipleGeneQuery() != null ) {
			if ( StringUtils.isDefined(this.alignmentQuery.getPrincipleGeneQuery().getId())) {
				criteria.add( " p_accessionNumber = " + p(this.alignmentQuery.getPrincipleGeneQuery().getId()));
			}
			if ( StringUtils.isDefined(this.alignmentQuery.getPrincipleGeneQuery().getSymbol())) {
				criteria.add( " p_geneSymbol LIKE '%" + this.alignmentQuery.getPrincipleGeneQuery().getSymbol() + "%' ");
			}
			if ( StringUtils.isDefined(this.alignmentQuery.getPrincipleGeneQuery().getSpeciesList())){
				criteria.add( " p_species " + in ( Arrays.asList(this.alignmentQuery.getPrincipleGeneQuery().getSpeciesList().split(";"))));
			}
		}
		if ( this.alignmentQuery.getComparisionGeneQuery() != null ) {
			if ( StringUtils.isDefined(this.alignmentQuery.getComparisionGeneQuery().getId())) {
				criteria.add( " c_accessionNumber = " + p(this.alignmentQuery.getComparisionGeneQuery().getId()));
			}
			if ( StringUtils.isDefined(this.alignmentQuery.getComparisionGeneQuery().getSymbol())) {
				criteria.add( " c_geneSymbol LIKE '%" + this.alignmentQuery.getComparisionGeneQuery().getSymbol() + "%' ");
			}
			if ( StringUtils.isDefined(this.alignmentQuery.getComparisionGeneQuery().getSpeciesList())){
				criteria.add( " c_species " + in ( Arrays.asList(this.alignmentQuery.getComparisionGeneQuery().getSpeciesList().split(";"))));
			}
		}
	}
	@Override
	public String toSql() {
		Collection<String> criteria = new LinkedList<String>();
		if ( overallScore > 0 ) criteria.add("overallScore >= " + p(overallScore));
		
		// If query is flattened, we use the attributes on the QGRS-H record instead other tables
		/* From alignment Query, we can search for:
				Alignment Score (alignmentScore)
				Gene Query (P and C)
				
		   From Gene Query can search for
		   		Species ( list ) (p_species)
		   		Symbol (p_geneSymbol)
		   		ID (p_accessionNumber)
		   		
		   From Principle/Comparison Qgrs Query we can search for:
		   		p_in5UTR, p_inCDS, p_in3UTR
		   		gq1Id
		   		p_tetrads
		   		p_gScore
		   
		*/
		if ( FLATTEN_QUERY  ) {
			addAlignmentCriteria(criteria);
			this.addQgrsCriteria(criteria);
		}
		else {
			String alignment = this.alignmentQuery == null ? "" : this.alignmentQuery.toSql();
			if ( StringUtils.isDefined(alignment) ) {
				criteria.add ("alignmentId IN (SELECT id FROM GENE_A " + alignment + ")");
			}
			
			String principle = this.principleQgrsQuery == null ? "" : this.principleQgrsQuery.toSql();
			if ( StringUtils.isDefined(alignment) ) {
				criteria.add ("gq1Id IN (SELECT id FROM QGRS " + principle + ")");
			}
			String comparison = this.comparisonQgrsQuery == null ? "" : this.comparisonQgrsQuery.toSql();
			if ( StringUtils.isDefined(alignment) ) {
				criteria.add ("gq2Id IN (SELECT id FROM QGRS " + comparison + ")");
			}
		}
		return where(criteria);
	}


	

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((alignmentQuery == null) ? 0 : alignmentQuery.hashCode());
		long temp;
		temp = Double.doubleToLongBits(avgLoopScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((comparisonQgrsQuery == null) ? 0 : comparisonQgrsQuery
						.hashCode());
		temp = Double.doubleToLongBits(overallScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(overlapScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((principleQgrsQuery == null) ? 0 : principleQgrsQuery
						.hashCode());
		temp = Double.doubleToLongBits(tetradScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalLengthScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		HomologyQuery other = (HomologyQuery) obj;
		if (alignmentQuery == null) {
			if (other.alignmentQuery != null)
				return false;
		} else if (!alignmentQuery.equals(other.alignmentQuery))
			return false;
		if (Double.doubleToLongBits(avgLoopScore) != Double
				.doubleToLongBits(other.avgLoopScore))
			return false;
		if (comparisonQgrsQuery == null) {
			if (other.comparisonQgrsQuery != null)
				return false;
		} else if (!comparisonQgrsQuery.equals(other.comparisonQgrsQuery))
			return false;
		if (Double.doubleToLongBits(overallScore) != Double
				.doubleToLongBits(other.overallScore))
			return false;
		if (Double.doubleToLongBits(overlapScore) != Double
				.doubleToLongBits(other.overlapScore))
			return false;
		if (principleQgrsQuery == null) {
			if (other.principleQgrsQuery != null)
				return false;
		} else if (!principleQgrsQuery.equals(other.principleQgrsQuery))
			return false;
		if (Double.doubleToLongBits(tetradScore) != Double
				.doubleToLongBits(other.tetradScore))
			return false;
		if (Double.doubleToLongBits(totalLengthScore) != Double
				.doubleToLongBits(other.totalLengthScore))
			return false;
		return true;
	}

	@Override
	public void set(PreparedStatement ps) throws SQLException {
		// TODO Auto-generated method stub

	}

}
