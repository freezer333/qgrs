package qgrs.db.query.qgrs;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import qgrs.data.GQuadruplex;
import qgrs.db.query.PageableQuery;
import qgrs.db.query.WhereClause;
import qgrs.input.QParam;
import qgrs.model.DbCriteria;

public class QgrsQuery  extends WhereClause implements PageableQuery {

	private final String selectClauseCount = "SELECT COUNT(DISTINCT QGRS.ID) ";
	
	// Note, the # is used to tranform the select's correlated subquery.
	private final String selectClauseResults = 
			"select QGRS.ID, GENE.GENESYMBOL, GENE.SPECIES, QGRS.SEQUENCESLICE, " +
			"QGRS.IN5PRIME, QGRS.INCDS, QGRS.IN3PRIME, QGRS.TETRAD1, QGRS.TETRAD2, QGRS.TETRAD3, QGRS.TETRAD4, " +
			"QGRS.NUMTETRADS, QGRS.GSCORE, " +
			"(SELECT COUNT(DISTINCT GQ2ID) FROM QGRS_H WHERE GQ1ID=QGRS.ID # GROUP BY GQ1ID) as HCOUNT";
	
	private final String fromClause = " from QGRS JOIN GENE ON GENE.AccessionNumber = QGRS.geneID";
	private final String orderClause = " ORDER BY QGRS.ID ";
	
	private String principleGeneId;
	private String principleGeneSymbol;
	private String principleGeneSpecies;
	private float minimumGeneAlignmentPercentage;
	private float qgrsMinHomologyScore;
	private String comparsionGeneSpecies;
	
	private String qgrsId;
	private int qgrsMinGScore;
	private int qgrsMinTetrads;
	private boolean in5Prime;
	private boolean inCds;
	private boolean in3Prime;
	
	private int pageLimit;
	private int pageOffset;
	
	private String alignment() {
		return "ALIGNMENTSCORE >= " + p(minimumGeneAlignmentPercentage/100);
	}
	private String qgrsHomology() {
		return "OVERALLSCORE >= " + p(qgrsMinHomologyScore);
	}
	private String tetrad() {
		return "NUMTETRADS >= " + p(qgrsMinTetrads);
	}
	private String gScore() {
		return "GSCORE >= " + p(qgrsMinGScore);
	}
	
	// returns the altered select withthe # expanded to include criterial for QGRS-H
	private String transformSelect() {
		LinkedList<String> criteria = new LinkedList<String>();
		criteria.add(this.stringConstraint("C_SPECIES", this.comparsionGeneSpecies));
		
		if ( this.minimumGeneAlignmentPercentage > 0.001 ) {
			criteria.add(this.alignment());
		}
		if ( this.qgrsMinHomologyScore > 0.3 ) {
			criteria.add(this.qgrsHomology());
		}
		String w = continuedWhere(criteria);
		return this.selectClauseResults.replace("#", w);
		
	}
	private String buildWhereClause() {
		LinkedList<String> criteria = new LinkedList<String>();
		if ( qgrsMinTetrads > GQuadruplex.MINIMUM_TETRAD ) criteria.add(this.tetrad());
		if ( qgrsMinGScore > GQuadruplex.MINIMUM_SCORE ) criteria.add(this.gScore());
		criteria.add(buildRegionConstraint());
		criteria.add(this.stringConstraint("QGRS.ID", this.qgrsId));
		criteria.add(this.stringConstraint("GENE.ACCESSIONNUMBER ", this.principleGeneId));
		criteria.add(this.stringConstraint("GENE.GENESYMBOL", this.principleGeneSymbol));
		criteria.add(this.stringConstraint("GENE.SPECIES", this.principleGeneSpecies));
		return  where(criteria);
	}
	
	@Override
	public String toCountSql() {
		return this.selectClauseCount + fromClause + this.buildWhereClause() ;
	}

	@Override
	public String toResultSetSql() {
		return toSql() ;
	}
	
	@Override
	public String toSql() {
		String q = 	this.transformSelect() +  
				fromClause + 
				this.buildWhereClause() + this.orderClause + 
				" LIMIT " + this.pageLimit + " OFFSET " + this.pageOffset;
		return q;
	}


	@Override
	public void setParameters(DbCriteria dbCriteria) {
		this.qgrsId = dbCriteria.get(QParam.Db_QgrsId1);
		this.qgrsMinTetrads = Integer.parseInt( dbCriteria.get(QParam.Db_MinTetrads1));
		this.qgrsMinGScore = Integer.parseInt(dbCriteria.get(QParam.Db_GScore1));
		this.in5Prime = dbCriteria.readBoolean(dbCriteria.get(QParam.Db_Region15UTR));
		this.inCds = dbCriteria.readBoolean(dbCriteria.get(QParam.Db_Region1CDS));
		this.in3Prime = dbCriteria.readBoolean(dbCriteria.get(QParam.Db_Region13UTR));
		
		
		this.minimumGeneAlignmentPercentage = Float.parseFloat(dbCriteria.get(QParam.Db_MinAlignmentScore));
		this.qgrsMinHomologyScore = Float.parseFloat(dbCriteria.get(QParam.Db_OverallSimilarity)) ;
		this.principleGeneId = dbCriteria.get(QParam.Db_GeneId1);
		this.principleGeneSpecies =  dbCriteria.get(QParam.Db_Species1);
		this.principleGeneSymbol = dbCriteria.get(QParam.Db_GeneSymbol1);
		this.comparsionGeneSpecies =  dbCriteria.get(QParam.Db_Species2);
	}

	@Override
	public void setPagingParameters(int pageLimit, int computedOffset) {
		this.pageLimit = pageLimit;
		this.pageOffset = computedOffset;
		
	}
	
	
	
	
	@Override
	public void set(PreparedStatement ps) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	
	public String buildRegionConstraint() {
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
				retval += ("IN5PRIME = TRUE");
				count++;
			}
			if ( inCds ) {
				if ( count > 0 ) retval += " OR ";
				retval += ("INCDS = TRUE");
				count++;
			}
			if ( in3Prime ) {
				if ( count > 0 ) retval += " OR ";
				retval += ("IN3PRIME = TRUE");
			}
			retval += ")";
			return retval;
		}
	}

}
