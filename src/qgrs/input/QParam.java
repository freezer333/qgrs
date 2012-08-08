package qgrs.input;

import java.util.LinkedList;
import java.util.List;

import framework.web.AbstractParam;


public enum QParam implements AbstractParam {
	
	Sequence1("seq1"), 
	Sequence2("seq2"), 
	Sequence1Chars("seq1_chars"),
	Sequence2Chars("seq2_chars"),
	Sequence1InputOption("seq1Option"),
	Sequence2InputOption("seq2Option"),
	JobId("jobId"),
	SimilarityCutoff("similarityCutoff"),
	GScoreCutoff("gScoreCutoff"),
	TetradCutoff("tetradCutoff"),
	Sequence1StartIndex("seq1StartIndex"),
	Sequence1EndIndex("seq1EndIndex"),
	Sequence2StartIndex("seq2StartIndex"),
	Sequence2EndIndex("seq2EndIndex"),
	OverlapFilter("overlapFilter"),
	PageNumber("pageNumber"), 
	PageSize("pageSize"), 
	AlignmentId("alignmentId"),
	AlertMessage("alertMessage"),
	
	Db_FilterState("dbFilterState", "block"), 
	Db_QgrsId1("dbQgrsId1"), 
	Db_MinTetrads1("dbMinTetrads1", "2"), 
	Db_GScore1("dbGScore1", "35"), 
	Db_Region13UTR("dbRegion13UTR", "on"), 
	Db_Region1CDS("dbRegion1CDS", "on"), 
	Db_Region15UTR("dbRegion15UTR", "on"), 
	Db_QgrsId2("dbQgrsId2"), 
	Db_MinTetrads2("dbMinTetrads2", "2"), 
	Db_GScore2("dbGScore2", "35"), 
	Db_Region23UTR("dbRegion23UTR", "on"), 
	Db_Region2CDS("dbRegion2CDS", "on"), 
	Db_Region25UTR("dbRegion25UTR", "on"), 
	Db_OverlapSimilarity("dbOverlapSimilarity", "0.75"),
	Db_TetradSimilarity("dbTetradSimilarity", "0.75"),
	Db_LoopSimilarity("dbLoopSimilarity", "0.75"),
	Db_TotalLengthSimilarity("dbTotalLengthSimilarity", "0.75"),
	Db_OverallSimilarity("dbOverallSimilarity", "0.90"),
	Db_Species1("dbSpecies1", "Homo sapiens"),
	Db_Species2("dbSpecies2", ""),
	Db_GeneSymbol1("dbGeneSymbol1", ""), 
	Db_GeneSymbol2("dbGeneSymbol2", ""), 
	Db_GeneId1("dbGeneId1", ""),
	Db_GeneId2("dbGeneId2", ""),
	Db_FilterSide("dbFilterSide", ""),
	
	Db_Ontology1("dbOntology1"),
	Db_QGRSNum1("dbQGRSNum1", "1"),
	Db_Ontology2("dbOntology2"),
	Db_QGRSNum2("dbQGRSNum2", "1"),
	Db_MinAlignmentScore("dbMinAlignmentScore", "60"),
	Db_PageNumber("dbPageNumber", "1"),
	Db_TotalResults("dbTotalResults", "1"),
	Db_TotalPages("dbTotalPages", "1")
	;
	
	private String name;
	private final String defaultValue;
	
	QParam(String name) {
		this.name = name;
		this.defaultValue = "";
	}
	QParam(String name, String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}
	public String getDefaultValue() {
		return this.defaultValue;
	}
	
	public static List<AbstractParam> asList() {
		LinkedList<AbstractParam> retval = new LinkedList<AbstractParam>();
		for ( QParam p : QParam.values() ) {
			retval.add(p);
		}
		return retval;
	}
}
