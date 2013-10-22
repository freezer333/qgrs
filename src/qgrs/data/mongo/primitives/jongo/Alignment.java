package qgrs.data.mongo.primitives.jongo;

import org.jongo.marshall.jackson.oid.Id;

public class Alignment {

	@Id
	String _id;
	private int alignmentPercentage;
	private AlignedSequence principal;
	private AlignedSequence comparison;
	
	public Alignment() {}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public int getAlignmentPercentage() {
		return alignmentPercentage;
	}
	public void setAlignmentPercentage(int alignmentPercentage) {
		this.alignmentPercentage = alignmentPercentage;
	}
	public AlignedSequence getPrincipal() {
		return principal;
	}
	public void setPrincipal(AlignedSequence principal) {
		this.principal = principal;
	}
	public AlignedSequence getComparison() {
		return comparison;
	}
	public void setComparison(AlignedSequence comparison) {
		this.comparison = comparison;
	}
	
	
	
}
