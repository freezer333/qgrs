package qgrs.compute.alignment;

import qgrs.data.BaseSymbol;

public class CellComputeParameter {
	private AlignmentCell left;
	private AlignmentCell top;
	private AlignmentCell diagonal;
	private BaseSymbol colSymbol;
	private BaseSymbol rowSymbol;
	private boolean lastRow;
	private boolean lastCol;
	private AlignmentProperties props;

	public CellComputeParameter(AlignmentCell left, AlignmentCell top,
			AlignmentCell diagonal, BaseSymbol colSymbol, BaseSymbol rowSymbol,
			boolean lastCol, boolean lastRow, AlignmentProperties props) {
		this.left = left;
		this.top = top;
		this.diagonal = diagonal;
		this.colSymbol = colSymbol;
		this.rowSymbol = rowSymbol;
		this.lastCol = lastCol;
		this.lastRow = lastRow;
		this.props = props;
	}
	
	
	public int getSimilarityScore() {
		return props.getSimilarityScore(colSymbol, rowSymbol);
	}
	
	public int getCombinedGapPenalty() {
		return props.getGapExtensionPenalty() + props.getGapOpenningPenalty();
	}

	public AlignmentCell getLeft() {
		return left;
	}

	public void setLeft(AlignmentCell left) {
		this.left = left;
	}

	public AlignmentCell getTop() {
		return top;
	}

	public void setTop(AlignmentCell top) {
		this.top = top;
	}

	public AlignmentCell getDiagonal() {
		return diagonal;
	}

	public void setDiagonal(AlignmentCell diagonal) {
		this.diagonal = diagonal;
	}

	public BaseSymbol getColSymbol() {
		return colSymbol;
	}

	public void setColSymbol(BaseSymbol colSymbol) {
		this.colSymbol = colSymbol;
	}

	public BaseSymbol getRowSymbol() {
		return rowSymbol;
	}

	public void setRowSymbol(BaseSymbol rowSymbol) {
		this.rowSymbol = rowSymbol;
	}

	
	public boolean isLastRow() {
		return lastRow;
	}


	public void setLastRow(boolean lastRow) {
		this.lastRow = lastRow;
	}


	public boolean isLastCol() {
		return lastCol;
	}


	public void setLastCol(boolean lastCol) {
		this.lastCol = lastCol;
	}


	public AlignmentProperties getProps() {
		return props;
	}

	public void setProps(AlignmentProperties props) {
		this.props = props;
	}
	
	
	
	
}