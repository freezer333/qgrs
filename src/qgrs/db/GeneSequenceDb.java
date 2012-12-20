package qgrs.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import qgrs.data.GeneSequence;
import qgrs.data.Range;
import framework.db.QueryConstraint;
import framework.db.QueryConstraints;
import framework.db.StatementBuilder;

public class GeneSequenceDb extends DbTable {

	private static String GENE_TABLE = "GENE";
	private static String POLY_A_SITE_TABLE = "POLY_A_SITE";
	private static String POLY_A_SIGNAL_TABLE = "POLY_A_SIGNAL";
	private static String ACCESSION_NUMBER_COL = "accessionNumber";
	
	DatabaseConnection dc;
	final PreparedStatement insertStatement;
	final PreparedStatement updateStatement;
	
	final PreparedStatement insertPolyASiteStatement;
	final PreparedStatement deletePolyASiteStatement;
	
	final PreparedStatement insertPolyASignalStatement;
	final PreparedStatement deletePolyASignalStatement;
	
	
	final PreparedStatement selectStatement;
	final PreparedStatement selectAllStatement;
	final PreparedStatement selectPolyASiteStatement;
	final PreparedStatement selectPolyASignalStatement;
	
	final GoDb goDb;
	
	public GeneSequenceDb(DatabaseConnection dc) {
		this.dc = dc;
		this.goDb = new GoDb(dc);
		this.insertStatement = createInsertStatement();
		this.insertPolyASiteStatement = createPolyASiteInsertStatement();
		this.insertPolyASignalStatement = createPolyASignalInsertStatement();
		this.updateStatement = createUpdateStatement();
		this.selectStatement = createSelectStatement();
		this.selectAllStatement = createSelectAllStatement();
		this.selectPolyASiteStatement = createPolyASiteSelectStatement();
		this.selectPolyASignalStatement = createPolyASignalSelectStatement();
		this.deletePolyASignalStatement = createPolyASignalDeleteStatement();
		this.deletePolyASiteStatement = createPolyASiteDeleteStatement();
	}
	
	
	@Override
	public int getCount() {
		return this.getCount(GENE_TABLE, dc.getConnection());
	}
	
