package qgrs.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.biojavax.bio.seq.RichSequence;
import org.jdom.Element;


public class GeneSequence implements Serializable{
	private final boolean directInput;
	private String accessionNumber;
	private final Range cds;
	private final String giNumber;
	private final String geneSymbol;
	private final String ncbiLink;
	private final String geneName;
	private final String species;
	private final List<Range> polyASignals;
	private final List<Range> polyASites;
	private final Range utr5;
	private final Range utr3;
	private final RichSequence richSequence;
	
	private List<Base> bases;
	private int sequenceLength; 
	
	private List<GQuadruplex> gQuads;
	private List<GQuadFamily> quadruplexFamilies;
	
	
	
	
	
	
	public Element getXmlElement(){
		Element gene = new Element("gene");
		gene.setAttribute("directInput", String.valueOf(this.isDirectInput()));
		gene.setAttribute("accessionNumber", this.getAccessionNumber());
		gene.setAttribute("sequenceLength", String.valueOf(this.getSequenceLength()));
		if ( !this.isDirectInput() ) {
			gene.addContent(new Element("giNumber").setText(this.giNumber));
			gene.addContent(new Element("geneSymbol").setText(this.geneSymbol));
			gene.addContent(new Element("ncbiLink").setText(this.ncbiLink));
			gene.addContent(new Element("geneName").setText(this.geneName));
			gene.addContent(new Element("species").setText(this.species));
			gene.addContent(this.cds.writeElement(new Element("cds")));
			gene.addContent(this.utr5.writeElement(new Element("utr5")));
			gene.addContent(this.utr3.writeElement(new Element("utr3")));
			Element polyASignalsElement= new Element("polyASignals");
			for ( Range r : this.getPolyASignals() ) {
				polyASignalsElement.addContent(r.writeElement(new Element("range")));
			}
			gene.addContent(polyASignalsElement);
			
			Element polyASitesElement= new Element("polyASites");
			for ( Range r : this.getPolyASites() ) {
				polyASitesElement.addContent(r.writeElement(new Element("range")));
			}
			gene.addContent(polyASitesElement);
		}
		return gene;
	}
	
	
	public static GeneSequence buildFromResultSet(ResultSet rs, List<Range> polyASites, List<Range> polyASignals) {
		try {
			return new GeneSequence(rs, polyASites, polyASignals);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static GeneSequence buildFromXml(Element geneRoot) {
		return new GeneSequence(geneRoot);
	}
	
	public static GeneSequence buildFromDirectInput(String sequence) {
		try {
			return new GeneSequence(sequence);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static GeneSequence buildFromDirectInput(String sequence, String accessionNumber) {
		try {
			GeneSequence retval = new GeneSequence(sequence);
			retval.accessionNumber = accessionNumber;
			return retval;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static GeneSequence buildFromRichSequence(String sequence, RichSequence rs) {
		try {
			return new GeneSequence(sequence, rs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public static GeneSequence buildFromSourceSequence(String sequence, GeneSequence source) {
		try {
			return new GeneSequence(sequence, source.richSequence);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	protected GeneSequence(String sequence) throws Exception {
		this(sequence, null);
	}

	
	protected GeneSequence(ResultSet rs, List<Range> polyASites, List<Range> polyASignals) throws Exception {
		this.directInput = false;
		this.accessionNumber = rs.getString("accessionNumber");
		this.geneName = rs.getString("geneName");
		this.giNumber = rs.getString("giNumber");
		this.geneSymbol = rs.getString("geneSymbol");
		this.ncbiLink = rs.getString("ncbiLink");
		this.species = rs.getString("species");
		this.polyASignals = polyASignals;
		this.polyASites = polyASites;
		this.cds = Range.buildFromResultSet("cds", rs);
		this.utr5 = Range.buildFromResultSet("utr5", rs);			
		this.utr3 = Range.buildFromResultSet("utr3", rs);
		this.sequenceLength = rs.getInt("sequenceLength");
		this.richSequence = null;
	}
	
	protected GeneSequence (Element xml) {
		this.directInput = false;
		this.accessionNumber = xml.getAttributeValue("accessionNumber");
		this.sequenceLength = Integer.parseInt(xml.getAttributeValue("sequenceLength"));
		this.giNumber = xml.getChild("giNumber").getTextTrim();
		this.geneSymbol = xml.getChild("geneSymbol").getTextTrim();
		this.ncbiLink = xml.getChild("ncbiLink").getTextTrim();
		this.geneName = xml.getChild("geneName").getTextTrim();
		this.species = xml.getChild("species").getTextTrim();
		
		this.polyASignals = new LinkedList<Range>();
		if ( xml.getChild("polyASignals") != null) {
			for ( Object o : xml.getChild("polyASignals").getChildren()) {
				if (o instanceof Element) {
					this.polyASignals.add(Range.buildFromElement((Element) o));
				}
			}
		}
		this.polyASites =new LinkedList<Range>();
		if ( xml.getChild("polyASites") != null) {
			for ( Object o : xml.getChild("polyASites").getChildren()) {
				if (o instanceof Element) {
					this.polyASites.add(Range.buildFromElement((Element) o));
				}
			}
		}
		
		this.cds = Range.buildFromElement(xml.getChild("cds"));
		this.utr5 = Range.buildFromElement(xml.getChild("utr5"));			
		this.utr3 = Range.buildFromElement(xml.getChild("utr3"));
		this.richSequence = null;
	}
	
	
	
	protected GeneSequence(String sequence, RichSequence rs) throws Exception {
		this.buildBases(sequence);
		
		this.gQuads = new LinkedList<GQuadruplex>();
		this.quadruplexFamilies = new LinkedList<GQuadFamily>();
		this.richSequence = rs;
		if ( rs == null ) {
			this.directInput = true;
			this.accessionNumber = "N/A";
			this.giNumber = "";
			this.geneSymbol = "";
			this.ncbiLink = "#";
			this.geneName = "";
			this.species = "";
			this.polyASignals = null;
			this.polyASites = null;
			this.cds = null;
			this.utr5 = null;			
			this.utr3 = null;
		}
		else {
			SequenceFeatureAdapter featuresAdapter = new SequenceFeatureAdapter(rs);
			this.directInput = false;
			this.accessionNumber = rs.getAccession() + "." + rs.getVersion();
			this.giNumber = rs.getIdentifier();
			this.geneSymbol = featuresAdapter.getGeneSymbol().toUpperCase();
			this.ncbiLink = "http://www.ncbi.nlm.nih.gov/nuccore/" + accessionNumber;
			this.geneName = rs.getDescription();
			this.species = rs.getTaxon() == null ? "" : rs.getTaxon().getDisplayName();
			
			this.polyASignals = featuresAdapter.getPolyASignals();
			this.polyASites = featuresAdapter.getPolyASites();
			
			this.cds = new Range(featuresAdapter.getCDSStart(), featuresAdapter.getCDSEnd());
			this.utr5 = new Range(1, cds.getStart()-1);			
			this.utr3 = new Range(cds.getEnd()+1, this.sequenceLength);
		}
		
	}
	
	public void buildBases(String sequence) {
		this.bases = new LinkedList<Base>();
		int pureIndex = 0;
		for ( char c : sequence.toCharArray()) {
			if ( Character.isWhitespace(c)) continue;
			Base b = new Base(BaseSymbol.parseSymbol(c));
			if ( b.getSymbol() != BaseSymbol.Gap) {
				b.setIndexWithoutGaps(pureIndex++);
			}
			bases.add(b);
		}
		this.sequenceLength = calculateLengthWithoutGaps();
		if ( this.gQuads != null ) {
			relinkQuadruplexes();
		}
		
	}
	
	private void relinkQuadruplexes() {
		ArrayList<Base> list=  this.buildNoGapList();
		for ( GQuadruplex gq : this.gQuads ) {
			gq.setStart(this.getLiveBase(list, gq.getStart()));
			gq.setTetrad2Start(this.getLiveBase(list, gq.getTetrad2Start()));
			gq.setTetrad3Start(this.getLiveBase(list, gq.getTetrad3Start()));
			gq.setTetrad4Start(this.getLiveBase(list, gq.getTetrad4Start()));
			gq.refreshLinks();
		}
	}
	private ArrayList<Base> buildNoGapList() {
		ArrayList<Base> retval = new ArrayList<Base>();
		for ( Base b : this.bases ) {
			if ( b.getSymbol() != BaseSymbol.Gap) {
				retval.add(b);
			}
		}
		return retval;
	}
	private Base getLiveBase(ArrayList<Base> bases, Base deadBase) {
		return bases.get(deadBase.getIndexWithoutGaps());
	}

	public void groupQuadruplexesIntoFamilies() {
		for ( GQuadruplex q : this.gQuads ) {
			boolean newFamily = true;
			for ( GQuadFamily family : this.quadruplexFamilies ) {
				if ( family.addIfBelongsIn(q)) {
					newFamily = false;
				}
			}
			if ( newFamily ) {
				this.quadruplexFamilies.add(new GQuadFamily(q));
			}
		}
		System.out.println(gQuads.size() + " G-Quadruplexes grouped into " + this.quadruplexFamilies.size() + " families");
	}
	public void filterQuadruplexesForBestFamilyRepresentatives() {
		this.gQuads = new LinkedList<GQuadruplex>();
		for ( GQuadFamily family : this.getQuadruplexFamilies() ) {
			gQuads.add(family.getBest());
		}
	}
	
	public int getIndexOfGQuadruplex(GQuadruplex q) {
		if ( this.gQuads.contains(q)) {
			return this.gQuads.indexOf(q);
		}
		throw new RuntimeException("Quadruplex does not exist in this sequence");
	}
	
	public List<GQuadFamily> getQuadruplexFamilies() {
		return quadruplexFamilies;
	}


	public Range getCdsRange() {
		return cds;
	}


	private int calculateLengthWithoutGaps() {
		int i = 1;
		while (this.getBases().get(this.getBases().size()-i).getSymbol() == BaseSymbol.Gap ) i++;
		return this.getBases().get(this.getBases().size()-i).getIndexWithoutGaps() + 1;
	}
	
	
	public String getAccessionNumber() {
		return accessionNumber;
	}

	
	public List<Base> getBases() {
		return bases;
	}

	public List<GQuadruplex> getgQuads() {
		return gQuads;
	}

	public void setgQuads(List<GQuadruplex> gQuads) {
		this.gQuads = gQuads;
	}

	
	public String getAlignedSequence() {
		return toString();
	}
	public String getPureSequence() {
		StringBuilder builder = new StringBuilder();
		for ( Base g : this.bases) {
			if ( g.getSymbol() != BaseSymbol.Gap) {
				builder.append(g.getSymbol().toString());
			}
		}
		return builder.toString();
	}
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for ( Base g : this.bases) {
			builder.append(g.getSymbol().toString());
		}
		return builder.toString();
	}
	
	
	

	//Inserts indels in the appropriate places so that a sequence with no gaps(before any 
	//alignment) matches an alignedSequence
	public void addGaps(GeneSequence alignedSequenceToMatch)
	{
		int i;
		for(i=0;i<this.getBases().size();i++)
		{
			if(this.getBases().get(i).getSymbol()==alignedSequenceToMatch.getBases().get(i).getSymbol())
			{
				continue;
			}
			else
			{
				Base base=new Base();
				base.setSymbol(BaseSymbol.Gap);
				this.getBases().add(i, base);
			}
		}
		
		for(int j=i;j<alignedSequenceToMatch.getBases().size();j++)
		{
			Base base = new Base();
			base.setSymbol(BaseSymbol.Gap);
			this.getBases().add(base);
		}
		
	}

	public Element getPageXmlElement(String defaultLabel) {
		Element mrna = new Element("mrna");
		mrna.setAttribute("accession", this.getAccessionNumber());
		mrna.setAttribute("length", String.valueOf(this.getSequenceLength()));
		if ( this.getgQuads() != null ) {
			mrna.setAttribute("numQgrs", String.valueOf(this.getgQuads().size()));
		}
		mrna.addContent(this.getInfoElement(defaultLabel));
		return mrna;
	}
	
	
	
	public boolean isDirectInput() {
		return directInput;
	}


	public Range getCds() {
		return cds;
	}


	public String getGiNumber() {
		return giNumber;
	}


	public String getGeneSymbol() {
		return geneSymbol;
	}


	public String getNcbiLink() {
		return ncbiLink;
	}


	public String getGeneName() {
		return geneName;
	}


	public String getSpecies() {
		return species;
	}


	public int getSequenceLength() {
		return sequenceLength;
	}


	public List<Range> getPolyASignals() {
		return polyASignals;
	}


	public List<Range> getPolyASites() {
		return polyASites;
	}


	public Range getUtr5() {
		return utr5;
	}


	public Range getUtr3() {
		return utr3;
	}


	/*"Direct input (Seq. 1)"*/
	public Element getInfoElement(String defaultLabel) {
		Element root = new Element("info");
		if ( this.isDirectInput() ) {
			root.setAttribute("accessionNumber", "Direct input");
			root.setAttribute("giNumber", "N/A");
			root.setAttribute("geneSymbol", defaultLabel);
			return root;
		}
		
		root.setAttribute("accessionNumber", this.getAccessionNumber());
		Element ncbiElement = new Element("ncbiLink");
		ncbiElement.setText(this.getNcbiLink());
		root.addContent(ncbiElement);

		root.setAttribute("giNumber", this.getGiNumber());
		root.setAttribute("geneName", this.getGeneName());
		root.setAttribute("species", this.getSpecies());
		root.setAttribute("geneSymbol", this.getGeneSymbol());
		Element mrnaElement = new Element("mrnaLength");
		mrnaElement.setText(" " + this.getSequenceLength() + " bp");
		root.addContent(mrnaElement);

		Element utr5s = new Element("utr5s");
		utr5s.setText(this.getUtr5().toString());
		root.addContent(utr5s);

		Element cds = new Element("cds");
		cds.setText(this.getCds().toString());
		root.addContent(cds);

		Element utr3s = new Element("utr3s");
		utr3s.setText(this.getUtr3().toString());
		root.addContent(utr3s);

		for (Range pAsig: this.getPolyASignals()){
			Element polyAsignal = new Element("polyAsignal");
			polyAsignal.setText( " " + pAsig.getStart() + " - " + pAsig.getEnd());
			root.addContent(polyAsignal);
		} 

		for (Range pAsites: this.getPolyASites()){
			Element polyAsite = new Element("polyAsite");
			polyAsite.setText(" " + pAsites.getStart());
			root.addContent(polyAsite);
		}

		
		
		return root;
	}



	
	//Used by alignment algorithms in order to construct their matrices.
    public static int[] convertStringToArr(String str)
    {
    	int[] arr = new int[str.length()];
        for (int i = 0; i < str.length(); i++)
        {
        	short n = -1;
            if(str.charAt(i) == 'A')
                n= 0;
            if(str.charAt(i) == 'G')
                n= 1;
            if(str.charAt(i) == 'C')
                n= 2;
            if(str.charAt(i) == 'T')
                n= 3;
            
            arr[i]=n;
        }

        return arr;    
    }

  
    public ArrayList<GeneSequence> makeDeepCopies(int numberOfCopies)
	{
		ArrayList<GeneSequence> copies = new ArrayList<GeneSequence>();
	    ObjectOutputStream oos = null;
	    ObjectInputStream ois = null;
	    
	    for(int i=0;i<numberOfCopies-1;i++)
	    {
		    try
		    {
		       ByteArrayOutputStream bos = new ByteArrayOutputStream();
		       oos = new ObjectOutputStream(bos); 
		       // serialize and pass the object
		       oos.writeObject(this);   
		       oos.flush();              
		       ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray()); 
		       ois = new ObjectInputStream(bin);                  
		       copies.add((GeneSequence) ois.readObject());
		       oos.close();
		       ois.close();
			    
		      }
		      catch(Exception e)
		      {
		         System.out.println("Exception in DeepCopyPrinciple = " + e);
		      }
	    }
	    copies.add(this);
		return copies;
		
	}
    
}
