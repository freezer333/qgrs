package qgrs.db.query.gene;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import qgrs.db.DatabaseConnection;
import qgrs.db.query.PageableQuery;
import qgrs.db.query.PagedQueryExecution;
import qgrs.model.DbCriteria;

public class GeneQueryExecution extends PagedQueryExecution {
	Collection<GeneQueryResult> results = new LinkedList<GeneQueryResult>();
	
	public Collection<GeneQueryResult> getResults() {
		return results;
	}

	@Override
	public void execute(DatabaseConnection connection, PageableQuery query, DbCriteria dbCriteria) throws SQLException {
		super.execute(connection, query, dbCriteria);
		
		if ( count == 0 ) return;
		
		while ( this.resultSet.next() ) {
			results.add(new GeneQueryResult(this.resultSet));
		}
		
		System.out.println("Result Set has " + results.size() + " records (paged)");
	}
}
