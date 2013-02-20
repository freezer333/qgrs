package qgrs.compute.stat.qgrs;

import java.sql.Connection;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import qgrs.compute.stat.db.AnalysisRecord;
import qgrs.compute.stat.db.LocationRecord;
import qgrs.compute.stat.db.PartitionRecord;
import qgrs.compute.stat.db.SeriesRecord;

public class WorkbookBuilder {

	private final AnalysisRecord analysis;
	
	public WorkbookBuilder (AnalysisRecord analysis) {
		this.analysis = analysis;
	}
	
	public Workbook makeWorkbook(Connection con) throws Exception {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("analysis");
	    
	     
	    Row row = sheet.createRow((short)0);
	    Cell cell = row.createCell(0);
	    cell.setCellValue(this.analysis.description);
	    
	    int r = 1;
	    row = sheet.createRow(r++);
	    cell = row.createCell(0);
	    cell.setCellValue("Parition ID");
	    cell = row.createCell(1);
	    cell.setCellValue("Partition Description");
	    
	    for ( PartitionRecord p : analysis.partitions) {
		    row = sheet.createRow(r++);
		    cell = row.createCell(0);
		    cell.setCellValue(p.partitionId);
		    cell = row.createCell(1);
		    cell.setCellValue(p.description);
	    }
	    
	    row = sheet.createRow(r++);
	    cell = row.createCell(0);
	    cell.setCellValue("Series ID");
	    cell = row.createCell(1);
	    cell.setCellValue("Series Description");
	    
	    for ( SeriesRecord s : analysis.series) {
		    row = sheet.createRow(r++);
		    cell = row.createCell(0);
		    cell.setCellValue(s.seriesId);
		    cell = row.createCell(1);
		    cell.setCellValue(s.description);
	    }
	    
	    row = sheet.createRow(r++);
	    cell = row.createCell(0);
	    cell.setCellValue("Location ID");
	    cell = row.createCell(1);
	    cell.setCellValue("Location Description");
	    
	    for ( LocationRecord loc : analysis.locations) {
		    row = sheet.createRow(r++);
		    cell = row.createCell(0);
		    cell.setCellValue(loc.id);
		    cell = row.createCell(1);
		    cell.setCellValue(loc.label);
	    }
	    
	    
	    
	   /* row = sheet.createRow(1);
	    int i = 0;
	    for ( String col : cols ) {
	    	 cell = row.createCell(i++);
	    	 cell.setCellValue(col);
	    }
	    int rnum = 2;
	    
	    String q = "select * from " + this.analysis.getTableName();
	    Statement stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery(q);
	    while (rs.next()) {
	    	int cnum = 0;
	    	row = sheet.createRow(rnum++);
		    for (String col : cols ) {
	    		RowReader rr = reader.columns.get(col);
	    		String v = rr.read(rs, col);
	    		cell = row.createCell(cnum++);
	    		cell.setCellValue(v);
	    	}
	    }
	    */
	    return wb;
	}
}
