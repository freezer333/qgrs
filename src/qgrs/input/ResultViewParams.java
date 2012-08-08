package qgrs.input;

import org.jdom.Element;

import qgrs.data.QgrsHomology;
import framework.web.AbstractWebContext;
import framework.web.util.StringUtils;

public class ResultViewParams {

	private double similarityCutoff = 0.75;
	private double gScoreCutoff = 17;
	private int pageNumber = 1;
	private int pageSize = 100;
	private int totalRecords;
	private int seq1StartIndex = 0;
	private int seq1EndIndex = Integer.MAX_VALUE;
	private int seq2StartIndex = 0;
	private int seq2EndIndex = Integer.MAX_VALUE;
	private int overlapFilter = 1;
	private int tetradCutoff = 2;
	
	public ResultViewParams() {
	}
	
	public Element getXml(String maxEndIndex) {
		Element root = new Element("viewParams");
		Element simCut = new Element(QParam.SimilarityCutoff.getName());
		Element gScoreCut = new Element(QParam.GScoreCutoff.getName());
		Element tetradCut = new Element(QParam.TetradCutoff.getName());
		Element pageNumberElement = new Element(QParam.PageNumber.getName());
		Element pageSizeElement = new Element(QParam.PageSize.getName());
		Element numPagesElement = new Element("numPages");
		Element totalRecordsElement = new Element("totalRecords");
		
		Element seq1StartIndexElement = new Element(QParam.Sequence1StartIndex.getName());
		Element seq1EndIndexElement = new Element(QParam.Sequence1EndIndex.getName());
		Element seq2StartIndexElement = new Element(QParam.Sequence2StartIndex.getName());
		Element seq2EndIndexElement = new Element(QParam.Sequence2EndIndex.getName());
		Element overlapFilterElement = new Element(QParam.OverlapFilter.getName());
		
		simCut.setText(String.valueOf(similarityCutoff));
		gScoreCut.setText(String.valueOf(gScoreCutoff));
		tetradCut.setText(String.valueOf(tetradCutoff));
		pageNumberElement.setText(String.valueOf(pageNumber));
		pageSizeElement.setText(String.valueOf(pageSize));
		numPagesElement.setText(String.valueOf(this.getNumPages()));
		totalRecordsElement.setText(String.valueOf(this.getTotalRecords()));
		seq1StartIndexElement.setText(String.valueOf(this.seq1StartIndex));
		seq1EndIndexElement.setText(this.seq1EndIndex == Integer.MAX_VALUE ? maxEndIndex : String.valueOf(this.seq1EndIndex));
		seq2StartIndexElement.setText(String.valueOf(this.seq2StartIndex));
		seq2EndIndexElement.setText(this.seq2EndIndex == Integer.MAX_VALUE ? "MAX" : String.valueOf(this.seq2EndIndex));
		overlapFilterElement.setText(String.valueOf(this.overlapFilter));
		
		root.addContent(simCut);
		root.addContent(gScoreCut);
		root.addContent(tetradCut);
		root.addContent(pageNumberElement);
		root.addContent(pageSizeElement);
		root.addContent(numPagesElement);
		root.addContent(totalRecordsElement);
		root.addContent(seq1StartIndexElement);
		root.addContent(seq1EndIndexElement);
		root.addContent(seq2StartIndexElement);
		root.addContent(seq2EndIndexElement);
		root.addContent(overlapFilterElement);
		return root;
	}
	
	public int getTetradCutoff() {
		return tetradCutoff;
	}

