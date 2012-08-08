package framework.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/** Moves across a given row */
public class RowRange extends AbstractRange {

	public RowRange(Workbook wbk, Sheet sheet, int row) {
		super(wbk, sheet);
		this.rowIndex = row;
	}
	
	public RowRange(Workbook wbk, Sheet sheet, int row, int startCol) {
		super(wbk, sheet);
		this.rowIndex = row;
		this.colIndex = startCol;
	}
	public RowRange(Workbook wbk, Sheet sheet, int row, String startCol) {
		super(wbk, sheet);
		this.rowIndex = row;
		this.colIndex = BasicCell.toColNum(startCol);	
	}
	
	@Override
	public void forward() {
		this.colIndex++;
	}
	
	
	
	

}
