/**
 * 
 */
package qgrs.utils.db;

import java.io.PrintWriter;

public class InputPair {
	public String principle;
	public String comparison;
	public InputPair(String principle, String comparison) {
		this.principle = principle;
		this.comparison = comparison;
	}
	public void write(PrintWriter pw) {
		pw.println(principle + "\t"+comparison);
	}
}