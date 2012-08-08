package framework.excel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

public class BasicCell {
	final private int rowIndex;
	final private int colIndex;
	final private Sheet sheet;
	final private Workbook wbk;
	final private FormulaEvaluator evaluator;
	
	public BasicCell(Workbook wbk, Sheet sheet, int row, int col) {
		this.wbk = wbk;
		this.sheet = sheet;
		this.rowIndex = row;
		this.colIndex = col;
		this.evaluator = wbk.getCreationHelper().createFormulaEvaluator();
	}
	
	public BasicCell(Workbook wbk, Sheet sheet, int row, String col) {
		this.wbk = wbk;
		this.sheet = sheet;
		this.rowIndex = row;
		this.colIndex = toColNum(col);
		this.evaluator = wbk.getCreationHelper().createFormulaEvaluator();
	}
	
	
	public static int toColNum(String alphaColLabel ) {
		return CellReference.convertColStringToIndex(alphaColLabel);
	}
	
	public int getRow() {
		return rowIndex;
	}



	public int getCol() {
		return colIndex;
	}



	public Sheet getSheet() {
		return sheet;
	}



	public Workbook getWbk() {
		return wbk;
	}


	public boolean hasValue() {
		String s = read();
		return s != null && s.trim().length() > 0;
	}
	
	public String getRangeString() {
		return (CellReference.convertNumToColString(this.getCol()) + (this.getRow()+1));
	}
	
	
	private String getCellContent() {
		Row row = sheet.getRow(this.getRow());
		if ( row == null ) return "";
	    Cell cell = row.getCell(this.getCol());
		if ( cell == null ) return "";
		switch ( cell.getCellType() ) {
			case Cell.CELL_TYPE_STRING:
				return cell.getRichStringCellValue().getString();
	      case Cell.CELL_TYPE_NUMERIC:
	    	  if(DateUtil.isCellDateFormatted(cell)) {
	    		Date a = cell.getDateCellValue();
	  			DateFormat outdfm = new SimpleDateFormat("dd/MM/yy");
	  			return outdfm.format(a);
	    	  } 
	    	  return String.valueOf(cell.getNumericCellValue());
	      case Cell.CELL_TYPE_BOOLEAN:
	    	  return String.valueOf((cell.getBooleanCellValue()));
	      case Cell.CELL_TYPE_FORMULA:
	  		  return readFormula(cell);
	    	  
		}
		return "";
	}
	
	private String readFormula(Cell cell) {
		CellValue cellValue = evaluator.evaluate(cell);
		if ( cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN ) {
			return String.valueOf(cellValue.getBooleanValue());
		}
		else if ( cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC ) {
			return String.valueOf(cellValue.getNumberValue());
		}
		else if ( cellValue.getCellType() == Cell.CELL_TYPE_STRING ) {
			return cellValue.getStringValue();
		}
		else return "";
		
	}
	public int readInt() {
		try {
			return ((Double)Double.parseDouble(read())).intValue();
		}
		catch (Exception e) {
			return 0;
		}
	}
	public String read() {
		String retval = this.getCellContent();
		System.out.println("Reading " + (getRangeString()) + " ->" + retval);
		return retval;
	}
}
