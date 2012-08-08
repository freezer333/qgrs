package qgrs.data;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom.Element;

public class AlignmentRecord {
	private int similarityScore;
	private float similarityPercentage = -1; // Cached
	private Date dateAligned;
	private String alignmentBuildKey;
	private final String principle;
	private final String comparison;
	
	
	
	public String getPrinciple() {
		return principle;
	}

	public String getComparison() {
		return comparison;
	}

	public Element getXmlElement(){
		Element align = new Element("alignment-record");
		align.setAttribute("id", String.valueOf(this.getId()));
		align.setAttribute("alignmentBuildKey", String.valueOf(this.getAlignmentBuildKey()));
		align.addContent(new Element("principle").setText(principle));
		align.addContent(new Element("comparison").setText(comparison));
		align.addContent(new Element("similarityScore").setText(String.valueOf(this.getSimilarityScore())));
		align.addContent(new Element("similarityPercentage").setText(String.valueOf(this.getSimilarityPercentage())));
		align.addContent(new Element("dateAligned").setText(SimpleDateFormat.getDateTimeInstance().format(this.getDateAligned())));
		return align;
	}
	
	public AlignmentRecord(String principle, String comparison) {
		this.principle = principle;
		this.comparison = comparison;
	}
	
	public AlignmentRecord(ResultSet rs) {
		try {
			this.principle = rs.getString("principle");
			this.comparison = rs.getString("comparison");
			this.setAlignmentBuildKey(rs.getString("alignmentBuildKey"));
			this.setSimilarityScore(rs.getInt("similarityScore"));
			this.setSimilarityPercentage((float)rs.getDouble("similarityPercentage"));
			this.setDateAligned(new Date(rs.getDate("dateAligned").getTime()));
		} catch (Exception e) {
			throw new RuntimeException (e);
		}
	}

	public AlignmentRecord(Element xml) {
		
		this.principle = xml.getChild("principle").getText();
		this.comparison = xml.getChild("comparison").getText();
		this.setAlignmentBuildKey(xml.getAttributeValue("alignmentBuildKey"));
		this.setSimilarityScore(Integer.parseInt(xml.getChild("similarityScore").getText()));
		this.setSimilarityPercentage(Float.parseFloat(xml.getChild("similarityPercentage").getText()));
		try {
			this.setDateAligned(SimpleDateFormat.getDateTimeInstance().parse(xml.getChild("dateAligned").getText()));
		} catch (ParseException e) {
			throw new RuntimeException (e);
		}
	}

	public int getSimilarityScore() {
		return similarityScore;
	}
	public void setSimilarityScore(int similarityScore) {
		this.similarityScore = similarityScore;
	}
	public float getSimilarityPercentage() {
		return similarityPercentage;
	}
	public void setSimilarityPercentage(float similarityPercentage) {
		this.similarityPercentage = similarityPercentage;
	}
	public String getId() {
		return this.principle + "x" + this.comparison;
	}
	
	public Date getDateAligned() {
		return dateAligned;
	}
	public void setDateAligned(Date dateAligned) {
		this.dateAligned = dateAligned;
	}
	public String getAlignmentBuildKey() {
		return alignmentBuildKey;
	}
	public void setAlignmentBuildKey(String alignmentBuildKey) {
		this.alignmentBuildKey = alignmentBuildKey;
	}
	
	
	
}
