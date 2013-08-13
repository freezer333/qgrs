package qgrs.data;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class GQuadFamily {
	private static int MAX_ENDPOINT_DIFFERENCE = 5;
	private List<GQuadruplex> members = new LinkedList<GQuadruplex>();
	private GQuadruplex best;
	
	public GQuadFamily(GQuadruplex adam) {
		best = adam;
		this.add(adam);
		
	}
	
	public List<GQuadruplex> getMembers() {
		return members;
	}

	private boolean add(GQuadruplex toAdd) {
		if ( toAdd.getScore() > this.best.getScore()) {
			best = toAdd;
		}
		return members.add(toAdd);
	}
	
	public boolean addIfBelongsIn(GQuadruplex candidate) {
		if ( belongsIn(candidate) ) {
			return add(candidate);
		}
		return false;
	}
	
	private boolean belongsIn(GQuadruplex candidate) {
		for ( GQuadruplex a : members) {
			if ( isRelatives(a, candidate) ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isRelatives(GQuadruplex a, GQuadruplex b) {
		int diff = a.getStart().getIndexWithoutGaps() - b.getStart().getIndexWithoutGaps();
		diff += a.getEnd().getIndexWithoutGaps() - b.getEnd().getIndexWithoutGaps();
		return Math.abs(diff/2.0) < MAX_ENDPOINT_DIFFERENCE;
	}

	public Collection<GQuadruplex> getOverlappingMotifs() {
		LinkedList<GQuadruplex> retval = new LinkedList<GQuadruplex>();
		for ( GQuadruplex g : this.members ) {
			if ( !g.getId().equals(this.best.getId())) {
				retval.add(g);
			}
		}
		return retval;
	}
	public GQuadruplex getBest() {
		return best;
	}
	
	
}
