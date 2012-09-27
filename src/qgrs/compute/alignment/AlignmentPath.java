package qgrs.compute.alignment;

import java.util.Collection;
import java.util.LinkedList;

import qgrs.data.BaseSymbol;

public class AlignmentPath {
	int score;
	int currentColumn;
	int currentRow;
	AlignmentCell.ScoreSource lastMove;
	
	String seq1;
	String seq2;
	int numGapsOpenned = 0;
	BaseSymbol [] rowSeq;
	BaseSymbol [] colSeq;
	int nextRowSymbol;
	int nextColSymbol;
	
	boolean isComplete() {
		return currentColumn == 0 && currentRow == 0;
	}
	
	AlignmentPath(BaseSymbol [] rowSeq, BaseSymbol [] colSeq, AlignmentCell initial, int col, int row) {
		currentColumn = col;
		currentRow = row;
		lastMove = null;
		score = initial.getScore();
		seq1 = "";
		seq2 = "";
		this.colSeq = colSeq;
		this.rowSeq = rowSeq;
		this.nextColSymbol = this.colSeq.length-1;
		this.nextRowSymbol = this.rowSeq.length-1;
	}
	
	public Collection<AlignmentPath> step(AlignmentMatrix matrix) {
		AlignmentCell curCell = matrix.getCell(currentColumn, currentRow);
		
		LinkedList<AlignmentCell.ScoreSource> sources = new LinkedList<AlignmentCell.ScoreSource>();
		if ( currentRow == 0 ) {
			sources.add(AlignmentCell.ScoreSource.LEFT);
		}
		else  if (currentColumn == 0) {
			sources.add(AlignmentCell.ScoreSource.TOP);
		}
		else {
			sources = curCell.getScoreSources();
		}
		
		
		if ( sources.size() == 1 ) {
			this.process(matrix, curCell, sources.getFirst());
			LinkedList<AlignmentPath> retval = new LinkedList<AlignmentPath>();
			retval.add(this);
			return retval;
		}
		else {
			throw new RuntimeException("Ties are not handles at this time");
		}
	}
	
	int nextRow(AlignmentCell.ScoreSource source) {
		if ( source == AlignmentCell.ScoreSource.LEFT ) {
			return currentRow;
		}
		else {
			return currentRow-1;
		}
	}
	
	void recordRowSeqToken(AlignmentCell.ScoreSource source) {
		if ( source == AlignmentCell.ScoreSource.TOP ) {
			seq1 = "-" + seq1;
		}
		else {
			seq1 = this.rowSeq[this.nextRowSymbol--].toString() + seq1;	
		}
	}
	void recordColSeqToken(AlignmentCell.ScoreSource source) {
		if ( source == AlignmentCell.ScoreSource.LEFT ) {
			seq2 = "-" + seq2;
		}
		else {
			seq2 = this.colSeq[this.nextColSymbol--].toString() + seq2;	
		}
	}
	int nextCol(AlignmentCell.ScoreSource source) {
		if ( source == AlignmentCell.ScoreSource.TOP ) {
			return currentColumn;
		}
		else {
			return currentColumn-1;
		}
	}
	void process(AlignmentMatrix matrix, AlignmentCell curCell, AlignmentCell.ScoreSource source) {
		if ( source != AlignmentCell.ScoreSource.DIAGONAL ) {
			// may be gap opening
			if ( lastMove != null && !source.equals(this.lastMove) ) {
				this.numGapsOpenned++;
			}
		}
		
		recordRowSeqToken(source);
		recordColSeqToken(source);
		
		// Move to next position
		nextCol(source);
		nextRow(source);
	}
}
