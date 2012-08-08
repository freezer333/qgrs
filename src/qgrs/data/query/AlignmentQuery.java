package qgrs.data.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import framework.web.util.StringUtils;

public class AlignmentQuery extends WhereClause {

	private final float score;
	private final GeneQuery principleGeneQuery;
	private final GeneQuery comparisionGeneQuery;
	
	
	public float getScore() {
		return score;
	}

	public GeneQuery getPrincipleGeneQuery() {
		return principleGeneQuery;
	}

	public GeneQuery getComparisionGeneQuery() {
		return comparisionGeneQuery;
	}

	@Override
	public String toSql() {
		String pgq = this.principleGeneQuery == null ? "" : this.principleGeneQuery.toSql();
		String cgq = this.comparisionGeneQuery == null ? "" : this.comparisionGeneQuery.toSql();
		LinkedList<String> criteria = new LinkedList<String>();
		
		if ( StringUtils.isDefined(pgq) ) {
			criteria.add(" principle IN ( SELECT accessionNumber FROM GENE " + pgq + ") ");
		}
		if ( StringUtils.isDefined(cgq) ) {
			criteria.add(" comparison IN ( SELECT accessionNumber FROM GENE " + cgq + ") ");
		}
		if ( score > 0 ) criteria.add(" similarityPercentage >= " + p(score/100));
		return where(criteria);
	}

	@Override
	public void set(PreparedStatement ps) throws SQLException {
		// TODO Auto-generated method stub

	}

	
	public AlignmentQuery(float score, GeneQuery principleGeneQuery,
			GeneQuery comparisionGeneQuery) {
		super();
		this.score = score;
		this.principleGeneQuery = principleGeneQuery;
		this.comparisionGeneQuery = comparisionGeneQuery;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((comparisionGeneQuery == null) ? 0 : comparisionGeneQuery
						.hashCode());
		result = prime
				* result
				+ ((principleGeneQuery == null) ? 0 : principleGeneQuery
						.hashCode());
		result = prime * result + Float.floatToIntBits(score);
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
		AlignmentQuery other = (AlignmentQuery) obj;
		if (comparisionGeneQuery == null) {
			if (other.comparisionGeneQuery != null)
				return false;
		} else if (!comparisionGeneQuery.equals(other.comparisionGeneQuery))
			return false;
		if (principleGeneQuery == null) {
			if (other.principleGeneQuery != null)
				return false;
		} else if (!principleGeneQuery.equals(other.principleGeneQuery))
			return false;
		if (Float.floatToIntBits(score) != Float.floatToIntBits(other.score))
			return false;
		return true;
	}

	
}
