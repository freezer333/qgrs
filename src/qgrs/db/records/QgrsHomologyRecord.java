package qgrs.db.records;

import java.sql.ResultSet;

import org.jdom.Element;

public class QgrsHomologyRecord {
	private int id;
	private String alignmentId;
	private String gq1Id;
	private String gq2Id;
	private double overallScore;
	
	/*  These fields are redundent, but their presence allows for simplified and fast searches*/
	private String p_accessionNumber;
	private String p_geneName;
	private String p_geneSymbol; 
	private String p_species;
	private int p_tetrads; 
	private int p_gScore; 
	private boolean p_in5UTR;
	private boolean p_inCDS;
	private boolean p_in3UTR;
	private String c_accessionNumber; 
	private String c_geneName;
	private String c_geneSymbol; 
	private String c_species;
	private int c_tetrads; 
	private int c_gScore; 
	private boolean c_in5UTR;
	private boolean c_inCDS;
	private boolean c_in3UTR;
	private double alignmentScore;
	
	
	

	public QgrsHomologyRecord() {
		
	}
	
	public QgrsHomologyRecord(Element xml) {
		this.id = Integer.parseInt(xml.getChildText("id"));
		this.alignmentId = xml.getChildText("alignmentId");
		this.gq1Id = xml.getChildText("gq1Id");
		this.gq2Id = xml.getChildText("gq2Id");
		this.overallScore = Double.parseDouble(xml.getChildText("overallScore"));
		
		
		this.p_accessionNumber = xml.getChildText("p_accessionNumber");
		this.p_geneName = xml.getChildText("p_geneName");
		this.p_geneSymbol = xml.getChildText("p_geneSymbol"); 
		this.p_species = xml.getChildText("p_species");
		this.p_tetrads = Integer.parseInt(xml.getChildText("p_tetrads")); 
		this.p_gScore = Integer.parseInt(xml.getChildText("p_gScore")); 
		this.p_in5UTR = Boolean.parseBoolean(xml.getChildText("p_in5UTR"));
		this.p_inCDS = Boolean.parseBoolean(xml.getChildText("p_inCDS"));
		this.p_in3UTR = Boolean.parseBoolean(xml.getChildText("p_in3UTR"));
		this.c_accessionNumber = xml.getChildText("c_accessionNumber"); 
		this.c_geneName = xml.getChildText("c_geneName");
		this.c_geneSymbol = xml.getChildText("c_geneSymbol"); 
		this.c_species = xml.getChildText("c_species");
		this.c_tetrads = Integer.parseInt(xml.getChildText("c_tetrads")); 
		this.c_gScore = Integer.parseInt(xml.getChildText("c_gScore")); 
		this.c_in5UTR = Boolean.parseBoolean(xml.getChildText("c_in5UTR"));
		this.c_inCDS = Boolean.parseBoolean(xml.getChildText("c_inCDS"));
		this.c_in3UTR = Boolean.parseBoolean(xml.getChildText("c_in3UTR"));
		this.alignmentScore = Double.parseDouble(xml.getChildText("alignmentScore"));
		
	}
	
	public QgrsHomologyRecord(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.alignmentId = rs.getString("alignmentId");
			this.gq1Id = rs.getString("gq1Id");
			this.gq2Id = rs.getString("gq2Id");
			this.overallScore = rs.getDouble("overallScore");
			
			
			this.p_accessionNumber = rs.getString("p_accessionNumber");
			this.p_geneName =rs.getString("p_geneName");
			this.p_geneSymbol = rs.getString("p_geneSymbol"); 
			this.p_species = rs.getString("p_species");
			this.p_tetrads = rs.getInt("p_tetrads"); 
			this.p_gScore = rs.getInt("p_gScore"); 
			this.p_in5UTR = rs.getBoolean("p_in5UTR");
			this.p_inCDS = rs.getBoolean("p_inCDS");
			this.p_in3UTR = rs.getBoolean("p_in3UTR");
			this.c_accessionNumber = rs.getString("c_accessionNumber"); 
			this.c_geneName = rs.getString("c_geneName");
			this.c_geneSymbol = rs.getString("c_geneSymbol"); 
			this.c_species = rs.getString("c_species");
			this.c_tetrads = rs.getInt("c_tetrads"); 
			this.c_gScore = rs.getInt("c_gScore"); 
			this.c_in5UTR = rs.getBoolean("c_in5UTR");
			this.c_inCDS = rs.getBoolean("c_inCDS");
			this.c_in3UTR = rs.getBoolean("c_in3UTR");
			this.alignmentScore = rs.getDouble("alignmentScore");
			
		}
		catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	public Element getXmlElement() {
		Element xml = new Element("homology");
		xml.addContent(new Element("id").setText(String.valueOf(this.id)));
		xml.addContent(new Element("alignmentId").setText(String.valueOf(this.alignmentId)));
		xml.addContent(new Element("gq1Id").setText(String.valueOf(this.gq1Id)));
		xml.addContent(new Element("gq2Id").setText(String.valueOf(this.gq2Id)));
		xml.addContent(new Element("overallScore").setText(String.valueOf(this.overallScore)));
		
		
		xml.addContent(new Element("p_accessionNumber").setText(this.p_accessionNumber));
		xml.addContent(new Element("p_geneName").setText(this.p_geneName));
		xml.addContent(new Element("p_geneSymbol").setText(this.p_geneSymbol)); 
		xml.addContent(new Element("p_species").setText(this.p_species));
		xml.addContent(new Element("p_tetrads").setText(String.valueOf(this.p_tetrads))); 
		xml.addContent(new Element("p_gScore").setText(String.valueOf(this.p_gScore))); 
		xml.addContent(new Element("p_in5UTR").setText(String.valueOf(this.p_in5UTR)));
		xml.addContent(new Element("p_inCDS").setText(String.valueOf(this.p_inCDS)));
		xml.addContent(new Element("p_in3UTR").setText(String.valueOf(this.p_in3UTR)));
		xml.addContent(new Element("c_accessionNumber").setText(this.c_accessionNumber)); 
		xml.addContent(new Element("c_geneName").setText(this.c_geneName));
		xml.addContent(new Element("c_geneSymbol").setText(this.c_geneSymbol)); 
		xml.addContent(new Element("c_species").setText(this.c_species));
		xml.addContent(new Element("c_tetrads").setText(String.valueOf(this.c_tetrads))); 
		xml.addContent(new Element("c_gScore").setText(String.valueOf(this.c_gScore))); 
		xml.addContent(new Element("c_in5UTR").setText(String.valueOf(this.c_in5UTR)));
		xml.addContent(new Element("c_inCDS").setText(String.valueOf(this.c_inCDS)));
		xml.addContent(new Element("c_in3UTR").setText(String.valueOf(this.c_in3UTR)));
		xml.addContent(new Element("alignmentScore").setText(String.valueOf(this.alignmentScore))); 
		
		return xml;
	}
	
	
	public boolean isP_in5UTR() {
		return p_in5UTR;
	}