	public void setTetradCutoff(int tetradCutoff) {
		this.tetradCutoff = tetradCutoff;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getNumPages() {
		int fullPages = this.totalRecords / this.pageSize;
		return ( this.totalRecords % this.pageSize != 0 ) ? fullPages+1 : fullPages;
	}
	
	
	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public ResultViewParams(AbstractWebContext context) {
		String str = context.getString(QParam.SimilarityCutoff);
		if ( StringUtils.isDefined(str) ) {
			this.similarityCutoff = Double.parseDouble(str);
		}
		
		str = context.getString(QParam.GScoreCutoff);
		if ( StringUtils.isDefined(str) ) {
			this.gScoreCutoff = Double.parseDouble(str);
		}
		
		str = context.getString(QParam.TetradCutoff);
		if ( StringUtils.isDefined(str) ) {
			this.tetradCutoff = Integer.parseInt(str);
		}
		
		str = context.getString(QParam.PageNumber);
		if ( StringUtils.isDefined(str) ) {
			this.pageNumber = Integer.parseInt(str);
			System.out.println("Page number = " + this.pageNumber);
		}
		
		str = context.getString(QParam.PageSize);
		if ( StringUtils.isDefined(str) ) {
			this.pageSize = Integer.parseInt(str);
		}
		
		str = context.getString(QParam.Sequence1StartIndex);
		if ( StringUtils.isDefined(str) ) {
			this.seq1StartIndex = Integer.parseInt(str);
		}
		str = context.getString(QParam.Sequence1EndIndex);
		if ( StringUtils.isDefined(str) ) {
			if ( str.equalsIgnoreCase("MAX")) this.seq1EndIndex = Integer.MAX_VALUE;
			else this.seq1EndIndex = Integer.parseInt(str);
		}
		
		str = context.getString(QParam.Sequence2StartIndex);
		if ( StringUtils.isDefined(str) ) {
			this.seq2StartIndex = Integer.parseInt(str);
		}
		str = context.getString(QParam.Sequence2EndIndex);
		if ( StringUtils.isDefined(str) ) {
			if ( str.equalsIgnoreCase("MAX")) this.seq2EndIndex = Integer.MAX_VALUE;
			else this.seq2EndIndex = Integer.parseInt(str);
		}
		
		str = context.getString(QParam.OverlapFilter);
		if ( StringUtils.isDefined(str) ) {
			this.overlapFilter = Integer.parseInt(str);
		}
		
	}

	public double getSimilarityCutoff() {
		return similarityCutoff;
	}

	public void setSimilarityCutoff(double similarityCutoff) {
		this.similarityCutoff = similarityCutoff;
	}
	
	public double getGScoreCutoff() {
		return gScoreCutoff;
	}
	
	public void setGScoreCutoff(double gScoreCutoff) {
		this.gScoreCutoff = gScoreCutoff;
	}
	
	public boolean isRecordInPage(int recordNumber) {
		int minRec = (pageNumber-1) * pageSize;
		int maxRec = (pageNumber * pageSize);
		return recordNumber >= minRec && recordNumber < maxRec;
	}
	
	public boolean filterQSimilarity(QgrsHomology qs) {
		boolean match = true;
		match = qs.getOverallScore() >= this.getSimilarityCutoff();
		match = match && qs.getGq1().getScore() >= this.getGScoreCutoff();
		match = match && qs.getGq2().getScore() >= this.getGScoreCutoff();
		
		match = match && qs.getGq1().getTetrads() >= this.getTetradCutoff();
		match = match && qs.getGq2().getTetrads() >= this.getTetradCutoff();
		
		// The plus on is here because users thinks of first index as 1, not 0;
		match = match && qs.getGq1().getStart().getIndexWithoutGaps()+1 >= this.seq1StartIndex;
		match = match && qs.getGq2().getStart().getIndexWithoutGaps()+1 >= this.seq2StartIndex;
		match = match && qs.getGq1().getEnd().getIndexWithoutGaps()+1 <= this.seq1EndIndex;
		match = match && qs.getGq2().getEnd().getIndexWithoutGaps()+1 <= this.seq2EndIndex;
		
		return match;
	}

	public int getOverlapFilter() {
		return overlapFilter;
	}

	public void setOverlapFilter(int overlapFilter) {
		this.overlapFilter = overlapFilter;
	}
	
	
}
