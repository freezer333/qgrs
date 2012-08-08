package qgrs.data;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jdom.Element;


public class QgrsHomology {
	private long numgq1;
	private long numgq2;
	private GQuadruplex gq1;
	private GQuadruplex gq2;
	private double overlapScore;
	private double tetradScore;
	private double avgLoopScore;
	private double totalLengthScore;
	private double overallScore;
	
	public QgrsHomology() {
	
	}
	public QgrsHomology(int gq1Num, GQuadruplex gq1, int gq2Num, GQuadruplex gq2, QgrsHomologyRecord r) {
		this.numgq1 = gq1Num;
		this.numgq2 = gq2Num;
		this.gq1 = gq1;
		this.gq2 = gq2;
		this.overallScore = r.getOverallScore();
		
	}
	public QgrsHomologyRecord buildHomologyRecord(AlignmentRecord r, GeneSequence principle, GeneSequence comparison) {
		QgrsHomologyRecord hr = new  QgrsHomologyRecord();
		hr.setAlignmentId(r.getId());
		hr.setGq1Id(this.gq1.getId());
		hr.setGq2Id(this.gq2.getId());
		
		hr.setP_accessionNumber(principle.getAccessionNumber());
		hr.setP_geneName(principle.getGeneName());
		hr.setP_geneSymbol(principle.getGeneSymbol());
		hr.setP_gScore(this.gq1.getScore());
		hr.setP_in5UTR(this.gq1.in5UTR());
		hr.setP_inCDS(this.gq1.inCDS());
		hr.setP_in3UTR(this.gq1.in3UTR());
		hr.setP_species(principle.getSpecies());
		hr.setP_tetrads(this.gq1.getTetrads());
		
		hr.setC_accessionNumber(comparison.getAccessionNumber());
		hr.setC_geneName(comparison.getGeneName());
		hr.setC_geneSymbol(comparison.getGeneSymbol());
		hr.setC_gScore(this.gq2.getScore());
		hr.setC_in5UTR(this.gq2.in5UTR());
		hr.setC_inCDS(this.gq2.inCDS());
		hr.setC_in3UTR(this.gq2.in3UTR());
		hr.setC_species(comparison.getSpecies());
		hr.setC_tetrads(this.gq2.getTetrads());
				
		hr.setOverallScore(this.getOverallScore());
		
		hr.setAlignmentScore(r.getSimilarityPercentage());
		
		return hr;
	}
	
	public Element getXmlElement(){
		Element result = new Element("result");

		result.setAttribute("num_gq1", String.valueOf(this.getNumgq1()));
		result.setAttribute("num_gq2", String.valueOf(this.getNumgq2()));

		result.setAttribute("gq1_start", String.valueOf(this.getGq1().getStart().getIndexWithoutGaps()));
		result.setAttribute("gq2_start", String.valueOf(this.getGq2().getStart().getIndexWithoutGaps()));
		result.setAttribute("gq1_end", String.valueOf(this.getGq1().getEnd().getIndexWithoutGaps()));
		result.setAttribute("gq2_end", String.valueOf(this.getGq2().getEnd().getIndexWithoutGaps()));

		result.setAttribute("gq1_startDisplay", String.valueOf(this.getGq1().getStart().getIndexWithoutGaps()+1));
		result.setAttribute("gq2_startDisplay", String.valueOf(this.getGq2().getStart().getIndexWithoutGaps()+1));
		result.setAttribute("gq1_endDisplay", String.valueOf(this.getGq1().getEnd().getIndexWithoutGaps()+1));
		result.setAttribute("gq2_endDisplay", String.valueOf(this.getGq2().getEnd().getIndexWithoutGaps()+1));

		result.setAttribute("gq1_glist", this.getGq1().getGListWithGaps());
		result.setAttribute("gq2_glist",  this.getGq2().getGListWithGaps());

		result.setAttribute("gq1_start_gap", String.valueOf(this.getGq1().getStart().getIndexWithGaps()));
		result.setAttribute("gq2_start_gap", String.valueOf(this.getGq2().getStart().getIndexWithGaps()));
		result.setAttribute("gq1_end_gap", String.valueOf(this.getGq1().getEnd().getIndexWithGaps()));
		result.setAttribute("gq2_end_gap", String.valueOf(this.getGq2().getEnd().getIndexWithGaps()));


		result.setAttribute("gq1_score", String.valueOf(this.getGq1().getScore()));
		result.setAttribute("gq2_score", String.valueOf(this.getGq2().getScore()));
		result.setAttribute("gq1_tetrad", String.valueOf(this.getGq1().getTetrads()));
		result.setAttribute("gq2_tetrad", String.valueOf(this.getGq2().getTetrads()));
		result.setAttribute("gq1_region", this.getGq1().getRegion());
		result.setAttribute("gq2_region", this.getGq2().getRegion());

		result.setAttribute("overlap_score", scoreString(this.getOverlapScore()));
		result.setAttribute("loop_score", scoreString(this.getAvgLoopScore()));
		result.setAttribute("length_score", scoreString(this.getTotalLengthScore()));
		result.setAttribute("tetrad_score", scoreString(this.getTetradScore()));
		result.setAttribute("score", scoreString(this.getOverallScore()));

		
		return result;
	}

	String scoreString(double d) {
		NumberFormat format = new DecimalFormat("0.000");
		return format.format(d);
	}

	public long getNumgq1() {
		return numgq1;
	}

	public void setNumgq1(long numgq1) {
		this.numgq1 = numgq1;
	}

	public long getNumgq2() {
		return numgq2;
	}

	public void setNumgq2(long numgq2) {
		this.numgq2 = numgq2;
	}

	public GQuadruplex getGq1() {
		return gq1;
	}

	public void setGq1(GQuadruplex gq1) {
		this.gq1 = gq1;
	}

	public GQuadruplex getGq2() {
		return gq2;
	}

	public void setGq2(GQuadruplex gq2) {
		this.gq2 = gq2;
	}

	public double getOverlapScore() {
		return overlapScore;
	}

	public void setOverlapScore(double overlapScore) {
		this.overlapScore = overlapScore;
	}

	public double getTetradScore() {
		return tetradScore;
	}

	public void setTetradScore(double tetradScore) {
		this.tetradScore = tetradScore;
	}


	public double getAvgLoopScore() {
		return avgLoopScore;
	}

	public void setAvgLoopScore(double avgLoopScore) {
		this.avgLoopScore = avgLoopScore;
	}

	public double getTotalLengthScore() {
		return totalLengthScore;
	}

	public void setTotalLengthScore(double totalLengthScore) {
		this.totalLengthScore = totalLengthScore;
	}

	public double getOverallScore() {
		return overallScore;
	}

	public void setOverallScore(double overallScore) {
		this.overallScore = overallScore;
	}



}
