package qgrs.compute.stat.qgrs;

import java.text.DecimalFormat;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class QgrsLocationResults extends DescriptiveStatistics {

	public double getMedian() {
		return getPercentile(50);
	}
	
	@Override 
	public String toString(){
		DecimalFormat f = new DecimalFormat("0.00");
		return "Samples:  " + getN() + 
				"\tSum:  " + f.format(getSum()) + 
				"\tMean:  " + f.format(getMean()) + 
				"\tMedian:  " + f.format(getPercentile(50));
	}
}
