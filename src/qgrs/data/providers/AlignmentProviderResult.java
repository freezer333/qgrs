package qgrs.data.providers;

public class AlignmentProviderResult {
	public final String gappedPrincipal;
	public final String gappedComparison;
	public final boolean live;
	
	public AlignmentProviderResult(String principal, String comparison, boolean live) {
		super();
		this.gappedPrincipal = principal;
		this.gappedComparison = comparison;
		this.live = live;
	}
	
	
}
