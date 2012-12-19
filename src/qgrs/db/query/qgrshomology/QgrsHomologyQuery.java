package qgrs.db.query.qgrshomology;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import qgrs.data.GQuadruplex;
import qgrs.db.query.PageableQuery;
import qgrs.db.query.QueryUtils;
import qgrs.db.query.WhereClause;
import qgrs.input.QParam;
import qgrs.model.DbCriteria;

public class QgrsHomologyQuery extends WhereClause implements PageableQuery {

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
	
	private int pageLimit;
	private int pageOffset;
	
	private final String selectionForResultSet = 
			"SELECT GQ1ID, GQ2ID, P_GENESYMBOL, C_GENESYMBOL, P_SPECIES, C_SPECIES, " +
			"QGRS1.SEQUENCESLICE as qgrs1seq , QGRS2.SEQUENCESLICE AS qgrs2Seq, " +
			"P_TETRADS , C_TETRADS, P_GSCORE , C_GSCORE , " +
			"p_in5UTR, p_inCDS, p_in3UTR, c_in5UTR, c_inCDS, c_in3UTR, " +
			"QGRS1.TETRAD1 as qgrs1Position , QGRS2.TETRAD1 as qgrs2Position, " +
			"QGRS1.TETRAD1 as qgrs1Tetrad1, QGRS1.TETRAD2 as qgrs1Tetrad2, " + 
			"QGRS1.TETRAD3 as qgrs1Tetrad3, QGRS1.TETRAD4 as qgrs1Tetrad4, " +
			"QGRS2.TETRAD1 as qgrs2Tetrad1, QGRS2.TETRAD2 as qgrs2Tetrad2, " + 
			"QGRS2.TETRAD3 as qgrs2Tetrad3, QGRS2.TETRAD4 as qgrs2Tetrad4, " +
			"OVERALLSCORE ";
	

	
	
	private final String selectionForCount = "SELECT COUNT(QGRS_H.ID) ";
	private final String fromClause = "FROM QGRS_H JOIN QGRS AS QGRS1 ON QGRS1.ID = GQ1ID JOIN QGRS AS QGRS2 ON QGRS2.ID = GQ2ID "; 
	private final String orderClause = " ORDER BY QGRS_H.ID ";
	
	private String alignment() {
		return "ALIGNMENTSCORE >= " + p(minimumGeneAlignmentPercentage/100);
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
	
	
	
	
	
				
	private String buildWhereClause() {
		LinkedList<String> criteria = new LinkedList<String>();
		if ( qgrsMinTetrads > GQuadruplex.MINIMUM_TETRAD ) criteria.add(this.tetrad());
		if ( qgrsMinGScore > GQuadruplex.MINIMUM_SCORE ) criteria.add(this.gScore());
		criteria.add(QueryUtils.buildRegionConstraint(in5Prime, inCds, in3Prime, QueryUtils.qgrs_h_regions_cols));
		criteria.add(this.stringConstraint("GQ1ID", this.qgrsId));
		criteria.add(this.stringConstraint("P_ACCESSIONNUMBER ", this.principleGeneId));
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
	
	
	@Override
	public String toCountSql() {
		return this.selectionForCount + fromClause + this.buildWhereClause() ;
	}
	
	@Override
	public String toResultSetSql() {
		return toSql() ;
	}
	
	
	
	
	public String toSql() {
		String q = 	selectionForResultSet + 
					fromClause + 
					this.buildWhereClause() + this.orderClause + 
					" LIMIT " + this.pageLimit + " OFFSET " + this.pageOffset;
		return q;
	}

	@Override
	public void set(PreparedStatement ps) throws SQLException {
		// TODO Auto-generated method stub
		
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
	
	
	
	
	
}
