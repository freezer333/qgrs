package framework.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class BasicSheet {

	final Workbook workbook;
	final Sheet sheet;
	
	public BasicSheet(Workbook wbk, Sheet sheet) {
		this.workbook = wbk;
		this.sheet = sheet;
	}
	
	public Sheet getSheet() {
		return sheet;
	}

	public BasicCell getCell(int row, int col) {
		return new BasicCell(this.workbook, this.sheet, row, col);
	}
	public BasicCell getCell(int row, String col) {
		return new BasicCell(this.workbook, this.sheet, row, col);
	}
	
	
	
	public ColumnRange getColumnRange(int col) {
		return new ColumnRange(this.workbook, this.sheet, col);
	}
	public ColumnRange getColumnRange(String col) {
		return new ColumnRange(this.workbook, this.sheet, col);
	}
	public ColumnRange getColumnRange(String col, int startRow) {
		return new ColumnRange(this.workbook, this.sheet, col, startRow);
	}
	public ColumnRange getColumnRange(int col, int startRow) {
		return new ColumnRange(this.workbook, this.sheet, col, startRow);
	}
	
	public RowRange getRowRange(int row) {
		return new RowRange(this.workbook, this.sheet, row);
	}
	public RowRange getRowRange(int row, String startCol) {
		return new RowRange(this.workbook, this.sheet, row, startCol);
	}
	public RowRange getRowRange(int row, int startCol) {
		return new RowRange(this.workbook, this.sheet, row, startCol);
	}
	
	
}
