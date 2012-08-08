package qgrs.data.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import qgrs.data.GQuadruplex;
import framework.web.util.StringUtils;

public class QgrsQuery extends WhereClause {

	private final String id;
	private final int minTetrad;
	private int minScore;
	private final boolean in5Prime;
	private final boolean inCds;
	private final boolean in3Prime;
	private GeneQuery geneClause;
	
	private String id() {
		if ( StringUtils.isDefined(id)) {
			return "id = " + p(id);
		}
		else return "";
	}
	private String tetrad() {
		return "numTetrads >= " + p(minTetrad);
	}
	private String gScore() {
		return "gScore >= " + p(minScore);
	}
	private String gene() {
		if ( this.geneClause == null ) return "";
		String gw = this.geneClause.toSql();
		if ( StringUtils.isDefined(gw)) {
			return " geneId IN ( SELECT accessionNumber FROM GENE " + gw + ")";
		}
		return "";
	}
	
	public String getId() {
		return id;
	}
	public int getMinTetrad() {
		return minTetrad;
	}
	public int getMinScore() {
		return minScore;
	}
	public boolean isIn5Prime() {
		return in5Prime;
	}
	public boolean isInCds() {
		return inCds;
	}
	public boolean isIn3Prime() {
		return in3Prime;
	}
	private String region() {
		if ( !in5Prime && !inCds && !in3Prime ) {
			return "FALSE";
		}
		if (in5Prime && inCds && in3Prime ) {
			return "";
		}
		else {
			int count = 0;
			String retval = "(";
			if ( in5Prime ) {
				retval += ("in5Prime = TRUE");
				count++;
			}
			if ( inCds ) {
				if ( count > 0 ) retval += " OR ";
				retval += ("inCds = TRUE");
				count++;
			}
			if ( in3Prime ) {
				if ( count > 0 ) retval += " OR ";
				retval += ("in3Prime = TRUE");
			}
			retval += ")";
			return retval;
		}
	}
	
	
	public void setGeneClause(GeneQuery geneClause) {
		this.geneClause = geneClause;
	}
	@Override
	public String toSql() {
		LinkedList<String> criteria = new LinkedList<String>();
		if ( minTetrad > GQuadruplex.MINIMUM_TETRAD ) criteria.add(this.tetrad());
		if ( minScore > GQuadruplex.MINIMUM_SCORE ) criteria.add(this.gScore());
		criteria.add(this.region());
		criteria.add(this.id());
		criteria.add(this.gene());
		return where(criteria);
	}

	@Override
	public void set(PreparedStatement ps) throws SQLException {
		/*if ( StringUtils.isDefined(id())) {
			ps.setString(1, this.id);
		}*/
	}
	
	
	public QgrsQuery(String id, int minTetrad, int minScore, boolean in5Prime,
			boolean inCds, boolean in3Prime) {
		super();
		this.id = id;
		this.minTetrad = minTetrad;
		this.minScore = minScore;
		this.in5Prime = in5Prime;
		this.inCds = inCds;
		this.in3Prime = in3Prime;
		this.geneClause = null;
	}
	public QgrsQuery() {
		super();
		this.id = "";
		this.minTetrad = 0;
		this.minScore = 0;
		this.in5Prime = true;
		this.inCds = true;
		this.in3Prime = true;
		this.geneClause = null;
	}
	
	public QgrsQuery setMinScore(int value) {
		this.minScore  = value;
		return this;
			
	}
	
	

	public QgrsQuery(String id, int minTetrad, int minScore, boolean in5Prime,
			boolean inCds, boolean in3Prime, GeneQuery geneClause) {
		super();
		this.id = id;
		this.minTetrad = minTetrad;
		this.minScore = minScore;
		this.in5Prime = in5Prime;
		this.inCds = inCds;
		this.in3Prime = in3Prime;
		this.geneClause = geneClause;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((geneClause == null) ? 0 : geneClause.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (in3Prime ? 1231 : 1237);
		result = prime * result + (in5Prime ? 1231 : 1237);
		result = prime * result + (inCds ? 1231 : 1237);
		result = prime * result + minScore;
		result = prime * result + minTetrad;
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
		QgrsQuery other = (QgrsQuery) obj;
		if (geneClause == null) {
			if (other.geneClause != null)
				return false;
		} else if (!geneClause.equals(other.geneClause))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (in3Prime != other.in3Prime)
			return false;
		if (in5Prime != other.in5Prime)
			return false;
		if (inCds != other.inCds)
			return false;
		if (minScore != other.minScore)
			return false;
		if (minTetrad != other.minTetrad)
			return false;
		return true;
	}
	


	

}
