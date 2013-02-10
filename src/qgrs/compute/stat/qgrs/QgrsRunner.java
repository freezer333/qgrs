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

	final QgrsSeriesSet seriesSet;
	
	public QgrsRunner(boolean active) {
		super(active);
		this.seriesSet = this.buildSeriesSet();
		for ( QgrsCriteriaSeries series : this.seriesSet ) {
			series.setLocations(this.buildQgrsLocationSet());
		}
		
	}
	
	
	// NEED TO FIGURE OUT WHERE TO CALL THIS.  Create series table, and then insert the series. 
	
	// IMPORTANT:  The insertion would happen with each partition
	
	// NOTE:  This can turn into an interactive system:
		// After defining a set of series, and a set of location sets, a UI can be built
		// to allow users to pick and choose.  Perhaps even create new ones through configuration.
	
		// 	Partitioning can be done online by uploading a set of accession numbers for each parition, or choosing
		// 	from an existing set.
	
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
						"seriesId, id, label, " +
						"total, mean, " +
						"median) " + 
						"VALUES (?, ?, ?, ?, ? , ?, ?, ?)";
		return conn.prepareStatement(iSql);
	}

	
	void createSeriesTable(Connection c) {
		String sql = "create table if not exists series (" + 
				 "analysisId varchar(255) not null, " +
				 "partitionId varchar(255) not null, " +
				 "id int, " +
				 "description varchar(MAX), " +
				 "primary key (analysisId, partitionId, id), " +
				 "foreign key (partitionId) references partition(partitionId), " +
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
				 "id int not null, " +
				 "label varchar(255) not null, " +
				 "total int not null, " +
				 "mean double not null, " +
				 "median double not null, " +
				 "primary key (analysisId, partitionId, seriesId, id), " +
				 "foreign key (seriesId) references series(id), " +
				 "foreign key (partitionId) references partition(partitionId), " +
				 "foreign key (analysisId) references analysis(id) on delete cascade)";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			executeUpdateAndClose(ps);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract QgrsSeriesSet buildSeriesSet();
	protected abstract QgrsLocationSet buildQgrsLocationSet();

	

	@Override
	protected PartitionAnalyzer createProcessor(GenePartition partition, StatusReporter reporter) {
		QgrsAnalyzer analyzer = new QgrsAnalyzer(partition, seriesSet, this.buildQgrsLocationSet(), reporter);
		return analyzer;
	}
	
}
