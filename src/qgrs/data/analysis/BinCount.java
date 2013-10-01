package qgrs.data.analysis;

public class BinCount {

	int total = 0;
	int totalWithG4 =0;
	
	void signal(boolean hasG4) {
		this.total++;
		if ( hasG4) this.totalWithG4++;
	}
	
	double measure() {
		return ((double)this.totalWithG4)/this.total;
	}

	public int getTotal() {
		return total;
	}

	public int getTotalWithG4() {
		return totalWithG4;
	}
}
