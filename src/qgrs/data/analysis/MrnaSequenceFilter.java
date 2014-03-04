package qgrs.data.analysis;

import java.util.HashMap;

import qgrs.data.mongo.primitives.jongo.MRNA;
import qgrs.data.providers.SequenceProvider;
import qgrs.data.providers.SequenceProvider.Key;

public class MrnaSequenceFilter extends MrnaFilter {

	public double percent;
	final SequenceProvider seqProvider;
	
	public MrnaSequenceFilter(SequenceProvider provider) {
		super();
		this.seqProvider = provider;
	}
	public MrnaSequenceFilter(String name, SequenceProvider provider) {
		super(name);
		this.seqProvider = provider;
	}
	public MrnaSequenceFilter(double perc, String name, SequenceProvider provider) {
		super(name);
		this.seqProvider = provider;
		this.percent = perc;
	}
	public MrnaSequenceFilter(double perc, String name, SequenceProvider provider) {
		super(name);
		this.seqProvider = provider;
		this.percent = perc;
	}
	
	@Override
	public String toString() {
		// TODO Should add a description of the sequence related filtering to the end
		// of what the toString function returns
		return super.toString();
	}
	
	
	@Override
	public boolean acceptable(MRNA mrna) {
		HashMap<Key, Object> data = this.seqProvider.getSequence(mrna.getAccessionNumber());
		
		// Gets the raw sequence data - all characters (unaligned)
		String sequence = (String) data.get(Key.Sequence);
		
		/*  Note - this is a silly example... only accept sequences with at least 1000 G's.
		 *  Matt - this class should be designed so the percentage G's is configurable by 
		 *  passing in a percentage into the constructors.
		 *  
		 *  Note - you'd probably want to restrict your counting to particular regions, based on properties (which you'd need to add to this class)
		 */
		
		int count = 0;
		
		for ( int i = 0; i < sequence.length(); i++ ) {
			if ( sequence.charAt(i) == 'G' ) count++;
		}
		
		// To make count value in count a double
		double numG = count * 1.00;
		
		double perc = numG/sequence.length();
		
		
		if ( perc < this.percent ) return false;
		else return super.acceptable(mrna);
	}
	
	
	
	
}
