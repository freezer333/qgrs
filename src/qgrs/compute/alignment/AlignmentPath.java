package qgrs.compute.alignment;

import java.util.LinkedList;

import qgrs.data.BaseSymbol;

public class AlignmentPath implements Comparable{
	int score;
	int currentColumn;
	int currentRow;
	AlignmentCell.ScoreSource lastMove;
	PrependingStringBuffer seq1;
	PrependingStringBuffer seq2;
	int numGapsOpenned = 0;
	BaseSymbol [] rowSeq;
	BaseSymbol [] colSeq;
	int nextRowSymbol;
	int nextColSymbol;
	
	boolean isComplete() {
		return currentColumn == 0 && currentRow == 0;
	}
	
	
	public AlignmentPath(BaseSymbol [] rowSeq, BaseSymbol [] colSeq, AlignmentCell initial, int col, int row) {
		currentColumn = col;
		currentRow = row;
		lastMove = null;
		score = initial.getScore();
		this.colSeq = colSeq;
		this.rowSeq = rowSeq;
		
		int bufSize =(int)(Math.max(this.colSeq.length, this.rowSeq.length) * 1.2);
		seq1 = new PrependingStringBuffer(bufSize);
		seq2 = new PrependingStringBuffer(bufSize);
		
		this.nextColSymbol = this.colSeq.length-1;
		this.nextRowSymbol = this.rowSeq.length-1;
	}
	private AlignmentPath (BaseSymbol [] rowSeq, BaseSymbol [] colSeq, 
			int score, int col, int row, AlignmentCell.ScoreSource lastMove, String seq1, String seq2){
		
		this.currentColumn = col;
		this.currentRow = row;
		this.lastMove = lastMove;
		this.score = score;
		this.colSeq = colSeq;
		this.rowSeq = rowSeq;
		
		int bufSize =(int)(Math.max(this.colSeq.length, this.rowSeq.length) * 1.2);
		this.seq1 = new PrependingStringBuffer(seq1, bufSize);
		this.seq2 = new PrependingStringBuffer(seq2, bufSize);
		
		this.nextColSymbol = this.colSeq.length-1;
		this.nextRowSymbol = this.rowSeq.length-1;
	}
	
	@Override
	public AlignmentPath clone() {
		AlignmentPath p = new AlignmentPath(this.rowSeq, this.colSeq, score, currentColumn, currentRow, lastMove, seq1.toString(), seq2.toString());
		return p;
	}
	
	
	
	public LinkedList<AlignmentPath> step(AlignmentMatrix matrix) {
		AlignmentCell curCell = matrix.getCell(currentColumn, currentRow);
		LinkedList<AlignmentCell.ScoreSource> sources = getSources(curCell);
		LinkedList<AlignmentPath> retval ;
		
		if ( sources.size() == 1 ) {
			this.process(matrix, curCell, sources.getFirst());
			retval = new LinkedList<AlignmentPath>();
			retval.add(this);
		}
		else {
			// tie, expand/clone paths and process each with the corresponding sources.
			retval = this.expand(sources);
			for (int i = 0; i < sources.size(); i++ ) {
				retval.get(i).process(matrix, curCell, sources.get(i));
			}
		}
		return retval;
	}
	
	
	LinkedList<AlignmentCell.ScoreSource> getSources(AlignmentCell curCell) {
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
		return sources;
	}
	
	
	void process(AlignmentMatrix matrix, AlignmentCell curCell, AlignmentCell.ScoreSource source) {
		checkForGapOpenning(source);
		recordRowSeqToken(source);
		recordColSeqToken(source);
		nextCol(source);
		nextRow(source);
	}
	
	LinkedList<AlignmentPath> expand(LinkedList<AlignmentCell.ScoreSource> sources) {
		LinkedList<AlignmentPath> retval = new LinkedList<AlignmentPath>();
		retval.add(this);
		retval.add(this.clone());
		if ( sources.size() > 2 ) {
			retval.add(this.clone());
		}
		return retval;
	}
	
	void checkForGapOpenning(AlignmentCell.ScoreSource source) {
		if ( source != AlignmentCell.ScoreSource.DIAGONAL ) {
			if ( lastMove != null && !source.equals(this.lastMove) ) {
				this.numGapsOpenned++;
			}
		}
	}
	
	
	
	void recordRowSeqToken(AlignmentCell.ScoreSource source) {
		if (nextRowSymbol < 0 ||  source == AlignmentCell.ScoreSource.LEFT ) {
			seq2.prepend('-');
		}
		else {
			seq2.prepend(this.rowSeq[this.nextRowSymbol--].toString());
		}
	}
	
	void recordColSeqToken(AlignmentCell.ScoreSource source) {
		if ( nextColSymbol < 0 || source == AlignmentCell.ScoreSource.TOP ) {
			seq1.prepend('-');
		}
		else {
			seq1.prepend(this.colSeq[this.nextColSymbol--].toString());	
		}
	}
	
	int nextRow(AlignmentCell.ScoreSource source) {
		if ( source == AlignmentCell.ScoreSource.LEFT ) {
			return currentRow;
		}
		else {
			return currentRow--;
		}
	}
	
	int nextCol(AlignmentCell.ScoreSource source) {
		if ( source == AlignmentCell.ScoreSource.TOP ) {
			return currentColumn;
		}
		else {
			return currentColumn--;
		}
	}


	@Override
	public int compareTo(Object arg0) {
		if ( ! (arg0 instanceof AlignmentPath)) 
			throw new RuntimeException("Comparison error for alignment path");
		
		return this.numGapsOpenned - ((AlignmentPath) arg0).numGapsOpenned;
	}


	public String getSeq1() {
		return seq1.toString();
	}


	public String getSeq2() {
		return seq2.toString();
	}


	public int getCurrentColumn() {
		return currentColumn;
	}


	

	public int getCurrentRow() {
		return currentRow;
	}


	
	
	
}
