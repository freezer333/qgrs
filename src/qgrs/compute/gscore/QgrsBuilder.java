package qgrs.compute.gscore;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class QgrsBuilder {
	final String sequence;
	Collection<QgrsCandidate> qgrs = new LinkedList<QgrsCandidate>();
	Queue<QgrsCandidate> processingQueue = new LinkedList<QgrsCandidate>();
	
	public QgrsBuilder(String sequence) {
		this.sequence = sequence;
	}
	
	public Collection<QgrsCandidate> findQgrs() {
		seedQueue();
		while ( !processingQueue.isEmpty()) {
			QgrsCandidate c = processingQueue.remove();
			if ( c.isCompleted()  ) {
				if ( c.isViable() ) {
					qgrs.add(c);
				}
			}
			else {
				for ( QgrsCandidate e : c.expand() ) {
					processingQueue.add(e);
				}
			}
		}
		return this.qgrs;
	}
	
	void seedQueue() {
		int g = 2;
		Collection<Integer> starts;
		do {
			starts = getStartingPoints(g);
			for ( Integer start : starts ) {
				this.processingQueue.add(new QgrsCandidate(sequence, g, start)) ;
			}
			g++;
		} while (starts.size() > 0 ) ;
		System.out.println("Detected  " + processingQueue.size() + " starts");
	}
	
	Collection<Integer> getStartingPoints(int numTetrads) {
		String tstr = QgrsCandidate.buildTetradString(numTetrads);
		LinkedList<Integer> starts = new LinkedList<Integer>();
		int p = 0;
		do {
			p = sequence.indexOf(tstr, p);
			if ( p >= 0 ) {
				starts.add(p);
			}
			p++;
		} while ( p > 0 ) ;
		return starts;
	}
}