	public void close() {
		try {
			this.insertStatement.close();
			this.insertPolyASiteStatement.close();
			this.insertPolyASignalStatement.close();
			this.updateStatement.close();
			this.selectStatement.close();
			this.selectPolyASiteStatement.close();
			this.selectPolyASignalStatement.close();
			this.deletePolyASignalStatement.close();
			this.deletePolyASiteStatement.close();
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	
	public void put(GeneSequence s) {
		PreparedStatement ps = null;
		boolean isUpdate = false;
		if (has(s.getAccessionNumber())) {
			ps = updateStatement;
			isUpdate = true;
		}
		else {
			ps = insertStatement;
		}
		
		fillPreparedStatement(ps, s, isUpdate);
		
		try {
			deletePolyASignalStatement.setString(1, s.getAccessionNumber());
			deletePolyASiteStatement.setString(1, s.getAccessionNumber());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		goDb.delete(s.getAccessionNumber());
		this.dc.executeUpdate(this.deletePolyASignalStatement);
		this.dc.executeUpdate(this.deletePolyASiteStatement);
		
		this.insertRanges(this.insertPolyASignalStatement, s.getAccessionNumber(), s.getPolyASignals());
		this.insertRanges(this.insertPolyASiteStatement, s.getAccessionNumber(), s.getPolyASites());
		this.dc.executeUpdate(ps);
		goDb.put(s.getAccessionNumber(), s.getOntologyData());
		
		
	}
	
	private void insertRanges(PreparedStatement ps, String accessionNumber, List<Range> ranges) {
		for ( Range r : ranges ) {
			fillPreparedStatement(ps, accessionNumber, r);
			this.dc.executeUpdate(ps);
		}
	}
	
	public GeneSequence get(String acsessionNumber) {
		try {
			this.selectStatement.setString(1, acsessionNumber);
			ResultSet rs = this.selectStatement.executeQuery();
			if ( rs.next()) {
				return GeneSequence.buildFromResultSet(rs, 
						this.getPolyASites(acsessionNumber), 
						this.getPolyASignals(acsessionNumber), 
						goDb.get(acsessionNumber));
			}
			else {
				return null;
			}
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	public List<GeneSequence> getAll() {
		try {
			List<GeneSequence> retval = new LinkedList<GeneSequence>();
			ResultSet rs = this.selectAllStatement.executeQuery();
			while ( rs.next()) {
				retval.add(GeneSequence.buildFromResultSet(rs, 
						this.getPolyASites(rs.getString(ACCESSION_NUMBER_COL)), 
						this.getPolyASignals(rs.getString(ACCESSION_NUMBER_COL)), 
						goDb.get(rs.getString(ACCESSION_NUMBER_COL)))
						);
			}
			return retval;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	
	
	
	
	
	public List<GeneSequence> getIn(Collection<String> accessionNumbers) {
		try {
			List<GeneSequence> retval = new LinkedList<GeneSequence>();
			if ( accessionNumbers == null || accessionNumbers.size()  < 1 ) {
				return retval;
			}
			String query = "SELECT * FROM GENE WHERE accessionNumber IN(" + getCSL(accessionNumbers)+ ") ";
			//System.out.println("QUERY DEBUG:  " + query);
			//long start = System.nanoTime();
			PreparedStatement ps = dc.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while ( rs.next()) {
				retval.add(GeneSequence.buildFromResultSet(rs, 
						this.getPolyASites(rs.getString(ACCESSION_NUMBER_COL)), 
						this.getPolyASignals(rs.getString(ACCESSION_NUMBER_COL)), 
						goDb.get(rs.getString(ACCESSION_NUMBER_COL)))
						);
			}
			//double  elapsed = System.nanoTime() - start;
			//System.out.println("QUERY TIME:  " + new DecimalFormat("0.000").format(elapsed /1000000000) + " sec");
			return retval;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	
	
	
	
	private List<Range> getPolyASites(String acsessionNumber) {
		return getPolys(this.selectPolyASiteStatement, acsessionNumber);
	}
	private List<Range> getPolyASignals(String acsessionNumber) {
		return getPolys(this.selectPolyASignalStatement, acsessionNumber);
	}
	
	private List<Range> getPolys(PreparedStatement ps, String acsessionNumber) {
		try {
			List<Range> retval = new LinkedList<Range>();
			ps.setString(1, acsessionNumber);
			ResultSet rs = ps.executeQuery();
			while ( rs.next()) {
				retval.add(new Range(rs.getInt("Start"), rs.getInt("End")));
			}
			return retval;
		} catch ( Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public boolean has(String acessesionId) {
		return get(acessesionId) != null;
	}
	
	
	private PreparedStatement createInsertStatement() {
		StatementBuilder builder = buildBuilder();
		return this.buildInsert(builder);
	}
	private PreparedStatement createPolyASiteInsertStatement() {
		StatementBuilder builder = buildPolyASiteBuilder();
		return this.buildInsert(builder);
	}
	private PreparedStatement createPolyASignalInsertStatement() {
		StatementBuilder builder = buildPolyASignalBuilder();
		return this.buildInsert(builder);
	}
	
	private PreparedStatement createUpdateStatement() {
		StatementBuilder builder = buildBuilder();
		return buildUpdate(builder);
	}
	
	
	private PreparedStatement buildUpdate(StatementBuilder builder) {
		QueryConstraints qc = new QueryConstraints();
		qc.add(new QueryConstraint(ACCESSION_NUMBER_COL, ""));
		try {
			return this.dc.getConnection().prepareStatement(builder.buildUpdateStatement(qc));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private PreparedStatement buildInsert(StatementBuilder builder) {
		try {
			return this.dc.getConnection().prepareStatement(builder.buildInsertStatement());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private PreparedStatement createSelectStatement() {
		StatementBuilder builder = new StatementBuilder(GENE_TABLE);
		return buildSelectStatement(builder);
	}
	
	private PreparedStatement createSelectAllStatement() {
		StatementBuilder builder = new StatementBuilder(GENE_TABLE);
		try {
			return dc.getConnection().prepareStatement(builder.buildSelectStatement(new QueryConstraints()));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	private PreparedStatement createPolyASiteSelectStatement() {
		StatementBuilder builder = new StatementBuilder(POLY_A_SITE_TABLE);
		return buildSelectStatement(builder);
	}
	private PreparedStatement createPolyASignalSelectStatement() {
		StatementBuilder builder = new StatementBuilder(POLY_A_SIGNAL_TABLE);
		return buildSelectStatement(builder);
	}
	
	private PreparedStatement createPolyASiteDeleteStatement() {
		StatementBuilder builder = new StatementBuilder(POLY_A_SITE_TABLE);
		return buildDeleteStatement(builder);
	}
	private PreparedStatement createPolyASignalDeleteStatement() {
		StatementBuilder builder = new StatementBuilder(POLY_A_SIGNAL_TABLE);
		return buildDeleteStatement(builder);
	}
	
	
	private PreparedStatement buildSelectStatement(StatementBuilder builder) {
		QueryConstraints qc = new QueryConstraints();
		qc.add(new QueryConstraint(ACCESSION_NUMBER_COL, ""));
		try {
			return dc.getConnection().prepareStatement(builder.buildSelectStatement(qc));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private PreparedStatement buildDeleteStatement(StatementBuilder builder) {
		QueryConstraints qc = new QueryConstraints();
		qc.add(new QueryConstraint(ACCESSION_NUMBER_COL, ""));
		try {
			return dc.getConnection().prepareStatement(builder.buildDeleteStatement(qc));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	private StatementBuilder buildBuilder() {
		StatementBuilder builder = new StatementBuilder("GENE");
		builder.add(ACCESSION_NUMBER_COL);
		builder.add("sequenceLength");
		builder.add("giNumber");
		builder.add("geneSymbol");
		builder.add("ncbiLink");
		builder.add("geneName");
		builder.add("species");
		
		builder.add("cdsStart");
		builder.add("cdsEnd");
		builder.add("utr3Start");
		builder.add("utr3End");
		builder.add("utr5Start");
		builder.add("utr5End");
		return builder;
	}
	
	private StatementBuilder buildPolyASiteBuilder() {
		StatementBuilder builder = new StatementBuilder(POLY_A_SITE_TABLE);
		addParamsToPolyBuilder(builder);
		return builder;
	}
	private StatementBuilder buildPolyASignalBuilder() {
		StatementBuilder builder = new StatementBuilder(POLY_A_SIGNAL_TABLE);
		addParamsToPolyBuilder(builder);
		return builder;
	}
	private void addParamsToPolyBuilder(StatementBuilder builder) {
		builder.add(ACCESSION_NUMBER_COL);
		builder.add("Start");
		builder.add("End");
	}
	
	
	private void fillPreparedStatement(PreparedStatement ps, GeneSequence s, boolean isUpdate) {
		try {
			ps.setString(1, s.getAccessionNumber());
			ps.setInt(2, s.getSequenceLength());
			ps.setString(3, s.getGiNumber());
			ps.setString(4, s.getGeneSymbol());
			ps.setString(5, s.getNcbiLink());
			ps.setString(6, s.getGeneName());
			ps.setString(7, s.getSpecies());
			
			ps.setInt(8, s.getCds().getStart());
			ps.setInt(9, s.getCds().getEnd());
			
			ps.setInt(10, s.getUtr3().getStart());
			ps.setInt(11, s.getUtr3().getEnd());
			
			ps.setInt(12, s.getUtr5().getStart());
			ps.setInt(13, s.getUtr5().getEnd());
			if ( isUpdate) {
				ps.setString(14, s.getAccessionNumber());
			}
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void fillPreparedStatement(PreparedStatement ps, String acsessionNumber, Range r) {
		try {
			ps.setString(1, acsessionNumber);
			ps.setInt(2, r.getStart());
			ps.setInt(3, r.getEnd());
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}
