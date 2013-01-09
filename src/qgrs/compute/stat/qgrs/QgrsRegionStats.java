package qgrs.compute.stat.qgrs;

import java.text.DecimalFormat;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class QgrsRegionStats extends DescriptiveStatistics {

	/// we'll probably add more to this, for now, just directly extend the commons math stats class.
	
	@Override 
	public String toString(){
		DecimalFormat f = new DecimalFormat("0.00");
		return "Samples:  " + getN() + "\tSum:  " + f.format(getSum()) + "\tMean:  " + f.format(getMean()) + "\tMedian:  " + f.format(getPercentile(50));
	}
}
