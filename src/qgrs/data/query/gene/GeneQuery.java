package qgrs.data.query.gene;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import qgrs.data.GQuadruplex;
import qgrs.data.query.PageableQuery;
import qgrs.data.query.QueryUtils;
import qgrs.data.query.WhereClause;
import qgrs.input.QParam;
import qgrs.model.DbCriteria;

public class GeneQuery extends WhereClause implements PageableQuery {


	
/*
select gene.accessionnumber , gene.species, gene.genesymbol, count (distinct qgrs_h.gq1id) as qgrsHCount, 

(select COUNT(distinct QGRS.ID) from qgrs where geneId = accessionNumber) as qgrsCount , 
count ( distinct qgrs_h.c_accessionNumber) as geneHCount


FROM gene left join qgrs_h on gene.accessionnumber = qgrs_h.p_accessionNumber
group by gene.accessionnumber
*/

	private final String selectClauseCount = 
			"select count(gene.accessionnumber) ";

	// Note, the # is used to tranform the select's correlated subquery.
	private final String selectClauseResults = 
			"select gene.accessionnumber , gene.species, gene.genesymbol, count (distinct CONSERVED.gq1id) as qgrsHCount," +
			"(select COUNT(distinct QGRS.ID) from qgrs where geneId = accessionNumber #) as qgrsCount ,  " +
			"count ( distinct CONSERVED.c_accessionNumber) as geneHCount ";
	
	// Note, the # is used to transform the subquery's constraints
	private final String fromClause = " FROM gene left join " +
									  " (select * from QGRS_H #) as CONSERVED" +
									  " on gene.accessionnumber =CONSERVED.p_accessionNumber ";
	
	private final String fromCountClause = " FROM gene ";
	
	private final String groupClause = " GROUP BY gene.accessionnumber ";
	private final String orderClause = " ORDER BY GENE.GENESYMBOL ";
	
	
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
		return "P_TETRADS >= " + p(qgrsMinTetrads);
	}
	private String gScore() {
		return "P_GSCORE >= " + p(qgrsMinGScore);
	}
	
	private String tetradQgrs() {
		return "QGRS.NUMTETRADS >= " + p(qgrsMinTetrads);
	}
	private String gScoreQgrs() {
		return "QGRS.GSCORE >= " + p(qgrsMinGScore);
	}
	
	
	// selects qgrs constraints on QGRS
	// continued where.
	private String buildQgrsWhereClause() {
		LinkedList<String> criteria = new LinkedList<String>();
		if ( qgrsMinTetrads > GQuadruplex.MINIMUM_TETRAD ) criteria.add(this.tetradQgrs());
		if ( qgrsMinGScore > GQuadruplex.MINIMUM_SCORE ) criteria.add(this.gScoreQgrs());
		criteria.add(QueryUtils.buildRegionConstraint(this.in5Prime, this.inCds, this.in3Prime, QueryUtils.qgrs_regions_cols));
		
		return continuedWhere(criteria);
	}
	
	// selects qgrs, qgrsH, and c_gene constrainst on QGRSH
	private String buildQgrsHWhereClause() {
		LinkedList<String> criteria = new LinkedList<String>();
		if ( qgrsMinTetrads > GQuadruplex.MINIMUM_TETRAD ) criteria.add(this.tetrad());
		if ( qgrsMinGScore > GQuadruplex.MINIMUM_SCORE ) criteria.add(this.gScore());
		criteria.add(QueryUtils.buildRegionConstraint(in5Prime, inCds, in3Prime, QueryUtils.qgrs_h_regions_cols));
		criteria.add(this.stringConstraint("GQ1ID", this.qgrsId));
		criteria.add(this.stringConstraint("P_AccessionNumber ", this.principleGeneId));
		criteria.add(this.stringConstraint("P_GENESYMBOL", this.principleGeneSymbol));
		criteria.add(this.stringConstraint("P_SPECIES", this.principleGeneSpecies));
		criteria.add(this.stringConstraint("C_SPECIES", this.comparsionGeneSpecies));
		
		if ( this.minimumGeneAlignmentPercentage > 0.001 ) {
			criteria.add(this.alignment());
		}
		if ( this.qgrsMinHomologyScore > 0.3 ) {
			criteria.add(this.qgrsHomology());
		}
		return  where(criteria);
	}
	
	// selects on gene
	private String buildWhereClause() {
		LinkedList<String> criteria = new LinkedList<String>();
		criteria.add(this.stringConstraint("GENE.ACCESSIONNUMBER ", this.principleGeneId));
		criteria.add(this.stringConstraint("GENE.GENESYMBOL", this.principleGeneSymbol));
		criteria.add(this.stringConstraint("GENE.SPECIES", this.principleGeneSpecies));
		return  where(criteria);
	}
	
	
	@Override
	public String toCountSql() {
		return this.selectClauseCount + this.fromCountClause + this.buildWhereClause();
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
	public String toSql() {
		String select = this.selectClauseResults.replace("#", this.buildQgrsWhereClause());
		String from = fromClause.replace("#", this.buildQgrsHWhereClause());
		String q = 	select + 
				from + 
				this.buildWhereClause() + this.groupClause + this.orderClause ;
		return q;
	}

	@Override
	public void set(PreparedStatement ps) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
