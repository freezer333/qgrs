package qgrs.db.query.genehomology;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import framework.web.util.StringUtils;

import qgrs.data.GQuadruplex;
import qgrs.db.query.PageableQuery;
import qgrs.db.query.QueryUtils;
import qgrs.db.query.WhereClause;
import qgrs.input.QParam;
import qgrs.model.DbCriteria;

public class GeneHomologyQuery extends WhereClause 
implements PageableQuery {
	private String principleGeneId;
	private String principleGeneSymbol;
	private String principleGeneSpecies;
	private String comparsionGeneSpecies;
	private float minimumGeneAlignmentPercentage;
	
	private String qgrsId;
	private int qgrsMinGScore;
	private int qgrsMinTetrads;
	private float qgrsMinHomologyScore;
	private boolean in5Prime;
	private boolean inCds;
	private boolean in3Prime;
	
	private int minNumConserved;
	
	private int pageLimit;
	private int pageOffset;
	
	
	/*
	 * 	SELECT PRINCIPLE, COMPARISON , PSPECIES , CSPECIES , PSYMBOL, CSYMBOL , SIMILARITYPERCENTAGE, 
		(SELECT COUNT(DISTINCT QGRS.ID) FROM QGRS WHERE GENEID=PRINCIPLE) as P_QgrsCount, 
		(SELECT COUNT(DISTINCT QGRS_H.ID) FROM QGRS_H WHERE P_ACCESSIONNUMBER=PRINCIPLE) as P_QgrsHCount
		FROM GENE_AQ
		WHERE  (SELECT COUNT(DISTINCT QGRS_H.ID) FROM QGRS_H WHERE P_ACCESSIONNUMBER=PRINCIPLE)= 2
	 */
	private final String selectClauseResults = 
			"SELECT ID, PRINCIPLE, COMPARISON, PSPECIES, " +
			"CSPECIES, PSYMBOL, CSYMBOL as C_GENESYMBOL , SIMILARITYPERCENTAGE, "+
			"(SELECT COUNT(DISTINCT QGRS.ID) FROM QGRS WHERE GENEID=PRINCIPLE #) as P_QgrsCount,  " +
			"(SELECT COUNT(DISTINCT QGRS_H.ID) FROM QGRS_H WHERE P_ACCESSIONNUMBER=PRINCIPLE @) as HCOUNT  ";

	private final String fromClause = " FROM GENE_AQ ";
	private final String orderClause = " ORDER BY PSYMBOL ";
	
	
	
	private String alignment() {
		return "SIMILARITYPERCENTAGE >= " + p(minimumGeneAlignmentPercentage/100);
	}
	private String qgrsHomology() {
		return "OVERALLSCORE >= " + p(qgrsMinHomologyScore);
	}
	
	private String tetrad() {
		return "P_TETRADS >= " + p(qgrsMinTetrads) + " AND C_TETRADS >= " + p(qgrsMinTetrads);
	}
	private String gScore() {
		return "P_GSCORE >= " + p(qgrsMinGScore)+ " AND C_GSCORE >= " + p(qgrsMinGScore);
	}
	
	private String tetradQgrs() {
		return "QGRS.NUMTETRADS >= " + p(qgrsMinTetrads);
	}
	private String gScoreQgrs() {
		return "QGRS.GSCORE >= " + p(qgrsMinGScore);
	}
	
	
	private String buildWhereClause() {
		LinkedList<String> criteria = new LinkedList<String>();
		
		criteria.add(this.stringConstraint("PRINCIPLE ", this.principleGeneId));
		criteria.add(this.stringConstraint("PSYMBOL", this.principleGeneSymbol));
		criteria.add(this.stringConstraint("PSPECIES", this.principleGeneSpecies));
		criteria.add(this.stringConstraint("CSPECIES", this.comparsionGeneSpecies));
		
		if ( this.minimumGeneAlignmentPercentage > 0.001 ) {
			criteria.add(this.alignment());
		}
		String where =  where(criteria);
		
		if ( this.minNumConserved > 0 ) {
			criteria = buildQgrsHCriteria();
			String qgrsHWhere = "(SELECT COUNT(DISTINCT QGRS_H.ID) FROM QGRS_H WHERE P_ACCESSIONNUMBER=PRINCIPLE @) >= '" + this.minNumConserved + "'";
			qgrsHWhere = qgrsHWhere.replace("@", continuedWhere(criteria));
			if ( StringUtils.isDefined(where)) {
				where += (" AND " +  qgrsHWhere);
			}
			else {
				where = "WHERE " + qgrsHWhere;
			}
		}
		return where;
	}
	
	
	
	
	@Override
	public String toCountSql() {
		return "SELECT COUNT(*) FROM (" + this.toSql() + ")";
	}

	@Override
	public String toResultSetSql() {
		return toSql() +  
				" LIMIT " + this.pageLimit + " OFFSET " + this.pageOffset;
	}

	@Override
	public void setParameters(DbCriteria dbCriteria) {
		this.qgrsId = dbCriteria.get(QParam.Db_QgrsId1);
		this.qgrsMinTetrads = Integer.parseInt( dbCriteria.get(QParam.Db_MinTetrads1));
		this.qgrsMinGScore = Integer.parseInt(dbCriteria.get(QParam.Db_GScore1));
		this.in5Prime = dbCriteria.readBoolean(dbCriteria.get(QParam.Db_Region15UTR));
		this.inCds = dbCriteria.readBoolean(dbCriteria.get(QParam.Db_Region1CDS));
		this.in3Prime = dbCriteria.readBoolean(dbCriteria.get(QParam.Db_Region13UTR));
		this.minNumConserved = Integer.parseInt(dbCriteria.get(QParam.Db_MinNumConserved));
		
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
	
	
	
	private String transformSelect() {
		// create the QGRS where
		LinkedList<String> criteria = new LinkedList<String>();
		if ( qgrsMinTetrads > GQuadruplex.MINIMUM_TETRAD ) criteria.add(this.tetradQgrs());
		if ( qgrsMinGScore > GQuadruplex.MINIMUM_SCORE ) criteria.add(this.gScoreQgrs());
		criteria.add(QueryUtils.buildRegionConstraint(this.in5Prime, this.inCds, this.in3Prime, QueryUtils.qgrs_regions_cols));
		
		String w = continuedWhere(criteria);
		String retval =  this.selectClauseResults.replace("#", w);
		
		// create the QGRS_H where
		criteria = buildQgrsHCriteria();
		return retval.replace("@", continuedWhere(criteria));
		
		
	}
	private LinkedList<String> buildQgrsHCriteria() {
		LinkedList<String> criteria;
		criteria = new LinkedList<String>();
		if ( qgrsMinTetrads > GQuadruplex.MINIMUM_TETRAD ) criteria.add(this.tetrad());
		if ( qgrsMinGScore > GQuadruplex.MINIMUM_SCORE ) criteria.add(this.gScore());
		criteria.add(QueryUtils.buildRegionConstraint(in5Prime, inCds, in3Prime, QueryUtils.qgrs_h_regions_cols));
		criteria.add(this.stringConstraint("GQ1ID", this.qgrsId));
		criteria.add(this.stringConstraint("C_SPECIES", this.comparsionGeneSpecies));
		if ( this.qgrsMinHomologyScore > 0.3 ) {
			criteria.add(this.qgrsHomology());
		}
		return criteria;
	}
	@Override
	public String toSql() {
		String q = 	transformSelect() + 
				fromClause + 
				this.buildWhereClause() + this.orderClause ;
		return q;
	}
	@Override
	public void set(PreparedStatement ps) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
