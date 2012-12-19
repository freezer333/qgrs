package qgrs.db.records;

import java.sql.ResultSet;

import org.jdom.Element;

import qgrs.data.GeneSequence;

public class GQuadruplexRecord {
	private String id;
	private int tetrad1;
	private int tetrad2;
	private int tetrad3;
	private int tetrad4;
	private int loop1Length;
	private int loop2Length;
	private int loop3Length;
	private int totalLength;
	private int numTetrads;
	private int score;
	private int distanceFromPolyASignal;
	private boolean in5Prime;
	private boolean inCds;
	private boolean in3Prime;
	private String sequenceSlice;
	private String geneAccessionNumber;
	
	
	public int getDistanceFromPolyASignal() {
		return distanceFromPolyASignal;
	}
	public void setDistanceFromPolyASignal(int distanceFromPolyASignal) {
		this.distanceFromPolyASignal = distanceFromPolyASignal;
	}
	public String getGeneAccessionNumber() {
		return geneAccessionNumber;
	}
	public void setGeneAccessionNumber(String geneSequenceNumber) {
		this.geneAccessionNumber = geneSequenceNumber;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getTetrad1() {
		return tetrad1;
	}
	public void setTetrad1(int tetrad1) {
		this.tetrad1 = tetrad1;
	}
	public int getTetrad2() {
		return tetrad2;
	}
	public void setTetrad2(int tetrad2) {
		this.tetrad2 = tetrad2;
	}
	public int getTetrad3() {
		return tetrad3;
	}
	public void setTetrad3(int tetrad3) {
		this.tetrad3 = tetrad3;
	}
	public int getTetrad4() {
		return tetrad4;
	}
	public void setTetrad4(int tetrad4) {
		this.tetrad4 = tetrad4;
	}
	public int getLoop1Length() {
		return loop1Length;
	}
	public void setLoop1Length(int loop1Length) {
		this.loop1Length = loop1Length;
	}
	public int getLoop2Length() {
		return loop2Length;
	}
	public void setLoop2Length(int loop2Length) {
		this.loop2Length = loop2Length;
	}
	public int getLoop3Length() {
		return loop3Length;
	}
	public void setLoop3Length(int loop3Length) {
		this.loop3Length = loop3Length;
	}
	public int getTotalLength() {
		return totalLength;
	}
	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}
	public int getNumTetrads() {
		return numTetrads;
	}
	public void setNumTetrads(int numTetrads) {
		this.numTetrads = numTetrads;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getSequenceSlice() {
		return sequenceSlice;
	}
	public void setSequenceSlice(String sequenceSlice) {
		this.sequenceSlice = sequenceSlice;
	}
	
	
	
	public boolean isIn5Prime() {
		return in5Prime;
	}
	public void setIn5Prime(boolean in5Prime) {
		this.in5Prime = in5Prime;
	}
	public boolean isInCds() {
		return inCds;
	}
	public void setInCds(boolean inCds) {
		this.inCds = inCds;
	}
	public boolean isIn3Prime() {
		return in3Prime;
	}
	public void setIn3Prime(boolean in3Prime) {
		this.in3Prime = in3Prime;
	}
	public Element getXmlElement() {
		this.applyAssertion();
		Element gq = new Element("quadruplex");
		gq.setAttribute("id", String.valueOf(this.getId()));
		gq.addContent(new Element("geneAccessionNumber").setText(this.getGeneAccessionNumber()));
		gq.addContent(new Element("tetrad1").setText(String.valueOf(this.getTetrad1())));
		gq.addContent(new Element("tetrad2").setText(String.valueOf(this.getTetrad2())));
		gq.addContent(new Element("tetrad3").setText(String.valueOf(this.getTetrad3())));
		gq.addContent(new Element("tetrad4").setText(String.valueOf(this.getTetrad4())));
		
		gq.addContent(new Element("tetrad1_normalized").setText(String.valueOf(this.getTetrad1()-this.getTetrad1()+1)));
		gq.addContent(new Element("tetrad2_normalized").setText(String.valueOf(this.getTetrad2()-this.getTetrad1()+1)));
		gq.addContent(new Element("tetrad3_normalized").setText(String.valueOf(this.getTetrad3()-this.getTetrad1()+1)));
		gq.addContent(new Element("tetrad4_normalized").setText(String.valueOf(this.getTetrad4()-this.getTetrad1()+1)));
		
		gq.addContent(new Element("loop1Length").setText(String.valueOf(this.getLoop1Length())));
		gq.addContent(new Element("loop2Length").setText(String.valueOf(this.getLoop2Length())));
		gq.addContent(new Element("loop3Length").setText(String.valueOf(this.getLoop3Length())));
		gq.addContent(new Element("totalLength").setText(String.valueOf(this.getTotalLength())));
		gq.addContent(new Element("numTetrads").setText(String.valueOf(this.getNumTetrads())));
		gq.addContent(new Element("score").setText(String.valueOf(this.getScore())));
		gq.addContent(new Element("sequenceSlice").setText(this.getSequenceSlice()));
		gq.addContent(new Element("in5Prime").setText(String.valueOf(this.isIn5Prime())));
		gq.addContent(new Element("inCds").setText(String.valueOf(this.isInCds())));
		gq.addContent(new Element("in3Prime").setText(String.valueOf(this.isIn3Prime())));
		gq.addContent(new Element("region").setText(this.getRegionLabel()));
		gq.addContent(new Element("position").setText(this.getPositionLabel()));
		gq.addContent(new Element("distanceFromPolyASignal").setText(getDistanceFromPolyASignlaLabel()));
		return gq;
	}	
	
	public String getPositionLabel() {
		return String.valueOf(this.getTetrad1()) + " - " + String.valueOf(this.getTetrad1() + this.getTotalLength());
	}
	
	public Element getXmlElement(GeneSequence geneSequenceToAttach) {
		this.applyAssertion();
		Element gq = this.getXmlElement();
		gq.addContent(geneSequenceToAttach.getXmlElement());
		return gq;
	}	
	public String getDistanceFromPolyASignlaLabel() {
		if ( this.getDistanceFromPolyASignal() >= Integer.MAX_VALUE ) {
			return "N/A";
		}
		return String.valueOf(this.getDistanceFromPolyASignal());
	}
	public String getRegionLabel() {
		if ( this.in5Prime && ! this.inCds ) {
			return "5' UTR";
		}
		else if ( this.in5Prime && this.inCds  ){
			return "5' UTR / CDS";
		}
		else if ( this.in3Prime && ! this.inCds ) {
			return "3' UTR";
		}
		else if ( this.in3Prime && this.inCds  ){
			return "CDS / 3' UTR";
		}
		else return "CDS";
	}
	public GQuadruplexRecord(Element element) {
		this.id = element.getAttributeValue("id");
		
		this.tetrad1 = Integer.parseInt(element.getChildText("tetrad1"));
		this.tetrad2 = Integer.parseInt(element.getChildText("tetrad2"));
		this.tetrad3 = Integer.parseInt(element.getChildText("tetrad3"));
		this.tetrad4 = Integer.parseInt(element.getChildText("tetrad4"));
		
		this.loop1Length = Integer.parseInt(element.getChildText("loop1Length"));
		this.loop2Length = Integer.parseInt(element.getChildText("loop2Length"));
		this.loop3Length = Integer.parseInt(element.getChildText("loop3Length"));
		
		this.totalLength = Integer.parseInt(element.getChildText("totalLength"));
		this.score = Integer.parseInt(element.getChildText("score"));
		this.numTetrads = Integer.parseInt(element.getChildText("numTetrads"));
		
		this.sequenceSlice = element.getChildText("sequenceSlice");
		this.geneAccessionNumber = element.getChildText("geneAccessionNumber");
		
		
		this.in5Prime = Boolean.parseBoolean(element.getChildText("in5Prime"));
		this.inCds = Boolean.parseBoolean(element.getChildText("inCds"));
		this.in3Prime = Boolean.parseBoolean(element.getChildText("in3Prime"));
		this.applyAssertion();
		try {
			this.distanceFromPolyASignal = Integer.parseInt(element.getChildText("distanceFromPolyASignal"));
		}
		catch (Exception e) {
			this.distanceFromPolyASignal = -1;
		}
	}
	
	public void applyAssertion() {
		if ( !this.in5Prime && !this.in3Prime && !this.inCds ) {
			System.err.println("Assertion Error - gquadruplex in no region!");
			throw new RuntimeException("Assertion Error - gquadruplex in no region!");
		}
	}
	public GQuadruplexRecord() {
		
	}
	public GQuadruplexRecord(ResultSet rs){
		try {
			this.id = rs.getString("id");
			this.tetrad1 = rs.getInt("tetrad1");
			this.tetrad2 = rs.getInt("tetrad2");
			this.tetrad3 = rs.getInt("tetrad3");
			this.tetrad4 = rs.getInt("tetrad4");
			this.loop1Length = rs.getInt("loop1Length");
			this.loop2Length = rs.getInt("loop2Length");
			this.loop3Length = rs.getInt("loop3Length");
			this.totalLength = rs.getInt("totalLength");
			this.score = rs.getInt("gScore");
			this.numTetrads = rs.getInt("numTetrads");
			this.sequenceSlice = rs.getString("sequenceSlice");
			this.geneAccessionNumber = rs.getString("geneId");
			this.in5Prime = rs.getBoolean("in5Prime");
			this.inCds = rs.getBoolean("inCds");
			this.in3Prime = rs.getBoolean("in3Prime");
			this.distanceFromPolyASignal = rs.getInt("distanceFromPolyASignal");
			this.applyAssertion();
		}
		catch ( Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
