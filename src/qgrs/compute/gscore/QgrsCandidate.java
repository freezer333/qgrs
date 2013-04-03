package qgrs.compute.gscore;

import java.util.Collection;
import java.util.LinkedList;

import org.jdom.Element;

public class QgrsCandidate {
	enum NextStep { Loop1, Loop2, Loop3, Complete }
	
	final int numTetrads;
	final String sequence;
	final String tString;
	final int start;
	
	int y1 = -1;
	int y2 = -1;
	int y3 = -1;
	private int maxLength;
	
	public QgrsCandidate (String sequence, int numTetrads, int start) {
		this.sequence = sequence;
		this.numTetrads = numTetrads;
		this.start = start;
		this.tString = buildTetradString(numTetrads);
		this.maxLength = this.computeDefaultMaxLength();
	}
	
	public QgrsCandidate(QgrsCandidate parent, int nextLoop) {
		this.sequence = parent.sequence;
		this.numTetrads = parent.numTetrads;
		this.start = parent.start;
		this.tString = parent.tString;
		this.maxLength = parent.maxLength;
		this.y1 = parent.y1;
		this.y2 = parent.y2;
		this.y3 = parent.y3;
		if ( this.getNextStep() == NextStep.Loop1) y1 = nextLoop;
		else if ( this.getNextStep() == NextStep.Loop2) y2 = nextLoop;
		else if ( this.getNextStep() == NextStep.Loop3) y3 = nextLoop;
		else {
			throw new RuntimeException("Illegal creation of new qgrs - parent was already complete");
		}
	}
	
	public QgrsCandidate (int numTetrads, int y1, int y2, int y3) {
		this.sequence = null;
		this.start = 0;
		this.numTetrads = numTetrads;
		this.y1 = y1;
		this.y2 = y2;
		this.y3 = y3;
		this.tString = buildTetradString(numTetrads);
		this.maxLength = this.computeDefaultMaxLength();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("..." + start + " (" + numTetrads + ")   ");
		for ( int i = start; i <= lastNucleotideProcessed(); i++ ) {
			if (t_begin(i)) {
				sb.append("[");
			}
			sb.append(sequence.charAt(i));
			if (t_end(i)) {
				sb.append("]");
			}
		}
		
		if ( isValid() ) {
			sb.append("   ->  " + this.getScore());
		}
		return sb.toString();
	}
	
	boolean t_begin(int i) {
		if ( i == t1()) return true;
		if ( y1 >= 0 && i == t2() ) return true;
		if ( y2 >= 0 && i == t3() ) return true;
		if ( y3 >= 0 && i == t4() ) return true;
		return false;
	}
	boolean t_end(int i) {
		if ( i == t1()+numTetrads-1) return true;
		if ( y1 >= 0 && i == t2()+numTetrads-1 ) return true;
		if ( y2 >= 0 && i == t3()+numTetrads-1 ) return true;
		if ( y3 >= 0 && i == t4()+numTetrads-1 ) return true;
		return false;
	}
	
	private int lastNucleotideProcessed() {
		int retval = start + numTetrads;
		if ( y1 >= 0 ) {
			retval += (y1 + numTetrads);
		}
		if ( y2 >= 0 ) {
			retval += (y2 + numTetrads);
		}
		if ( y3 >= 0 ) {
			retval += (y3 + numTetrads);
		}
		return retval-1;
	}

	public static String buildTetradString(int numTetrads) {
		StringBuilder sb = new StringBuilder();
		for ( int i =0; i < numTetrads; i++ ) sb.append("G");
		return sb.toString();
	}
	
	public Collection<QgrsCandidate> expand() {
		LinkedList<QgrsCandidate> expansion = new LinkedList<QgrsCandidate>();
		Collection<Integer> loopLengths = findPossibleLoopLengthsFrom(cursor());
		for ( Integer y : loopLengths ) {
			expansion.add(new QgrsCandidate(this, y));
		}
		return expansion;
	}
	
