package qgrs.data.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import qgrs.controllers.PageHelper;
import qgrs.db.DatabaseConnection;
import qgrs.model.DbCriteria;

public abstract class PagedQueryExecution {

	protected int count;
	protected ResultSet resultSet;
	
	public void execute(DatabaseConnection connection, PageableQuery query, DbCriteria dbCriteria) throws SQLException {
		query.setParameters(dbCriteria);
		PreparedStatement stmt = connection.getConnection().prepareStatement(query.toCountSql());
		ResultSet rs = stmt.executeQuery();
		if ( rs.next() ) {
			count = rs.getInt(1);
		}
		else {
			count = 0;
		}
		System.out.println(query.toCountSql());
		System.out.println("Count Result = " + count);
		PageHelper pager = new PageHelper(dbCriteria, count);
		query.setPagingParameters(dbCriteria.getPageLimit(), pager.getComputedOffset());
		String sql = query.toResultSetSql();
		
		stmt = connection.getConnection().prepareStatement(sql);
		resultSet = stmt.executeQuery();
		
		System.out.println(sql);
	}

	public int getCount() {
		return count;
	}

	
	
	
	
	
}
