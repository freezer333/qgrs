package qgrs.controllers;

import qgrs.input.QParam;
import qgrs.model.DbCriteria;

public class PageHelper {

	private final int computedLimit;
	
	public PageHelper(DbCriteria dbCriteria, int count) {
		int totalPages = (count % dbCriteria.getPageLimit()) == 0 ? count / dbCriteria.getPageLimit(): count / dbCriteria.getPageLimit()+1;
	    dbCriteria.put(QParam.Db_TotalResults, String.valueOf(count));
	    int requestedPage = Integer.parseInt(dbCriteria.get(QParam.Db_PageNumber));
	    if ( requestedPage > totalPages ) {
	    	requestedPage = totalPages;
	    }
	    computedLimit = (requestedPage - 1) * dbCriteria.getPageLimit();
	    if ( requestedPage == 0 ) {
	    	requestedPage = 1;
	    }
	    if( totalPages == 0 ) {
	    	totalPages = 1;
	    }
	    dbCriteria.put(QParam.Db_TotalPages, String.valueOf(totalPages));
	    dbCriteria.put(QParam.Db_PageNumber, String.valueOf(requestedPage));
	}

	public int getComputedOffset() {
		return computedLimit;
	}
	
	
}
