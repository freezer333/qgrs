package qgrs.data.query.gene;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import qgrs.data.query.PageableQuery;
import qgrs.data.query.WhereClause;
import qgrs.model.DbCriteria;

public class GeneQuery extends WhereClause implements PageableQuery {

private final String selectClauseCount = "SELECT COUNT(DISTINCT QGRS.ID) ";
	
	// Note, the # is used to tranform the select's correlated subquery.
	private final String selectClauseResults = 
			"select QGRS.ID, GENE.GENESYMBOL, GENE.SPECIES, QGRS.SEQUENCESLICE, " +
			"QGRS.IN5PRIME, QGRS.INCDS, QGRS.IN3PRIME, QGRS.TETRAD1, QGRS.TETRAD2, QGRS.TETRAD3, QGRS.TETRAD4, " +
			"QGRS.NUMTETRADS, QGRS.GSCORE, " +
			"(SELECT COUNT(DISTINCT GQ2ID) FROM QGRS_H WHERE GQ1ID=QGRS.ID # GROUP BY GQ1ID) as HCOUNT";
	
	private final String fromClause = " from QGRS JOIN GENE ON GENE.AccessionNumber = QGRS.geneID";
	private final String orderClause = " ORDER BY QGRS.ID ";
	
	@Override
	public String toCountSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toResultSetSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParameters(DbCriteria dbCriteria) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPagingParameters(int pageLimit, int computedOffset) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set(PreparedStatement ps) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
