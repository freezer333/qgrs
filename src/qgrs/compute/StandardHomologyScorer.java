package qgrs.compute;

import java.util.LinkedList;
import java.util.List;

import qgrs.data.Base;
import qgrs.data.GQuadruplex;
import qgrs.data.GeneSequence;
import qgrs.data.QgrsHomology;
import qgrs.job.CancelException;
import qgrs.job.CancelFlag;
import qgrs.job.DefaultCancelFlag;
import qgrs.job.JobStage;
import qgrs.job.StatusHolder;

//testing google code on 8/11/11 

public class StandardHomologyScorer implements QgrsHomologyScorer{
	
	private final StatusHolder statusHolder;
	private List<QgrsHomology> similarityResults;
	
	final float minPercent = 0;
	final float maxOverlapPercent = (float) 0.85;
	final float maxPercentLength = (float) 0.60;
	final float maxPercentTetrad = (float) 0.50;
	final float maxPercentLoop = (float) 0.50;
	final float maxPercentLoopavg = (float) 0.50;
	
	final float weightedOverlap = (float) 0.65; 
	final float weightedTetrad = (float) 0.2; 
	final float weightedLoop = (float) 0.1; 
	final float weightedLength = (float) 0.05; 
	
	CancelFlag cancelFlag = new DefaultCancelFlag();
	
	@Override
	public void setCancelFlag(CancelFlag flag) {
		cancelFlag = flag;
	}
	
	// Allocating instance variables instead of stack variables for heavily called function
	
	@Override
	public List<QgrsHomology> getSimilarityResults() {
		return similarityResults;
	}

	public StandardHomologyScorer(StatusHolder statusHolder) {
		this.statusHolder = statusHolder;
		similarityResults = new LinkedList<QgrsHomology>();
	}
	
	// numS1 is 0 for first gquad in sequence 1, 1 for 2nd in S1 
	void compareQuadruplexes(long numS1, GQuadruplex fromS1, long numS2, GQuadruplex fromS2, GeneSequence principle, GeneSequence comparison) {
		Base start1 = fromS1.getStart();
		Base start2 = fromS2.getStart();
		Base end1 = fromS1.getEnd();
		Base end2 = fromS2.getEnd();
		int lengthGaps1 = end1.getIndexWithGaps() - start1.getIndexWithGaps();
		int lengthGaps2 = end2.getIndexWithGaps() - start2.getIndexWithGaps();
		
		float paddedstart1 = fromS1.getStart().getIndexWithGaps();
		float paddedstart2 = fromS2.getStart().getIndexWithGaps();
		float paddedend1 = fromS1.getEnd().getIndexWithGaps();
		float paddedend2 = fromS2.getEnd().getIndexWithGaps();
		
		int padding = 0; 
		if ( lengthGaps1 <= lengthGaps2) {
			padding = lengthGaps1 >> 1;
		}
		else {
			padding = lengthGaps2 >> 1;
		}

		paddedstart1 = start1.getIndexWithGaps() - padding;
		paddedstart2 = start2.getIndexWithGaps() - padding;	
		paddedend1 = end1.getIndexWithGaps() + padding;
		paddedend2 = end2.getIndexWithGaps() + padding;

		float newStart1 = Math.max(0, paddedstart1);
		float newStart2 = Math.max(0, paddedstart2);
		float newEnd1 = Math.min(paddedend1, principle.getBases().size());
		float newEnd2 = Math.min(paddedend2, comparison.getBases().size());		

		float overlap = overlap(newStart1,newStart2,newEnd1,newEnd2)/length(Math.min(newStart1,newStart2),Math.max(newEnd1,newEnd2));
		float overlapScore = overlapScore(minPercent, maxOverlapPercent, overlap);
		if ( overlapScore <= 0.2 ) return;
		
		
		float loop1LengthS1 = fromS1.getLoop1Length();
		float loop2LengthS1 = fromS1.getLoop2Length();
		float loop3LengthS1 = fromS1.getLoop3Length();

		float loop1LengthS2 = fromS2.getLoop1Length(); 
		float loop2LengthS2 = fromS2.getLoop2Length();
		float loop3LengthS2 = fromS2.getLoop3Length();

		float lengthS1 = loop1LengthS1 + loop2LengthS1 + loop3LengthS1 + (fromS1.getTetrads() << 2);
		float lengthS2 = loop1LengthS2 + loop2LengthS2 + loop3LengthS2 + (fromS2.getTetrads() << 2);

		float tetradValueS1 = fromS1.getTetrads() + fromS1.getTetradAdjustmentScore();
		float tetradValueS2 = fromS2.getTetrads() + fromS2.getTetradAdjustmentScore();
		float percentLengthDifference = percentDifference(lengthS1,lengthS2);
		float totalLengthScore = score(minPercent, maxPercentLength, percentLengthDifference);

		float percentTetradDifference = percentDifference(fromS1.getTetrads(), fromS2.getTetrads());
		if ( fromS1.getTetrads() < fromS2.getTetrads() ) { 
			percentTetradDifference = percentDifference(tetradValueS1, fromS2.getTetrads());
		}
		if ( fromS1.getTetrads() > fromS2.getTetrads()) {
			percentTetradDifference = percentDifference(fromS1.getTetrads(), tetradValueS2);
		}

		float tetradScore = score(minPercent, maxPercentTetrad, percentTetradDifference);	

		float percentLoop1Difference = percentDifference(loop1LengthS1,loop1LengthS2);
		float percentLoop2Difference = percentDifference(loop2LengthS1,loop2LengthS2);
		float percentLoop3Difference = percentDifference(loop3LengthS1,loop3LengthS2);
		
		float avgLoopDifference = (percentLoop1Difference + percentLoop2Difference + percentLoop3Difference)/3;
		float avgLoopScore = score(minPercent, maxPercentLoopavg, avgLoopDifference);

		float avgScore = (weightedOverlap * overlapScore) + (weightedTetrad * tetradScore) +
		(weightedLoop * avgLoopScore) + (weightedLength * totalLengthScore);
		
		QgrsHomology qs = new QgrsHomology();
		qs.setNumgq1(numS1);
		qs.setNumgq2(numS2);
		qs.setGq1(fromS1);
		qs.setGq2(fromS2);
		qs.setAvgLoopScore(avgLoopScore);
		qs.setOverlapScore(overlapScore);
		qs.setTetradScore(tetradScore);
		qs.setTotalLengthScore(totalLengthScore);
		qs.setOverallScore(avgScore);
		this.similarityResults.add(qs);
		
	
	}

	

	

	

