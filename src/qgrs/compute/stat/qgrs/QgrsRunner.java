package qgrs.compute.stat.qgrs;

import java.sql.Connection;
import java.sql.PreparedStatement;

import qgrs.compute.stat.Analysis;
import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.PartitionAnalyzer;
import qgrs.compute.stat.StatusReporter;
import qgrs.compute.stat.qgrs.location.sets.QgrsLocationSet;
import qgrs.compute.stat.qgrs.series.QgrsCriteriaSeries;
import qgrs.compute.stat.qgrs.series.QgrsSeriesSet;

public abstract class QgrsRunner extends Analysis {

	
	public QgrsRunner(boolean active) {
		super(active);
	}
	
	@Override 
	protected void prepareCustomTables(Connection c) {
		createSeriesTable(c);
		createResultsTable(c);
	}
	
	
	
	@Override
	protected void cleanCustomTablesById(Connection c) {
		String sql_results = "delete from results where analysisId = ?";
		String sql_series = "delete from series where analysisId = ?";
		try {
			PreparedStatement ps = c.prepareStatement(sql_results);
			ps.setString(1, this.getId());
			executeUpdateAndClose(ps);
			
			ps = c.prepareStatement(sql_series);
			ps.setString(1, this.getId());
			executeUpdateAndClose(ps);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public PreparedStatement buildResultsStatement(Connection conn) throws Exception{
		String iSql = 	"INSERT INTO results (analysisId, partitionId, " +
						"seriesId, resultId, label, " +
						"total, mean, " +
						"median, std, numSamples, numSamplesWithQgrs, " +
						"n_mean, n_median, n_std) " + 
						"VALUES (?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return conn.prepareStatement(iSql);
	}

	
	void createSeriesTable(Connection c) {
		String sql = "create table if not exists series (" + 
				 "analysisId varchar(255) not null, " +
				 "seriesId int, " +
				 "description varchar(MAX), " +
				 "primary key (analysisId, seriesId), " +
				 "foreign key (analysisId) references analysis(id) on delete cascade)";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			executeUpdateAndClose(ps);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void createResultsTable(Connection c) {
		String sql = "create table if not exists results (" + 
				 "analysisId varchar(255) not null, " +
				 "partitionId varchar(255) not null, " +
				 "seriesId varchar(255) not null, " +
				 "resultId int not null, " +
				 "label varchar(255) not null, " +
				 "total int not null, " +
				 "mean double not null, " +
				 "median double not null, " +
				 "std double not null, " +
				 "numSamples int not null, " +
				 "numSamplesWithQgrs int not null, " +
				 "n_mean double not null, " +
				 "n_median double not null, " +
				 "n_std double not null, " +
				 "primary key (analysisId, partitionId, seriesId, resultId), " +
				 "foreign key (analysisId) references analysis(id) on delete cascade)";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			executeUpdateAndClose(ps);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	protected abstract QgrsLocationSet buildQgrsLocationSet();

	

	@Override
	protected PartitionAnalyzer createProcessor(GenePartition partition, StatusReporter reporter) {
		QgrsSeriesSet seriesSet = this.buildSeriesSet();
		for ( QgrsCriteriaSeries series : seriesSet ) {
			series.setLocations(this.buildQgrsLocationSet());
		}
		QgrsAnalyzer analyzer = new QgrsAnalyzer(partition, seriesSet, reporter);
		return analyzer;
	}
	
}
