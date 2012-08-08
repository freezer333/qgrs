package qgrs.db;

import java.io.File;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import qgrs.data.AlignmentRecord;
import qgrs.data.GQuadruplex;
import qgrs.data.GeneSequence;
import qgrs.data.QgrsHomology;
import qgrs.data.QgrsHomologyRecord;

public abstract class XmlWriteCache implements Cache {

	
	
	
	protected Element alignmentRecordElement;
	protected Element principleAlignedSequenceElement;
	protected Element comparisonAlignedSequenceElement;
	protected Element principleSequenceElement = null;
	protected Element comparisonSequenceElement = null;
	protected Element principleQuadruplexElement = null;
	protected Element comparisonQuadruplexElement = null;
	protected Element homologyElement = null;
	protected AlignmentRecord alignmentRecord;
		
	abstract void handleXmlResult(Document xmlDoc) ;
	
	@Override
	public void flushAndClose() {
		Document xmlDoc = new Document();
		Element root = new Element("qgrsResultSet");
		root.addContent(this.alignmentRecordElement);
		Element principle = new Element("principle");
		principle.addContent(this.principleSequenceElement);
		principle.addContent(this.principleAlignedSequenceElement);
		principle.addContent(this.principleQuadruplexElement);
		Element comparison = new Element("comparison");
		comparison.addContent(this.comparisonSequenceElement);
		comparison.addContent(this.comparisonAlignedSequenceElement);
		comparison.addContent(this.comparisonQuadruplexElement);
		root.addContent(principle);
		root.addContent(comparison);
		root.addContent(this.homologyElement);
		xmlDoc.addContent(root);
		this.handleXmlResult(xmlDoc);
	}

	
	
	
	
	
	
	
	@Override
	public void put(GeneSequence sequence) {
		String tag = this.principleSequenceElement == null ? "principle" : "comparison";
		
		Element root = sequence.getXmlElement();
		root.setAttribute("tag", tag);
	    if (principleSequenceElement == null ) {
	    	principleSequenceElement = root;
	    }
	    else {
	    	comparisonSequenceElement = root;
	    }
	}
	
	@Override
	public void put(AlignmentRecord alignmentRecord) {
		this.alignmentRecordElement = alignmentRecord.getXmlElement();
		this.alignmentRecord = alignmentRecord;
	}
	
	@Override
	public void put(AlignmentRecord alignmentRecord, GeneSequence sequence) {
		Element root = new Element("alignment").setText(sequence.getAlignedSequence());
	    if ( sequence.getAccessionNumber().equalsIgnoreCase(alignmentRecord.getPrinciple())) {
	    	this.principleAlignedSequenceElement = root;
	    }
	    else {
	    	this.comparisonAlignedSequenceElement = root;
	    }
	}
	
	@Override
	public void putQuadruplexes(GeneSequence s) {
		Element root = new Element("quadruplexes").setAttribute("accessionNumber", s.getAccessionNumber());
	    for ( GQuadruplex gq : s.getgQuads()) {
	    	if ( gq.isAcceptable()) {
	    		root.addContent(gq.getRecord().getXmlElement());
	    	}
	    }
	    if ( s.getAccessionNumber().equalsIgnoreCase(alignmentRecord.getPrinciple())) {
	    	this.principleQuadruplexElement = root;
	    }
	    else {
	    	this.comparisonQuadruplexElement = root;
	    }
	}
	
	@Override
	public void putHomologyResults(AlignmentRecord record,
			List<QgrsHomology> homologyResults, GeneSequence principle, GeneSequence comparison) {
		this.homologyElement = new Element("homologyResults");
	    for ( QgrsHomology h : homologyResults) {
			if ( h.getOverallScore() > CacheConstants.HomologyScoreCutoff ) {
				QgrsHomologyRecord hr = h.buildHomologyRecord(record, principle, comparison);
				homologyElement.addContent(hr.getXmlElement());
			}
		}
	    
	   
	}
	
	
	
	
	@Override
	public List<QgrsHomologyRecord> getHomologyRecords(AlignmentRecord record) {
		return null;
	}

	@Override
	public AlignmentRecord getAlignmentRecord(GeneSequence principle,
			GeneSequence comparison, String alignmentBuildKey) {
		return null;
	}

	

	@Override
	public String getAlignedSequence(AlignmentRecord alignmentRecord,
			GeneSequence sequence) {
		return null;
	}

	

	@Override
	public List<GQuadruplex> getQuadruplexes(GeneSequence s) {
		return null;
	}

	

	
	

	
	
	@Override
	public GeneSequence get(String accessionNumber) {
		return null;
	}

}