	public void setP_in5UTR(boolean p_in5UTR) {
		this.p_in5UTR = p_in5UTR;
	}

	public boolean isP_inCDS() {
		return p_inCDS;
	}

	public void setP_inCDS(boolean p_inCDS) {
		this.p_inCDS = p_inCDS;
	}

	public boolean isP_in3UTR() {
		return p_in3UTR;
	}

	public void setP_in3UTR(boolean p_in3UTR) {
		this.p_in3UTR = p_in3UTR;
	}

	public boolean isC_in5UTR() {
		return c_in5UTR;
	}

	public void setC_in5UTR(boolean c_in5UTR) {
		this.c_in5UTR = c_in5UTR;
	}

	public boolean isC_inCDS() {
		return c_inCDS;
	}

	public void setC_inCDS(boolean c_inCDS) {
		this.c_inCDS = c_inCDS;
	}

	public boolean isC_in3UTR() {
		return c_in3UTR;
	}

	public void setC_in3UTR(boolean c_in3UTR) {
		this.c_in3UTR = c_in3UTR;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAlignmentScore() {
		return alignmentScore;
	}

	public void setAlignmentScore(double alignmentScore) {
		this.alignmentScore = alignmentScore;
	}
	
	public String getAlignmentId() {
		return alignmentId;
	}

	public void setAlignmentId(String alignmentId) {
		this.alignmentId = alignmentId;
	}

	public String getGq1Id() {
		return gq1Id;
	}
	public void setGq1Id(String gq1Id) {
		this.gq1Id = gq1Id;
	}
	public String getGq2Id() {
		return gq2Id;
	}
	public void setGq2Id(String gq2Id) {
		this.gq2Id = gq2Id;
	}
	
	public double getOverallScore() {
		return overallScore;
	}
	public void setOverallScore(double overallScore) {
		this.overallScore = overallScore;
	}

	public String getP_accessionNumber() {
		return p_accessionNumber;
	}

	public void setP_accessionNumber(String p_accessionNumber) {
		this.p_accessionNumber = p_accessionNumber;
	}

	public String getP_geneName() {
		return p_geneName;
	}

	public void setP_geneName(String p_geneName) {
		this.p_geneName = p_geneName;
	}

	public String getP_geneSymbol() {
		return p_geneSymbol;
	}

	public void setP_geneSymbol(String p_geneSymbol) {
		this.p_geneSymbol = p_geneSymbol;
	}

	public String getP_species() {
		return p_species;
	}

	public void setP_species(String p_species) {
		this.p_species = p_species;
	}

	public int getP_tetrads() {
		return p_tetrads;
	}

	public void setP_tetrads(int p_tetrads) {
		this.p_tetrads = p_tetrads;
	}

	public int getP_gScore() {
		return p_gScore;
	}

	public void setP_gScore(int p_gScore) {
		this.p_gScore = p_gScore;
	}

	

	public String getC_accessionNumber() {
		return c_accessionNumber;
	}

	public void setC_accessionNumber(String c_accessionNumber) {
		this.c_accessionNumber = c_accessionNumber;
	}

	public String getC_geneName() {
		return c_geneName;
	}

	public void setC_geneName(String c_geneName) {
		this.c_geneName = c_geneName;
	}

	public String getC_geneSymbol() {
		return c_geneSymbol;
	}

	public void setC_geneSymbol(String c_geneSymbol) {
		this.c_geneSymbol = c_geneSymbol;
	}

	public String getC_species() {
		return c_species;
	}

	public void setC_species(String c_species) {
		this.c_species = c_species;
	}

	public int getC_tetrads() {
		return c_tetrads;
	}

	public void setC_tetrads(int c_tetrads) {
		this.c_tetrads = c_tetrads;
	}

	public int getC_gScore() {
		return c_gScore;
	}

	public void setC_gScore(int c_gScore) {
		this.c_gScore = c_gScore;
	}




	
	
	
	
	
}
