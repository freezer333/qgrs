package framework.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class AbstractRange{
	final protected Sheet sheet;
	final protected Workbook wbk;
	protected int rowIndex = 0;
	protected int colIndex = 0;
	
	public AbstractRange(Workbook wbk, Sheet sheet) {
		this.wbk = wbk;
		this.sheet = sheet;
		
	}
	
	abstract public void forward() ;
	
	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}
	public void setColIndex(String colIndex) {
		this.colIndex = BasicCell.toColNum(colIndex);
	}

	public BasicCell getCell() {
		return new BasicCell(this.wbk, this.sheet, this.rowIndex, this.colIndex);
	}
	
	public BasicCell getCellAndMove() {
		BasicCell retval = this.getCell();
		this.forward();
		return retval;
	}
}
