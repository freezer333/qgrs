package qgrs.data.mongo.primitives;

import com.mongodb.BasicDBObject;


public class Alignment extends BasicDBObject{

	private int similarityScore;
	private float similarityPercentage;
	

	
	public Alignment() {
		super();
	}
	public int getSimilarityScore() {
		return this.getInt("similarityScore");
	}
	public void setSimilarityScore(int similarityScore) {
		this.put("similarityScore", similarityScore);
	}
	public double getSimilarityPercentage() {
		return getDouble("similarityPercentage");
	}
	public void setSimilarityPercentage(double similarityPercentage) {
		this.put("similarityPercentage", similarityPercentage);
	}
	
	
}
