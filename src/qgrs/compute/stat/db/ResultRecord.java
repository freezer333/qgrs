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
	
	public ResultRecord (ResultSet rs) throws SQLException {
		total = rs.getInt("total");
		mean = rs.getDouble("mean");
		median = rs.getDouble("median");
		std = rs.getDouble("std");
	}
	
	
	public void writeElement(Element e) {
		e.setAttribute("total",String.valueOf(total));
		e.setAttribute("mean", new DecimalFormat("0.00").format(mean));
		e.setAttribute("median", new DecimalFormat("0.00").format(median));
		e.setAttribute("std", new DecimalFormat("0.000").format(std));
	}
}
