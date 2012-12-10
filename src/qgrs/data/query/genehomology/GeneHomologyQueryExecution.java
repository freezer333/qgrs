package qgrs.data.query.genehomology;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import qgrs.data.query.PageableQuery;
import qgrs.data.query.PagedQueryExecution;
import qgrs.db.DatabaseConnection;
import qgrs.model.DbCriteria;

public class GeneHomologyQueryExecution extends PagedQueryExecution {
	Collection<GeneHomologyQueryResult> results = new LinkedList<GeneHomologyQueryResult>();
	
	public Collection<GeneHomologyQueryResult> getResults() {
		return results;
	}

	@Override
	public void execute(DatabaseConnection connection, PageableQuery query, DbCriteria dbCriteria) throws SQLException {
		super.execute(connection, query, dbCriteria);
		
		if ( count == 0 ) return;
		
		while ( this.resultSet.next() ) {
			results.add(new GeneHomologyQueryResult(this.resultSet));
		}
		
		System.out.println("Result Set has " + results.size() + " records (paged)");
	}
}