	//returns number of positions within a start and end value
	float length(float start, float end){
		return end - start + 1;
	}

	//determines the number of overlapping bases
	float overlap(float s1, float s2, float e1, float e2){
		if (s1<=s2 && e1>=e2 && e1>s2) {
			return length(s2,e2);}
		if (s1<=s2 && e1<e2 && e1>s2) {
			return length(s2,e1);}
		if (s1>s2 && e1>=e2 && e2>s1) {
			return length(s1,e2);}
		if (s1>s2 && e1<e2) {
			return length(s1,e1);}
		if (e2<=s1 || s2>=e1) {return 0;}
		return -1;
	}

	//percent difference is the difference between both lengths divided by the length of the maximum strand
	float percentDifference(float valueA, float valueB){
		if (valueA == 0 && valueB == 0){
			return 0;
		}
		return ( (Math.abs(valueA - valueB))/Math.max(valueA, valueB));
	}

	//assigns a score from 0-1 by taking percent difference into account and the highest percentage desired to be incorporated
	float score(float minPercent, float maxPercent, float percentDifference){
		return Math.max(0,(1- (percentDifference)/(maxPercent - minPercent)));
	}

	float overlapScore(float minPercent, float maxPercent, float percentOverlap) {
		return Math.min(1, (percentOverlap)/(maxPercent - minPercent));
	}

	@Override
	public void alignGQuadruplexes(GeneSequencePair pair) throws Exception {
		// TODO Auto-generated method stub
		long s1 = 0;
		long s2 = 0;
		//output method
		double total = pair.getPrinciple().getgQuads().size();
		System.out.println("QGRS Count:  " + pair.getPrinciple().getgQuads().size() + " x " + pair.getComparison().getgQuads().size());
		
		for (GQuadruplex pq : pair.getPrinciple().getgQuads() ){
			s1++;
			s2 = 0;
			
			// for every gq in pair.principle
			for (GQuadruplex cq : pair.getComparison().getgQuads() ) {
				s2++;
				int distance = cq.getStart().getIndexWithGaps() - pq.getStart().getIndexWithGaps();
				if ( distance > 200) break;
				if ( Math.abs(distance) < 200 ) {
					compareQuadruplexes(s1, pq, s2, cq, pair.getPrinciple(), pair.getComparison());
				}
				if ( s2 % 100 == 0) {
					this.statusHolder.setStatus(JobStage.QGRS_Homology,  s1/total, null);
					
				}
				if ( this.cancelFlag.isRaised()) 
					throw new CancelException();
			}
			
			
			
		}

	} 

	
}
