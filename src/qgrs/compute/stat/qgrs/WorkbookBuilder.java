package qgrs.compute.stat.qgrs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import qgrs.compute.stat.qgrs.TableReader.RowReader;

public class WorkbookBuilder {

	private final QgrsRunner runner;
	
	public WorkbookBuilder (QgrsRunner runner) {
		this.runner = runner;
	}
	
	public Workbook makeWorkbook(Connection con) throws Exception {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("analysis");
	    
	    TableReader reader = new TableReader(this.runner.getTableName(), con);
	    List<String> cols = new ArrayList<String>();
	    cols.addAll(reader.columns.keySet());
	    Collections.sort(cols);
	    
	    Row row = sheet.createRow((short)0);
	    Cell cell = row.createCell(0);
	    cell.setCellValue(this.runner.getDescription());
	    
	    row = sheet.createRow(1);
	    int i = 0;
	    for ( String col : cols ) {
	    	 cell = row.createCell(i++);
	    	 cell.setCellValue(col);
	    }
	    int rnum = 2;
	    
	    String q = "select * from " + this.runner.getTableName();
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
	    
	    return wb;
	}
}
