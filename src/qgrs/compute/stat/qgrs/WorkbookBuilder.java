package qgrs.compute.stat.qgrs;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import qgrs.compute.stat.db.AnalysisRecord;
import qgrs.compute.stat.db.LocationRecord;
import qgrs.compute.stat.db.PartitionRecord;
import qgrs.compute.stat.db.RecordKey;
import qgrs.compute.stat.db.ResultRecord;
import qgrs.compute.stat.db.SeriesRecord;

public class WorkbookBuilder {

	private final AnalysisRecord analysis;
	
	public WorkbookBuilder (AnalysisRecord analysis) {
		this.analysis = analysis;
	}
	
	public Workbook makeWorkbook(Connection con) throws Exception {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("analysis");
	    writeSummarySheet(sheet);
	    for ( PartitionRecord p : analysis.partitions ) {
	    	writePartitionSheet(wb, p);
	    }
	    return wb;
	}
	
	void writePartitionSheet(Workbook wb, PartitionRecord part) {
		Sheet sheet = wb.createSheet(part.partitionId);
		DecimalFormat format = new DecimalFormat("0.00");
		Row row = sheet.createRow(0);
		Row rRow = sheet.createRow(1);
		int c = 1;
		for ( SeriesRecord s : analysis.series ) {
			Cell col = row.createCell(c);
			col.setCellValue(s.description);
			col = rRow.createCell(c++);
			col.setCellValue("Total");
			col = rRow.createCell(c++);
			col.setCellValue("Mean");
			col = rRow.createCell(c++);
			col.setCellValue("Median");
		}
		
		int r = 2;
		List<LocationRecord> locations = new LinkedList<LocationRecord>();
		locations.addAll(analysis.locations);
		Collections.sort(locations);
		for ( LocationRecord loc : locations ){
			c = 0;
			row = sheet.createRow(r);
			Cell col = row.createCell(c);
			col.setCellValue(loc.label);
			c++;
			for ( SeriesRecord s : analysis.series ) {
				RecordKey key = new RecordKey(part, s, loc);
				ResultRecord result = analysis.results.get(key);
				col = row.createCell(c++);
				col.setCellValue(String.valueOf(result.total));
				col = row.createCell(c++);
				col.setCellValue(format.format(result.mean));
				col = row.createCell(c++);
				col.setCellValue(format.format(result.median));
			}
			r++;
		}
	}

	private void writeSummarySheet(Sheet sheet) {
		Row row = sheet.createRow((short)0);
	    Cell cell = row.createCell(0);
	    cell.setCellValue(this.analysis.description);
	    
	    int r = 1;
	    r = writePartitionInfo(sheet, r);
	    
	    r = writeSeriesInfo(sheet, r);
	    
	    writeLocationInfo(sheet, r);
	}

	private void writeLocationInfo(Sheet sheet, int r) {
		Row row;
		Cell cell;
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
	    
	}

	private int writeSeriesInfo(Sheet sheet, int r) {
		Row row;
		Cell cell;
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
		return r;
	}

	private int writePartitionInfo(Sheet sheet, int r) {
		Row row;
		Cell cell;
		row = sheet.createRow(r++);
	    cell = row.createCell(0);
	    cell.setCellValue("Parition ID");
	    cell = row.createCell(1);
	    cell.setCellValue("Partition Description");
	    cell = row.createCell(2);
	    cell.setCellValue("# of Genes in Partition");
	    
	    for ( PartitionRecord p : analysis.partitions) {
		    row = sheet.createRow(r++);
		    cell = row.createCell(0);
		    cell.setCellValue(p.partitionId);
		    cell = row.createCell(1);
		    cell.setCellValue(p.description);
		    cell = row.createCell(2);
		    cell.setCellValue(p.numSamples);
	    }
		return r;
	}
}
