package qgrs.output;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Element;

import qgrs.compute.GeneSequencePair;
import qgrs.data.Base;
import qgrs.data.BaseSymbol;
import qgrs.data.QgrsHomology;
import qgrs.input.ResultViewParams;

public class XmlResultProcessor extends ResultProcessor {

	Element resultElement;
	int totalRecords = 0;
	int principleLastIndex = Integer.MAX_VALUE;
	private ResultViewParams viewParams;
	private List<GeneSequencePair> _cached_pairs;
	List<QgrsHomology> _cached_similarityResults;

	public ResultViewParams getViewParams() {
		return viewParams;
	}

	public void setViewParams(ResultViewParams viewParams) {
		this.viewParams = viewParams;
	}




	public XmlResultProcessor () {


	}

	
	class BaseAverage {
		int count = 0;
		double totalScore = 0;
		double maxScore = 0;
		public void recordScore(double score) {
			totalScore += score;
			count++;
			if ( score > maxScore ) maxScore = score;
		}
		public double avg() {
			return totalScore / count;
		}
		public double max() {
			return maxScore;
		}
	}

	private Element getHomologyMapElement(GeneSequencePair pair,  List<QgrsHomology> similarityResults) {
		Element root = new Element("homologyMap");
		Element avg = new Element("avg");
		Element max = new Element("max");
		HashMap <Integer, BaseAverage> map = new HashMap<Integer, BaseAverage>();
		for ( QgrsHomology qs : similarityResults) {
			int startIndex = qs.getGq1().getStart().getIndexWithoutGaps();
			int endIndex = startIndex + qs.getGq1().getLength();
			for ( int i = startIndex; i <= endIndex; i++ ) {
				BaseAverage baseAvg = map.get(i);
				if ( baseAvg == null ) {
					baseAvg = new BaseAverage();
					map.put(i, baseAvg);
				}
				baseAvg.recordScore(qs.getOverallScore());
			}
		}
		String valueList = "";
		String maxList = "";
		boolean lastScore0 = false;
		boolean lastMaxScore0 = false;

		for ( Base b: pair.getPrinciple().getBases() ) {
			if ( b.getSymbol() != BaseSymbol.Gap) {

				BaseAverage ba = map.get(b.getIndexWithoutGaps());
				BaseAverage baNext = map.get(b.getIndexWithoutGaps()+1);
				double score = (ba==null?0:ba.avg());
				double maxScore = (ba==null?0:ba.max());
				double nextScore = (baNext==null?0:baNext.avg());
				double nextMaxScore = (baNext==null?0:baNext.avg());

				if ( score > 0 || !lastScore0 || nextScore > 0) {
					valueList += ("[" + b.getIndexWithoutGaps() + ", " + score + "],");
				}


				if ( maxScore > 0 || !lastMaxScore0 || nextMaxScore > 0) {
					maxList += ("[" + b.getIndexWithoutGaps() + ", " + maxScore + "],");
				}

				lastScore0 = score <= 0;
				lastMaxScore0 = maxScore <= 0;
			}
		}
		valueList = valueList.substring(0, valueList.length()-1);
		avg.setText(valueList);
		max.setText(maxList);
		root.addContent(avg);
		root.addContent(max);
		return root;
	}

	@Override
	public void handleResults(List<GeneSequencePair> pairs,  List<QgrsHomology> similarityResults) {
		this._cached_pairs = pairs;
		this._cached_similarityResults = similarityResults;
		this.totalRecords = 0;
		
		this.resultElement = new Element("alignmentResults");
		Element pairResult = this.getPairResultElement(pairs);
		
		List<QgrsHomology> matches = new LinkedList<QgrsHomology>();
		for ( QgrsHomology qs : similarityResults) {
			if ( this.viewParams.filterQSimilarity(qs) ) {
				matches.add(qs);
			}
		}

		pairResult.addContent(this.getHomologyMapElement(pairs.get(0), matches));

		List<QgrsHomology> finalMatches = this.mergeOverlaps(matches);
		
		pairResult.addContent(this.getSequenceViewerElement());
		pairResult.addContent(this.getResultsElement(finalMatches));

		this.resultElement.addContent(pairResult);
		this.resultElement.addContent(this.getCacheElement());
		this.principleLastIndex = pairs.get(0).getPrinciple().getSequenceLength();
	}
	
	Element getCacheElement() {
		Element cacheElement = new Element("cacheStatus");
		cacheElement.addContent(new Element("principleQuadruplexCached").setText(String.valueOf(this.isPrincipleQuadruplexIdCached())));
		cacheElement.addContent(new Element("comparisonQuadruplexCached").setText(String.valueOf(this.isComparisonQuadruplexIdCached())));
		cacheElement.addContent(new Element("alignmentCached").setText(String.valueOf(this.isAlignmentCached())));
		cacheElement.addContent(new Element("homologyCached").setText(String.valueOf(this.isHomologyResultsCached())));
		return cacheElement;
	}
	
