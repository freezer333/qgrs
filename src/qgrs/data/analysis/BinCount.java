package qgrs.data.analysis;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class BinCount {

	int total = 0;
	int totalWithG4 =0;
	DescriptiveStatistics stats = new DescriptiveStatistics();
	
	void record(int numG4) {
		this.total++;
		if ( numG4 > 0) this.totalWithG4++;
		stats.addValue(numG4);
	}
	
	double measure() {
		return ((double)this.totalWithG4)/this.total;
	}
	double mean() {
		return this.stats.getMean();
	}
	double median() {
		return this.stats.getPercentile(50);
	}
	double sum() {
		return this.stats.getSum();
	}

	public int getTotal() {
		return total;
	}

	public int getTotalWithG4() {
		return totalWithG4;
	}
}
