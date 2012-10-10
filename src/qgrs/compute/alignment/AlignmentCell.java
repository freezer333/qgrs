package qgrs.compute.alignment;

import java.util.LinkedList;


class AlignmentCell {

	public enum ScoreSource { TOP, LEFT, DIAGONAL };
	
	public final int topScore;
	public final int leftScore;
	public final int diagonalScore;
	
	public AlignmentCell() {
		this.diagonalScore = 0;
		this.leftScore = 0;
		this.topScore = 0;
	}
	public AlignmentCell(int value) {
		this.diagonalScore = value;
		this.leftScore = value;
		this.topScore = value;
	}
	public AlignmentCell(int left, int top, int diag) {
		this.diagonalScore = diag;
		this.leftScore = left;
		this.topScore = top;
	}
	
	
	public AlignmentCell(CellComputeParameter params) {
		this.diagonalScore = diagonal(params);
		this.leftScore = left(params);
		this.topScore = top(params);
	}
	
	@Override 
	public String toString() {
		return leftScore + "/" + this.diagonalScore + "/" + topScore + "=" + this.getScore();
	}
	public int getScore() {
		return Math.max(Math.max(topScore, leftScore), diagonalScore);
	}
	
	public int getLeftScore() {
		return this.leftScore;
	}
	public int getTopScore() {
		return this.topScore;
	}
	
	public LinkedList<ScoreSource> getScoreSources() {
		LinkedList<ScoreSource> retval = new LinkedList<ScoreSource>();
		int maxScore = getScore();
		if ( topScore == maxScore ) {
			retval.add(ScoreSource.TOP);
		}
		if ( leftScore == maxScore ) {
			retval.add(ScoreSource.LEFT);
		}
		if ( diagonalScore == maxScore ) {
			retval.add(ScoreSource.DIAGONAL);
		}
		return retval;
	}
	
	
	
	private int diagonal(CellComputeParameter params) {
		int matchScore = params.getSimilarityScore();
		return params.getDiagonal().getScore() + matchScore;
	}
	
	private int left(CellComputeParameter params) {
		if ( params.isLastRow() ) {
			return params.getLeft().getScore();
		}
		else {
			int openning = params.getLeft().getScore() + params.getCombinedGapPenalty();
			if ( openning != params.getLeft().getLeftScore() ) {
				openning += params.getCombinedGapPenalty();
			}
			else {
				openning += params.getProps().getGapExtensionPenalty();
			}
			int extend = params.getLeft().getLeftScore() + params.getProps().getGapExtensionPenalty();
			return Math.max(openning, extend);
		}
	}
	private int top(CellComputeParameter params) {
		if ( params.isLastCol() ) {
			return params.getTop().getScore();
		}
		else {
			int openning = params.getTop().getScore();
			if ( openning != params.getTop().getTopScore() ) {
				openning += params.getCombinedGapPenalty();
			}
			else {
				openning += params.getProps().getGapExtensionPenalty();
			}
			int extend = params.getTop().getTopScore() + params.getProps().getGapExtensionPenalty();
			return Math.max(openning, extend);
		}
	}
	
	
}