	private Element getPairResultElement(List<GeneSequencePair> pairs){
		Element pairResult = new Element("pairResult");
		Element principle = new Element("principle");
		Element comparison = new Element("comparison");

		
		principle.addContent(pairs.get(0).getPrinciple().getPageXmlElement("Direct input (Seq. 1)"));
		comparison.addContent(pairs.get(0).getComparison().getPageXmlElement("Direct input (Seq. 2)"));
		pairResult.setAttribute("alignmentScore", String.valueOf(pairs.get(0).getSimilarityScore()));
		pairResult.setAttribute("alignmentPercent", String.valueOf(pairs.get(0).getSimilarityPercentage()));
		
		pairResult.addContent(principle);
		pairResult.addContent(comparison);
		return pairResult;
	}
	private Element getResultsElement(List<QgrsHomology> finalMatches){
		Element results = new Element("gQuadSimResult");
		for ( QgrsHomology qs : finalMatches ) {
			results.addContent(qs.getXmlElement());
			totalRecords++;
		}
		return results;
	}
	private List<QgrsHomology> mergeOverlaps(List<QgrsHomology> matches) {
		// The matches must now be collated into bins based on the QGRS 1 number.  
		// The bins are sorted, and the overlap cutoff criteria is applied.
		HashMap<Long, List<Double>> bins = new HashMap<Long, List<Double>>();
		for ( QgrsHomology qs : matches ) {
			List<Double> bin = bins.get(qs.getNumgq1());
			if ( bin == null ) {
				bin = new LinkedList<Double>();
				bins.put(qs.getNumgq1(), bin);
			}
			if (! bin.contains(qs.getOverallScore())) {
				bin.add(qs.getOverallScore());
			}
		}
		// each bin now has a list of scores
		HashMap<Long, Double> filterBins = new HashMap<Long, Double>();
		for ( Long key : bins.keySet() ) {
			Collections.sort(bins.get(key));
			Collections.reverse(bins.get(key));
			// the bin is now sorted.
			int t = Math.min(bins.get(key).size(), this.viewParams.getOverlapFilter()) - 1;
			filterBins.put(key, bins.get(key).get(t));
		}

		List<QgrsHomology> finalMatches = new LinkedList<QgrsHomology>();
		for ( QgrsHomology qs : matches ) {
			if ( filterBins.get(qs.getNumgq1()) <= qs.getOverallScore() ) {
				finalMatches.add(qs);
			}
		}
		
		return finalMatches;
	}
	
	Element getSequenceViewerElement(){
		// Build the Sequence Section Elements
		Element sequence = new Element("sequence");
		int index = 0;
		final int sectionWidth = 30;
		GeneSequencePair pair = this._cached_pairs.get(0);
		Element sequenceSection = null;
		String principleString = "", comparisonString = "", matchString = "";
		boolean firstP = true;
		boolean firstC = true;
		boolean lastAdded = false;
		while ( index < pair.getPrinciple().getBases().size()) {
			lastAdded = false;
			Base p = pair.getPrinciple().getBases().get(index);
			Base c = null;
			if ( index < pair.getComparison().getBases().size() ) {
				c = pair.getComparison().getBases().get(index);
			}
			if ( index % sectionWidth == 0 ) {
				sequenceSection = new Element("section");
				principleString = "";
				comparisonString = "";
				matchString = "";
				firstP = true;
				firstC = true;
			}
			if ( firstP && p.getSymbol() != BaseSymbol.Gap ) {
				sequenceSection.setAttribute("principle_start",String.valueOf(p.getIndexWithoutGaps()));
				sequenceSection.setAttribute("principle_startDisplay",String.valueOf(p.getIndexWithoutGaps()+1));
				firstP = false;
			}
			if ( c != null && firstC && c.getSymbol() != BaseSymbol.Gap) {
				sequenceSection.setAttribute("comparison_start",String.valueOf(c.getIndexWithoutGaps()));
				sequenceSection.setAttribute("comparison_startDisplay",String.valueOf(c.getIndexWithoutGaps()+1));
				firstC = false;
			}
			principleString+=p.getSymbol().toString();
			comparisonString += (c== null ? " " : c.getSymbol().toString());
			matchString += (c != null && p.getSymbol() == c.getSymbol() ? "|" : " ");
			index++;
			if ( index % sectionWidth == 0 ) {
				sequenceSection.addContent(new Element("principleSection").setText(principleString));
				sequenceSection.addContent(new Element("matchSection").setText(matchString));
				sequenceSection.addContent(new Element("comparisonSection").setText(comparisonString));
				sequence.addContent(sequenceSection);
				lastAdded = true;
			}

		}
		if ( !lastAdded) {
			sequenceSection.addContent(new Element("principleSection").setText(principleString));
			sequenceSection.addContent(new Element("matchSection").setText(matchString));
			sequenceSection.addContent(new Element("comparisonSection").setText(comparisonString));
			sequence.addContent(sequenceSection);
		}
		return sequence;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public int getPrincipleLastIndex() {
		return this.principleLastIndex;

	}

	public Element getXmlElement() {
		this.handleResults(this._cached_pairs, this._cached_similarityResults);
		return this.resultElement;
	}

}
