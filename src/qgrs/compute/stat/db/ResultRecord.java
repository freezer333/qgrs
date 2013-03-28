package qgrs.compute.stat.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.jdom.Element;


public class ResultRecord {
	
	public final int total;
	public final double mean;
	public final double median;
	public final double std;
	public final double n_mean;
	public final double n_median;
	public final double n_std;
	public final int numSamples;
	public final int numSamplesWithQgrs;
	public final double percentSamplesWithQgrs;
	
	public ResultRecord (ResultSet rs) throws SQLException {
		total = rs.getInt("total");
		mean = rs.getDouble("mean");
		median = rs.getDouble("median");
		std = rs.getDouble("std");
		numSamples = rs.getInt("numSamples");
		numSamplesWithQgrs = rs.getInt("numSamplesWithQgrs");
		n_mean = rs.getDouble("n_mean");
		n_median = rs.getDouble("n_median");
		n_std = rs.getDouble("n_std");	
		percentSamplesWithQgrs = ((double)numSamplesWithQgrs)/numSamples;
	}
	
	
	public void writeElement(Element e) {
		e.setAttribute("total",String.valueOf(total));
		e.setAttribute("mean", new DecimalFormat("0.0000").format(mean));
		e.setAttribute("median", new DecimalFormat("0.0000").format(median));
		e.setAttribute("std", new DecimalFormat("0.0000").format(std));
		e.setAttribute("n_mean", new DecimalFormat("0.0000").format(n_mean));
		e.setAttribute("n_median", new DecimalFormat("0.0000").format(n_median));
		e.setAttribute("n_std", new DecimalFormat("0.0000").format(n_std));
		e.setAttribute("numSamples",String.valueOf(numSamples));
		e.setAttribute("numSamplesWithQgrs",String.valueOf(numSamplesWithQgrs));
		e.setAttribute("percentSamplesWithQgrs",new DecimalFormat("0.0%").format(percentSamplesWithQgrs));
	}
}
