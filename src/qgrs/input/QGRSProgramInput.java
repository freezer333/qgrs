package qgrs.input;

import java.util.List;

import qgrs.data.GeneSequence;
import qgrs.data.InputType;

public class QGRSProgramInput {

	private GeneSequence principle;
	private List<GeneSequence> comparisons;
	private InputType inputType;
	
	
	public GeneSequence getPrinciple() {
		return principle;
	}
	public void setPrinciple(GeneSequence principle) {
		this.principle = principle;
	}
	public List<GeneSequence> getComparisons() {
		return comparisons;
	}
	public void setComparisons(List<GeneSequence> comparisons) {
		this.comparisons = comparisons;
	}
	public void setInputType(InputType inputType) {
		this.inputType = inputType;
	}
	public InputType getInputType() {
		return inputType;
	}
	
}
