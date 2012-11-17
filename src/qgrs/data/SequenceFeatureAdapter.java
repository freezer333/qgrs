package qgrs.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.FeatureHolder;
import org.biojavax.bio.seq.RichSequence;


public class SequenceFeatureAdapter {

	private static final String SOURCE = "source";
	private static final String CDS = "CDS";
	/*private static final String GENE = "gene";
	private static final String EXON = "exon";
	private static final String MISC_FEATURE = "misc_feature";
	private static final String STS = "STS";*/
	private static final String POLY_A_SIGNAL = "polyA_signal";
	private static final String POLY_A_SITE = "polyA_site";

	RichSequence rs;
	
	public SequenceFeatureAdapter(RichSequence rs) {
		this.rs = rs;
	}
	public int getSequenceLength(){
		int length = 0;

		for (Feature f:rs.getFeatureSet()){

			if (f.getTypeTerm().getName().equals(SOURCE)){
				length = f.getLocation().getMax();
			}
		}
		return length;
	}

	public int getCDSStart(){

		int cdsStart = 0;

		for (Feature f:rs.getFeatureSet()){
			if (f.getTypeTerm().getName().equals(CDS)){
				cdsStart = f.getLocation().getMin();
			}
		}
		return cdsStart;
	}

	public int getCDSEnd(){

		int cdsEnd = 0;

		for (Feature f:rs.getFeatureSet()){
			if (f.getTypeTerm().getName().equals(CDS)){
				cdsEnd = f.getLocation().getMax();
			}
		}
		return cdsEnd;
	}

	public List<Range> getPolyASignals(){
		List<Range> polyAsignalsList = new LinkedList<Range>();

		for (Feature f:rs.getFeatureSet()){
			if (f.getTypeTerm().getName().equals(SequenceFeatureAdapter.POLY_A_SIGNAL)){
				Range r = new Range(f.getLocation().getMin(), f.getLocation().getMax());
				polyAsignalsList.add(r);
			}
		}
		return polyAsignalsList;
	}

	public List<Range> getPolyASites(){
		List<Range> polyAsitesList = new LinkedList<Range>();

		for (Feature f:rs.getFeatureSet()){
			if (f.getTypeTerm().getName().equals(SequenceFeatureAdapter.POLY_A_SITE)){
				Range r = new Range(f.getLocation().getMin(), f.getLocation().getMax());
				polyAsitesList.add(r);
			}
		}
		return polyAsitesList;
	}
	public String getGeneSymbol() {
		String retval = getFeatureAnnotation("biojavax:gene", this.rs);
		if (retval == null ) {
			return this.rs.getAccession();
		}
		else {
			return retval;
		}
	}
	
	private String getFeatureAnnotation(String keyName, FeatureHolder fh)
	{
	    for (Iterator i = fh.features(); i.hasNext(); ) {
	        Feature f = (Feature) i.next();
	        
	        for ( Object key : f.getAnnotation().asMap().keySet() ) {
	        	if (key.toString().equalsIgnoreCase(keyName)) {
	        		return (String) f.getAnnotation().asMap().get(key);
	        	}
	        }
	    }
	    return null;
	}
	
	/** Helper function for interrogating all featues in a gene. Recursive */
	public void printFeatures(FeatureHolder fh,String prefix)
	{
	    for (Iterator i = fh.features(); i.hasNext(); ) {
	        Feature f = (Feature) i.next();
	        System.out.println("<<");
		System.out.print(prefix);
		System.out.print(f.getType());
		System.out.print(" at ");
		System.out.print(f.getLocation().toString());
		System.out.println();
		for ( Object key : f.getAnnotation().asMap().keySet() ) {
			System.out.println(key + ":  " + f.getAnnotation().asMap().get(key));
		}
		printFeatures(f, prefix + "    ");
	    }
	}
	
}