	public Element getXmlElement() {
		if ( !isValid() ) {
			throw new RuntimeException("Qgrs is not valid");
		}
		Element e = new Element("qgrs");
		e.addContent(new Element("start").setText(String.valueOf(t1())));
		e.addContent(new Element("x").setText(String.valueOf(this.numTetrads)));
		e.addContent(new Element("y1").setText(String.valueOf(this.y1)));
		e.addContent(new Element("y2").setText(String.valueOf(this.y2)));
		e.addContent(new Element("y3").setText(String.valueOf(this.y3)));
		e.addContent(new Element("length").setText(String.valueOf(this.numTetrads*4 + y1 + y2 + y3)));
		e.addContent(new Element("score").setText(String.valueOf(this.getScore())));
		Element d = new Element ("nucleotides");
		d.addContent(new Element("ts").setText(buildTetradString(numTetrads)));
		d.addContent(new Element("loop1").setText(sequence.substring(t1()+numTetrads, t1()+numTetrads+y1)));
		d.addContent(new Element("loop2").setText(sequence.substring(t2()+numTetrads, t2()+numTetrads+y2)));
		d.addContent(new Element("loop3").setText(sequence.substring(t3()+numTetrads, t3()+numTetrads+y3)));
		e.addContent(d);
		return e;
	}
	
	Collection<Integer> findPossibleLoopLengthsFrom(int i) {
		Collection<Integer> retval = new LinkedList<Integer>();
		int p = i;
		do {
			p = sequence.indexOf(tString, p);
			if ( p >= 0 ) {
				int y = p-i;
				if ( y >= this.minAcceptableLoopLength()) {
					retval.add(y);
				}
			}
			p++;
		} while (p > 0);
		
		return retval;
	}
	
	
	int cursor() {
		if ( y1 < 0  ){ 
			return t1() + numTetrads;
		}
		else if ( y2 < 0 ) {
			return t2() + numTetrads;
		}
		else if ( y3 < 0 ) {
			return t3() + numTetrads;
		}
		throw new RuntimeException("cursor cannot be computed for qgrs, loops are already complete");
	}
	
	
	public boolean isValid() {
		if ( y1 < 0 ) return false;
		if ( y2 < 0 ) return false;
		if ( y3 < 0 ) return false;
		return true;
	}
	
	
	int getScore() {
		if (!isValid() ) {
			throw new RuntimeException("Qgrs is invalid, score cannot be computed");
		}
		float gavg = (Math.abs(y1-y2) + Math.abs(y2-y3) + Math.abs(y3-y1))/3.0f;
		float gscore = gmax() - gavg + gmax()*T();
		return Math.round(gscore);
	}
	
	int minAcceptableLoopLength() {
		if ( y1 == 0 || y2 == 0 || y3 == 0 ) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	int T() {
		return numTetrads-2;
	}
	
	int gmax() {
		return getMaxLength()-9;
	}
	
	int computeDefaultMaxLength(){
		if ( numTetrads < 3 ) return 30;
		return 45;
	}
	int getMaxLength() {
		return this.maxLength;
	}
	
	public void setMaxLength(int v) {
		this.maxLength = v;
	}
	
	NextStep getNextStep() {
		if ( y1 < 0 ) return NextStep.Loop1;
		if ( y2 < 0 ) return NextStep.Loop2;
		if ( y3 < 0 ) return NextStep.Loop3;
		return NextStep.Complete;
	}
	
	int t1() {
		return start;
	}
	int t2() {
		if ( y1 < 0  ){
			throw new RuntimeException("Loop 1 not defined");
		}
		return t1() + numTetrads + y1;
	}
	int t3() {
		if ( y2 < 0  ){
			throw new RuntimeException("Loop 2 not defined");
		}
		return t2() + numTetrads + y2;
	}
	int t4() {
		if ( y3 < 0  ) {
			throw new RuntimeException("Loop 3 not defined");
		}
		return t3() + numTetrads + y3;
	}
}
