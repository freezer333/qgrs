package qgrs.data.query;

import qgrs.model.DbCriteria;

public interface PageableQuery {

	public abstract String toCountSql();

	public abstract String toResultSetSql();

	public abstract void setParameters(DbCriteria dbCriteria);
	public abstract void setPagingParameters(int pageLimit, int computedOffset);
}