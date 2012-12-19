/**
 * 
 */
package qgrs.db.tasks;

import java.io.PrintWriter;

public class InputPair {
	public String principle;
	public String comparison;
	public String principleSpecies;
	public String comparisonSpecies;
	
	public InputPair(String principle, String comparison, 
			String principleSpecies, String comparisonSpecies) {
		this.principle = principle;
		this.comparison = comparison;
		this.principleSpecies = principleSpecies;
		this.comparisonSpecies = comparisonSpecies;
	}
	public void write(PrintWriter pw) {
		pw.println(principle + "\t"+comparison);
	}
}