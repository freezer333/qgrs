package framework.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ColumnRange extends AbstractRange {
	public ColumnRange(Workbook wbk, Sheet sheet, int col) {
		super(wbk, sheet);
		this.colIndex = col;
	}
	public ColumnRange(Workbook wbk, Sheet sheet, String col) {
		super(wbk, sheet);
		this.colIndex = BasicCell.toColNum(col);	
	}
	
	public ColumnRange(Workbook wbk, Sheet sheet, int col, int startRow) {
		super(wbk, sheet);
		this.rowIndex = startRow;
		this.colIndex = col;
	}
	
	public ColumnRange(Workbook wbk, Sheet sheet,String col, int startRow) {
		super(wbk, sheet);
		this.rowIndex = startRow;
		this.colIndex = BasicCell.toColNum(col);	
	}
	
	@Override
	public void forward() {
		this.rowIndex++;
	}
	
	/**
	 * Searches the column, starting at the startRow and ending before endRow for the given value
	 * instance allows you to specify 2 if you want the 2nd instance.
	 * @param value
	 * @return -1 if no match
	 */
	public int findRowContaining(String value, int startRow, int endRow, int instance) {
		int times = 0;
		for ( int i = startRow; i < endRow; i++ ) {
			if ( value.equals(new BasicCell(this.wbk, this.sheet, i, this.colIndex).read() ) ) {
				times++;
			}
			if ( times == instance ) return i;
		}
		return -1;
	}
	public int findRowContaining(String value, int endRow, int instance) {
		return findRowContaining(value, this.rowIndex, endRow, instance);
	}
	public int findRowContaining(String value, int endRow) {
		return findRowContaining(value, this.rowIndex, endRow, 1);
	}
}
