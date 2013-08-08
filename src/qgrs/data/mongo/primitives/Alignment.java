package qgrs.data.mongo.primitives;


public class Alignment {

	private int similarityScore;
	private float similarityPercentage;
	
	private long _id;
	
	
	public Alignment() {
		super();
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
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	
	
}
