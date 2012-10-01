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
	int pathID;
	public final double gapOpenTolerance;
	public static int nextPathId = 0; 
	
	private static final double gapOpenPercentTolerance = 0.01;
	
	boolean isComplete() {
		return currentColumn == 0 && currentRow == 0;
	}
	
	
	public int distance() {
		return this.currentColumn + this.currentRow;
	}
	
	public int getPathID() {
		return pathID;
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
		pathID = nextPathId++;
		this.gapOpenTolerance = Math.min(colSeq.length, rowSeq.length)*gapOpenPercentTolerance;
		System.out.println("Using gap openning tolerance of " + this.gapOpenTolerance);
	}
	private AlignmentPath (BaseSymbol [] rowSeq, BaseSymbol [] colSeq, 
			int score, int col, int row, AlignmentCell.ScoreSource lastMove, int numGapOpenings, String seq1, String seq2){
		
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
		this.numGapsOpenned = numGapOpenings;
		this.gapOpenTolerance = Math.min(colSeq.length, rowSeq.length)*gapOpenPercentTolerance;
		pathID = nextPathId++;
	}
	
	@Override
	public AlignmentPath clone() {
		AlignmentPath p = new AlignmentPath(this.rowSeq, this.colSeq, score, currentColumn, currentRow, lastMove, this.numGapsOpenned, seq1.toString(), seq2.toString());
	//	System.out.println("path " + this.getPathID() + " spawned path " + p.getPathID());
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
			//System.out.println("path " + this.getPathID() + " encountered " + sources.size() + "-way tie at col/row " + currentColumn + "/" + currentRow);
			retval = this.expand(sources);
			
			for (int i = 0; i < sources.size(); i++ ) {
				retval.get(i).process(matrix, curCell, sources.get(i));
			}
		}
		return retval;
	}
	
	
	public int getNumGapsOpenned() {
		return numGapsOpenned;
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
		this.lastMove = source;
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
				//System.out.println("path " + this.getPathID() + " openned gap, now has " + this.numGapsOpenned);
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
		
		int retval = this.numGapsOpenned - ((AlignmentPath) arg0).numGapsOpenned;
		if ( Math.abs(retval) < gapOpenTolerance) {
			return (this.distance()) - (((AlignmentPath) arg0).distance());
		}
		else return retval;
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
