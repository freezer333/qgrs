package qgrs.compute.alignment;

import qgrs.data.Base;
import qgrs.data.BaseSymbol;
import qgrs.data.GeneSequence;
import framework.diagnostic.MemoryReporter;

public class AlignmentMatrix {

	private AlignmentCell [][] m;
	private final int lastColIndex;
	private final int lastRowIndex;
	private final AlignmentProperties props;
	private final BaseSymbol [] columnSymbols;
	private final BaseSymbol [] rowSymbols;
	
	public AlignmentMatrix (GeneSequence gene1, GeneSequence gene2, AlignmentProperties alignmentProperites) throws Exception {
		createMatrix(gene1.getSequenceLength()+1, gene2.getSequenceLength()+1);
		this.lastColIndex = gene1.getSequenceLength();
		this.lastRowIndex = gene2.getSequenceLength();
		this.props = alignmentProperites;
		
		columnSymbols = new BaseSymbol[gene1.getSequenceLength()];
		fillSymbolArray(columnSymbols, gene1);
		rowSymbols = new BaseSymbol[gene2.getSequenceLength()];
		fillSymbolArray(rowSymbols, gene2);
	}
	
	public AlignmentCell getCell(int col, int row) {
		return m[col][row];
	}
	public BaseSymbol getRowSymbol(int col) {
		return rowSymbols[col-1];
	}
	public BaseSymbol getColSymbol(int row) {
		return columnSymbols[row-1];
	}
	
	
	public AlignmentPath computeBestAlignment() {
		buildForward();
		return backtracePaths();
	}
	
	private void buildForward() {
		
		m[0][0] = new AlignmentCell();
		// Initialize first row to have 0's
		for ( int col = 1; col <= lastColIndex; col++ ) {
			m[col][0] = new AlignmentCell(0, props.getGapExtensionPenalty() + props.getGapOpenningPenalty(), 0);
		}
		
		// Initialize first column to have 0's
		for ( int row = 1; row <= lastRowIndex; row++) {
			m[0][row] = new AlignmentCell(props.getGapExtensionPenalty() + props.getGapOpenningPenalty(), 0, 0);
		}
		
		// Create the rest of the matrix
		for ( int col = 1; col <= lastColIndex; col++ ) {
			for ( int row = 1; row <= lastRowIndex; row++ ) {
				m[col][row] = new AlignmentCell(buildParam(col, row));
			}
		}
		printDebug();
	}
	
	private AlignmentPath backtracePaths() {
		return null;
	}
	
	
	
	
	
	
	
	
	private static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}
	
	public void printDebug() {
		System.out.print(padLeft(" ", 5) + " |");
		for ( int col = 0; col <= lastColIndex; col++ ) {
			String p;
			if ( col == 0 ) {
				p = " ";
			}
			else {
				p = columnSymbols[col-1].toString();
			}
			System.out.print(padLeft(p, 5) + " |");
		}
		System.out.println();
		for ( int col = 0; col <= lastColIndex + 1; col++ ) {
			System.out.print("-------");
		}
		System.out.println();
		for ( int row = 0; row <= lastRowIndex; row++ ) {
			String p;
			if ( row == 0 ) {
				p = " ";
			}
			else {
				p = rowSymbols[row-1].toString();
			}
			System.out.print(padLeft(p, 5) + " |");
			for ( int col = 0; col <= lastColIndex; col++ ) {
				p = String.valueOf(m[col][row].getScore());
				System.out.print(padLeft(p, 5) + " |");
			}
			System.out.println();
			for ( int col = 0; col <= lastColIndex+1; col++ ) {
				System.out.print("-------");
			}
			System.out.println();
		}
		System.out.println("Alignment Score -> " + m[lastColIndex][lastRowIndex].getScore());
	}
	
	
	private void createMatrix(int cols, int rows) throws Exception {
		int times = 0;
		while ( times < 15) {
			try {
				m = new AlignmentCell[cols][rows];
				return;
			}
			catch (Throwable t) {
				System.gc();
				Thread.sleep(1000);
				times++;
				System.out.println("Waiting for enough memory to become available for matrix") ;
				MemoryReporter.memoryReport();
			}
		}
		// out of while loop.  Try one last time.
		m = new AlignmentCell[rows][cols];
	}
	
	private void fillSymbolArray(BaseSymbol [] symbols, GeneSequence gene) {
		int i = 0;
		for ( Base b : gene.getBases() ) {
			symbols[i++] = b.getSymbol();
		}
	}
	
	private CellComputeParameter buildParam(int col, int row) {
		return new CellComputeParameter(
				m[col-1][row], 
				m[col][row-1], 
				m[col-1][row-1], 
				columnSymbols[col-1], // minus 1 because our matrix has an extra col and row at top and left.
				rowSymbols[row-1],
				col==lastColIndex, 
				row==lastRowIndex, 
				props);
	}
	
}
