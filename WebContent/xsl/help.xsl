<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" version="5.0" indent="yes"/>
	<xsl:variable name="imagePath" select="'../images/'"/>
	<xsl:variable name="cssPath" select="'../css/'"/>
	<xsl:variable name="jsPath" select="'../javascript/'"/>
	
	<xsl:include href="utils.xsl"/>
	<xsl:include href="header.xsl"/>
	
	
	<xsl:template name="help_main">
		<xsl:call-template name="header">
			<xsl:with-param name="title">: Help Page</xsl:with-param>
			<xsl:with-param name="gotodb">true</xsl:with-param>
			<xsl:with-param name="gotopredictor">true</xsl:with-param>
			<xsl:with-param name="appname"></xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="help_quickstart"/>
		<xsl:call-template name="help_qgrs"/>
		<xsl:call-template name="help_homologyScore"/>
		<xsl:call-template name="help_gScore"/>
		<xsl:call-template name="help_homologyMap"/>
		<xsl:call-template name="help_sequenceViewer"/>
		<xsl:call-template name="help_results"/>
		<xsl:call-template name="help_glossary"/>
		<!-- <xsl:call-template name="help_references"/> -->
	</xsl:template>
	
	
	<xsl:template name="help_quickstart">
		<div class="messageInsert rounded-corners">
		<h1>Help Contents</h1>
		<ul>
			<li><a href="#help_quickstart">QGRS-H Predictor Quickstart Guide</a></li>
			<li><a href="#help_quickstartDb">QGRS-H Database Quickstart Guide</a></li>
			<li><a href="#help_qgrs">QGRS Definition</a></li>
			<li><a href="#help_overlaps">Dealing with Overlapping QGRS</a></li>
			
			<li><a href="#help_gScore">QGRS G-Score Details</a></li>
			<li><a href="#help_homologyScore">QGRS Homology Scoring Details</a></li>
			<li><a href="#help_homologyMap">Understanding the QGRS Homology Map</a></li>
			<li><a href="#help_sequenceViewer">Understanding the Sequence Viewer</a></li>
			<li><a href="#help_results">Understanding the Results Table</a></li>
			<li><a href="#help_glossary">Glossary</a></li>
		</ul>
		</div>
		<a name="help_quickstart"/>
		<div class="messageInsert rounded-corners">
		
		<h1>QGRS-H Predictor - Quickstart Guide</h1>
		<p style="margin-bottom:0;">
		The goal of <b>QGRS-H Predictor</b> is to map and analyze phylogenetically conserved putative <b>Q</b>uadruplex forming '<b>G</b>'-<b>R</b>ich <b>S</b>equences (QGRS) in the mRNAs, ncRNAs and other nucleotide sequences -e.g. Promoter and Telomeric and gene flank regions. The putative <a href="#glossary_entry_G-quadruplex">G-quadruplexes</a> are identified using the following motif:
		</p>
		<p style="margin:0; text-align:center">
		<img src="{$imagePath}motif.png"/>
		</p>
		<p style="margin-top:0;">
		Where x = # guanine tetrads in the G-quadruplex and y1, y2, y3 = length of gaps 
		</p>
		<h2>Entering Sequence Input</h2>
		<img src="{$imagePath}sequence_input.png"/>
		<p>
		The program requires you to enter two mRNA sequences, a <a href="#glossary_entry_Principle-mRNA">principal</a> and <a href="#glossary_entry_Comparison-mRNA">comparison</a>.  Typically, these genes should be known to be homologous, where often
		the <a href="#glossary_entry_Principle-mRNA">principal mRNA</a> will be the human sequence and the <a href="#glossary_entry_Comparison-mRNA">comparison</a> is from another species.  The program accepts two modes of input:
		<ul>
			<li>Accession / GI Number:  You may enter an accession or mRNA GI number and the program will download the sequences from <a href="http://www.ncbi.nlm.nih.gov/" target="_blank">NCBI</a> automatically</li>
			<li>Raw / FASTA:  If you select Raw or FASTA you will be given a larger text box (click the button to reveal the text button) where you can directly enter the genetic sequence.  When entering FASTA format, the program will always use the sequence data entered, but it will also download meta data about the sequence using the provided accession number.</li>
		</ul>
		You may select any method of entry for each of the inputs - they do not need to be the same.
		</p>
		<p>
		Alternatively, if you are only interested in learning about the program, you may choose to run one of the sample homologous pairs listed in the bottom
		part of the input page.
		<img src="{$imagePath}pair_input.png" style="width:100%;"/>
		</p>
		<h2>Data Processing</h2>
		<img src="{$imagePath}processing.png"/>
		<p>
		Once you click on the "Run Analysis" button data processing will begin.  Depending on the length of the sequences you've entered this process can
		take anywhere from 1-10 minutes.  During this step, you will see progress information explaining the steps being taken (see above).  The program
		first identifies <a href="#glossary_entry_QGRS">QGRS</a> instances in each sequence independently ( See below for more detail), then performs a <a href="#glossary_entry_Semi-GlobalAlignment">Semi-Global Alignment</a> to relate the two sequences.  The last step involves
		computing a <a href="#glossary_entry_HomologyScore">Homology Score</a>( See below for more detail) for every pair of <a href="#glossary_entry_QGRS">QGRS</a> instance across the two sequences.
		</p>
		<h2>Results Page</h2>
		<img src="{$imagePath}all.png"  style="width:100%;"/>
		<p>
		Once analysis is complete you will be presented with a results page consisting of 4 main areas:
		</p>
		<h2>Sequence Information</h2>
		<img src="{$imagePath}results-details.png" style="width:100%;"/>
		<p>Additional information about the mRNA sequences is automatically retrieved from <a href="http://www.ncbi.nlm.nih.gov/" target="_blank">NCBI</a> and displayed in the details section (made visible by clicking
		on the "Details" button on the top right).  This information is only available for mRNA sequences entered with their accession #,  GI # or 
		FASTA - this information is not displayed if the sequence is entered in its RAW form.</p>
		
		<h2>QGRS Homology Map</h2>
		<img src="{$imagePath}homology_map.png" style="width:100%;"/>
		<p>The QGRS Homology Map shows you a view of the principal (sequence 1) gene 
		sequence where the vertical bars indicate locations where <a href="#glossary_entry_G-quadruplex">G-quadruplexes</a> have been found 
		that have homologous <a href="#glossary_entry_G-quadruplex">G-quadruplexes</a> in the <a href="#glossary_entry_Comparison-mRNA">comparison</a> gene (sequence 2).  </p>
		The height of the bar indicates the degree of similarity (the <a href="#help_homologyScore">QGRS Homology Score</a>). 
		More details can be found <a href="#help_homologyMap">below</a>.
		
		<h2>Sequence Viewer</h2>
		<img src="{$imagePath}sequenceview.png" style="width:100%;"/>
		<p>The sequence viewer shows the actual base symbols of both the <a href="#glossary_entry_Principle-mRNA">principal</a> and <a href="#glossary_entry_Comparison-mRNA">comparison</a> mRNAs with <a href="#glossary_entry_Semi-GlobalAlignment">Semi-Global Alignment</a> applied.  The <a href="#glossary_entry_Principle-mRNA">principal</a> sequence is always shown above the <a href="#glossary_entry_Comparison-mRNA">comparison</a> sequence.  
		More details can be found <a href="#help_sequenceViewer">below</a>.
		</p>
		
		<h2>Results Table</h2>
		<img src="{$imagePath}results-table.png" style="width:100%;"/>
		<p>The result table lists each <a href="#glossary_entry_QGRS">QGRS</a> pairing within the region selected in the QGRS Homology Map (and meets the filter conditions).
		Clicking on a row will cause the <a href="#glossary_entry_QGRS">QGRS</a> pair to be highlighted and shown in the sequence viewer.  More details can be found <a href="#help_results">below</a>.
		</p>
		
		
		<h2>Getting More Help</h2>
		<p>Wherever the "i"<span style="float:left" class="context_help ui-icon ui-icon-info"/> button is displayed, clicking it will reveal context-specific help information.  
		At any time you can refer back to the remainder of this page for more detailed information.</p>
		
		
		
		</div>
		
		<a name="help_quickstartDb"/>
		<div class="messageInsert rounded-corners">
		
		<h1>QGRS-H Database - Quickstart Guide</h1>
		<p style="margin-bottom:0;">
			QGRS-H DB contains comprehensive information on the composition and 
			distribution of putative homologous G-quadruplexes (QGRS) in semi-globally 
			aligned homologous mRNA sequences. QGRS-H DB can be searched by 
			mRNA and QGRS criteria.
		</p>
		<h2>mRNA Criteria</h2>
		<img src="{$imagePath}mrna-criteria.png" style="width:50%;"/>
		<p>
		The mRNA search criteria allows you to set filters on the 
		<a href="#glossary_entry_Principle-mRNA">principal</a>
		 and/or <a href="#glossary_entry_Comparison-mRNA">comparison mRNAs</a> to be compared. Typically, these nucleotide sequences are known to be homologous, where the principal mRNA will always be the human sequence and the comparison is an established homolog.  The results of a search can be narrowed by using the filters:
		<ul>
			<li><b>mRNA ID</b> (NCBI Accession / GI Number #)</li>
			<li><b>Gene Symbol</b> </li>
			<li><b>Organism</b> </li>
			<li><b>Minimum Sequence Similarity</b></li> 
		</ul>
		Available <a href="#glossary_entry_mrnaId">mRNA IDs</a> and 
		<a href="#glossary_entry_geneSymbol">gene symbols</a> in 
		the database can be browsed by clicking on the appropriate mRNA tabs in 
		the results (keeping ALL the search fields empty).
		</p>
		
		<h2>QGRS Criteria</h2>
		<img src="{$imagePath}qgrs-criteria.png" style="width:50%;"/>
		<p>
		The QGRS search criteria allows you to set filters on the
		 principal (human) or comparison (non-human homolog) QGRS to be 
		 compared. The results of a search can be narrowed by using the filters:
		<ul>
			<li><b>QGRS ID</b></li>
			<li><b>Minimum # of  Tetrads</b> </li>
			<li><b>Minimum G-Score</b> </li>
			<li><b>In the mRNA Region (<a href="#glossary_entry_5UTR">5’ UTR</a>, <a href="#glossary_entry_CDS">CDS</a>, or <a href="#glossary_entry_3UTR">3’ UTR</a>)</b></li>
			<li><b>Minimum Homology</b></li>
			<li><b>Homology Score</b></li> 
		</ul>
		QGRS IDs available in the database can be browsed by clicking on the 
		appropriate QGRS tabs in the results (keeping ALL the search fields empty). 
		</p>
		<h3 style="text-align:center">Interpreting the Results</h3>
		<p>
		The results will appear below the search criteria fields, organized 
		
		into six tables: mRNA (Principal), mRNA (Comparison), mRNA Homologs, 
		QGRS (Principal), QGRS (Comparison), and QGRS Homologous Pairs. 
		 By default, mRNA Homologs matching the search criteria will be 
		 displayed. Click on the appropriate tab to view the corresponding results.
		</p>
		
		<h2>mRNA (Principal)/mRNA (Comparison) Tables</h2>
		<img src="{$imagePath}mrna-listing.png" style="width:90%;"/>
		<p>
		These tables display important characteristics of the principal 
		mRNAs (or Comparison mRNAs) matching the search criteria, and are 
		interactive. Clicking on an appropriate mRNA ID will display more 
		information about the mRNA: including its length, respective positions
		 of <a href="#glossary_entry_5UTR">5’-UTR</a>, <a href="#glossary_entry_CDS">CDS</a>, <a href="#glossary_entry_3UTR">3’-UTR</a> and poly-A Signals/Sites, if available from NCBI. Clicking on a QGRS Count value will lead to the corresponding set of predicted G-quadruplexes in QGRS (Principal) or QGRS (Comparison) tables as appropriate. Clicking on an mRNA Homologs value will lead to the set of its homolog comparisons in the mRNA Homologs table. 
		</p>
		
		<h2>mRNA Homologs Table</h2>
		<img src="{$imagePath}mrna-homolog-listing.png" style="width:90%;"/>
		<p>
		This table displays information about homologous mRNA pairs matching 
		the search criteria. In addition to the basic characteristics of each mRNA
		 in the pair, an overview of the computational analysis results, including 
		 percent <a href="#glossary_entry_sequenceSimilarity">sequence similarity</a> 
		 between the two mRNAs, total number of QGRS in each mRNA and total 
		 number of QGRS homologous in the two mRNAs is also presented. Clicking on 
		 the appropriate QGRS value will lead to the corresponding set of predicted 
		 G-quadruplexes in QGRS (Principal), QGRS (Comparison), or QGRS Homologous 
		 Pairs tables as appropriate. The homologous mRNA pair can be directly 
		 submitted to our previously published <a href="start">QGRS-H Predictor</a> 
		 software for detailed comprehensive analysis of evolutionarily conserved 
		 QGRS by clicking on the "Detailed Analysis" link".
		</p>
		
		<h2>QGRS (Principle)/QGRS (Comparison)</h2>
		<img src="{$imagePath}qgrs-listing.png" style="width:90%;"/>
		<p>
		These tables display important characteristics of computed QGRS in the 
		Principal mRNAs (or Comparison mRNAs) matching the search criteria. 
		The tables show values for <a href="#glossary_entry_qgrsId">QGRS ID</a> (a derivative of the associated mRNA ID), 
		<a href="#glossary_entry_geneSymbol">Gene symbol</a>, and organism. 
		The tables also show several QGRS properties 
		including the <a href="#glossary_entry_G-Score">G-Score</a>, 
		# <a href="#glossary_entry_G-Tetrad">Tetrads</a>, and mRNA Region in which the QGRS 
		is mapped. The actual QGRS sequence motif is shown with G-tracts of the 
		tetrads underlined. The nt Position refers to the beginning location 
		of the QGRS in its parent mRNA sequence.  
		</p>
		
		<h2>QGRS Homologous Pairs</h2>
		<img src="{$imagePath}qgrs-homolog-listing.png" style="width:100%;"/>
		<p>
		This table displays important characteristics of computed homologous 
		QGRS pairs in homologous mRNAs matching the search criteria. The table 
		shows values for QGRS ID pairs, Gene symbol(s), and organism(s) for the 
		corresponding homologous mRNA pair. The table also shows several 
		properties of the Homologous QGRS pair, including the G-Scores, 
		# Tetrads, 
		and mRNA Regions in which the QGRS are mapped. The actual Homologous 
		QGRS sequence motifs are shown with <a href="#glossary_entry_G-Tract">G-tracts</a>
		 of the tetrads underlined. 
		The <a href="#glossary_entry_ntPosition">nt Position</a> refers to the 
		locations of the QGRS in their respective parent mRNA sequences.  
		This table also displays the Homology Score which evaluates a pair 
		of QGRS on two aligned homologous nucleotide sequences for their 
		evolutionary conservation.
		</p>
				
		</div>
		
	</xsl:template>
	
	<xsl:template name="help_qgrs">
		<a name="help_qgrs"/>
		<div class="messageInsert rounded-corners">
		<h1>QGRS Definition</h1>
		<p>
		Quadruplex forming 'G'-Rich Sequences (QGRS; predicted <a href="#glossary_entry_G-quadruplex">G-quadruplexes</a>) 
		are computationally mapped in aligned eukaryotic mRNA homologs using the 
		following motif: </p>
		<p style="margin:0; text-align:center">
		<img src="{$imagePath}motif.png"/>
		</p>
		<p>
		Here x = # guanine <a href="#glossary_entry_G-Tetrad">tetrads</a> in 
		the <a href="#glossary_entry_G-quadruplex">G-quadruplex</a> and y1, y2, 
		y3 = length of <a href="#glossary_entry_GapLoop">gaps</a> (that is, the length 
		of the loops connecting the guanine tetrads). The motif consists of four 
		equal length tracts of guanines, separated by arbitrary nucleotide 
		sequences, with the following restrictions. </p>
		<ul>
			<li>
			The above formula for a QGRS motif can theoretically allow for a G-quadruplex with ten tetrads. However, in reality, G-quadruplexes with more than six tetrads have generally not been observed in naturally occurring nucleotide sequences. Our QGRS prediction method puts a maximum limit of six tetrads on a 45 base length QGRS (i.e., x ≤ 6).
			</li>
			<li>
			For G-quadruplexes with three or more tetrads, only QGRS of maximum length of 45 bases are considered. This restriction on the length of the sequences being considered is in agreement with literature.   
			</li>
			<li>
			G-quadruplexes with only two tetrads are restricted to a maximum length of 30 bases. 
			</li>
			<li>
			Also, at most one of the <a href="#glossary_entry_GapLoop">loops</a> is allowed to be of zero length.
			</li>
		</ul>
		<p>
		This table shows some examples of valid QGRS. The guanine groups which form the tetrads are underlined.  
		</p>
		<p style="margin:0; text-align:center">
		<img src="{$imagePath}gtable.png"/>
		</p>
		<p>The first sequence has two tetrads, even though three of the <a href="#glossary_entry_G-Tract">G-groups</a> could have included another G (since all G-groups must be equal in size). The length of second loop is zero in the first sequence (at the most one gap is allowed to be of zero length). The second sequence is notable for the significant differences in the size of its loops. The third sequence has four tetrads and equal length gaps. This would seem to provide the most stable G-quadruplex</p>
		
		<a name="help_overlaps"/>
		<h2>Dealing with Overlapping QGRS</h2>
		<p>
		Two <a href="#glossary_entry_QGRS">QGRS</a> are said to overlap if their 
		positions in the nucleotide sequence overlap. <a href="#glossary_entry_OverlappingQGRS">Overlapping G-quadruplexes</a> 
		may differ in total length, <a href="#glossary_entry_GapLoop">loop</a> sizes and loop sequences. These sequence 
		arrangements have the potential to form intramolecular quadruplexes in the 
		cell, many of which may be stable.  
		</p>
		<p><b>Example 1:</b> The following table shows a small sample of the 24676 overlaps 
		found in the human GREB1 gene. The first sequence starts at position 40356 in
		 intron 4 of the gene.  
		</p>
		<p style="margin:0; text-align:center">
		<img src="{$imagePath}overlap1.png" style="width:45%"/>
		</p>
		<p>
		From the indicated overlapping sequences, the bolded sequence, i.e., the highest scoring one is more likely to form a relatively stable 
		<a href="#glossary_entry_G-quadruplex">G-quadruplex</a>.
		</p>
		<p>The following table shows a small sample of the 24676 overlaps found in 
		the human GREB1 gene. The first sequence starts at position 40356 in intron 
		4 of the gene. 
		</p>
		<p>
		<b>Example 2</b>: <sup>8729</sup>GGGGGAGAGGACAGGGAGGAGGGATTGG<sup>8756</sup> 
		Mapped near alternative splice site of the Mouse Guanylyl Cyclase-B (Npr2) gene (NCBI Accession # AH013048), the above sequence can theoretically form 173 <a href="#glossary_entry_OverlappingQGRS">overlapping G-quadruplexes</a>. Some (out of 173) potential overlapping arrangements from the above example:  
		</p>
		<p style="margin:0; text-align:center">
		<img src="{$imagePath}overlap2.png" style="width:40%"/>
		</p>
		<p>Out of all the possible arrangements, the highest scoring <a href="#glossary_entry_QGRS">QGRS</a> is more likely to form a relatively stable <a href="#glossary_entry_G-quadruplex">G-quadruplex</a>.</p>
		</div>
	</xsl:template>
	
	
	<xsl:template name="help_gScore">
		<a name="help_gScore"/>
		<div class="messageInsert rounded-corners">
		<h1>QGRS G-Score Details</h1>
		<p>We have used our previously devised scoring system <a href="http://nar.oxfordjournals.org/content/36/suppl_1/D141.abstract?ct=ct">(Kikin et. al., 2006)</a>  that evaluates a <a href="#glossary_entry_QGRS">QGRS</a> for its likelihood to form a stable <a href="#glossary_entry_G-quadruplex">G-quadruplex</a>. Higher scoring sequences will make better candidates for <a href="#glossary_entry_G-quadruplex">G-quadruplexes</a>. The scoring method uses the following principles, which are based on published work. 
		<ul>
		<li>Shorter <a href="#glossary_entry_GapLoop">loops</a> are more common than longer <a href="#glossary_entry_GapLoop">loops</a>.</li>
		<li><a href="#glossary_entry_G-quadruplex">G-quadruplexes</a> tend to have <a href="#glossary_entry_GapLoop">loops</a> roughly equal in size.</li>
		<li>The greater the number of guanine <a href="#glossary_entry_G-Tetrad">tetrads</a>, the more stable the <a href="#glossary_entry_G-quadruplex">G-quadruplex</a></li>
		</ul> 
		Using these precepts, the first sequence in following table should have the largest score. The scoring system assigns a nonnegative integer, which we call the <a href="#glossary_entry_G-Score">G-score</a>, to a <a href="#glossary_entry_QGRS">QGRS</a>. 
		</p>
		<p style="margin:0; text-align:center">
		<img src="{$imagePath}score.png"/>
		</p>
		<p>
		<b>Note:</b><br/>
        The computed G-scores are dependent on the user selected maximum QGRS length. 
        The highest possible G-score, using the default maximum QGRS length of 45, 
        and other restrictions as explained in the QGRS Definition (link appropriately), 
        is 180. Here is an example of a sequence attaining that score: 
        <span style="color:blue">GGGGGG</span>NNNNNNN<span style="color:blue">GGGGGG</span>NNNNNNN<span style="color:blue">GGGGGG</span>NNNNNNN<span style="color:blue">GGGGGG</span>.
		
		</p>
		</div>
	</xsl:template>
	
	<xsl:template name="help_homologyScore">
		<a name="help_homologyScore"/>
		<div class="messageInsert rounded-corners">
		<h1>QGRS Homology Scoring Details</h1>
		<p>The Homology Score which evaluates a pair of QGRS on two aligned homologous 
		nucleotide sequences for their evolutionary conservation. The biological roles of 
		G-quadruplexes are expected to involve structure and specific location within the 
		gene rather than just their sequence. The G-quadruplex structure is primarily dependent 
		on the G-tetrads and loop lengths. The determinants of G-quadruplex homology are 
		expected to be similarities in their specific locations on the aligned transcripts, 
		number of tetrads, loop lengths and overall lengths. Consequently, the QGRS Homology 
		score calculation is based on these parameters.
		</p>
		<p>
		After performing a Semi-Global Pairwise Sequence Alignment based on Needleman-Wunsch, 
		the percentage of overlap between pairs of QGRS on the aligned nucleotide sequences is 
		calculated and weighted at 65% of the overall Homology score. The G-tetrad similarity 
		in the two QGRS being analyzed makes up 20% of the homology score. The loop length 
		similarity compares the length of each loop of the two G-quadruplexes, respectively, 
		and the average of these values is weighted at 10% of the homology score. Finally, 
		the total lengths of the two G-quadruplexes are compared and make up the last 5% of 
		the homology score. The result is a homology score between 0 and 1, where 1 indicates 
		a very high level of evolutionary conservation.
		</p>
		<p style="margin:0; text-align:center">
		<img src="{$imagePath}piescore.png" style="width:50%"/>
		</p>
		<hr/>
		<h3>Example 1</h3>
		<p>
		The QGRS pair below has highly conserved location, number of tetrads, overall length and reasonably conserved loop lengths, resulting in a very high homology score. (G-tetrads are shown in bold face).
		</p>
		<p>
		<pre style="margin-right:3em;font-size:14pt;float:left">
<b>GGG</b>AAC<b>GGG</b>AGC<b>GGG</b>TT<b>GGG</b>
||||| ||||| ||||||||
<b>GGG</b>AAT<b>GGG</b>AA-<b>GGG</b>TT<b>GGG</b>
		</pre>
		<table>
			<tr>
				<td style="padding-right:1em;font-weight:bold">Homology Score</td>
				<td style="padding-right:1em;font-weight:bold">0.974</td>
			</tr>
			<tr>
				<td>Overlap score</td>
				<td>1.00</td>
			</tr>
			<tr>
				<td>Tetrad score</td>
				<td>1.00</td>
			</tr>
			<tr>
				<td>Loop score</td>
				<td>0.78</td>
			</tr>
			<tr>
				<td style="padding-right:1em">Total Length score</td>
				<td>0.917</td>
			</tr>
		</table>
		</p>
		<hr/>
		<h3>Example 2</h3>
		<p>
		<table style="margin-right:3em;float:left">
			<tr>
				<td style="padding-right:1em;font-weight:bold">Homology Score</td>
				<td style="padding-right:1em;font-weight:bold">0.893</td>
			</tr>
			<tr>
				<td>Overlap score</td>
				<td>1.0</td>
			</tr>
			<tr>
				<td>Tetrad score</td>
				<td>0.67</td>
			</tr>
			<tr>
				<td>Loop score</td>
				<td>0.72</td>
			</tr>
			<tr>
				<td style="padding-right:1em">Total Length score</td>
				<td>0.75</td>
			</tr>
		</table>
		<pre style="font-size:14pt;">
		<p>While the QGRS instances below have highly conserved locations, they vary in number of tetrads, overall length and loop lengths – resulting in a lower homology score. (G-tetrads are shown in bold face).</p>
<b>GGG</b>TT<b>GGG</b>A-----AC<b>GGG</b>TT<b>GGG</b>
|| |||| |     ||||||||||
<b>GG</b>ATT<b>GG</b>AAATAAGACG<b>GG</b>TTG<b>GG</b>
		</pre>
		</p>
		
				<hr/>
		<h3>Example 3</h3>
		<p>
		The QGRS instances below have highly conserved tetrads; however they vary in loop lengths and overall location – again contributing to a lower homology score. (G-tetrads are shown in bold face).
		</p>
		<p>
		<pre style="margin-right:3em;font-size:14pt;float:left">
GAGTT-<b>GGG</b>ACGAGTC<b>GGG</b>TT<b>GGG</b>CC-<b>GGG</b>
| ||| |||||| ||||||||| ||| ||
<b>GGG</b>TTA<b>GGG</b>AC<b>GGG</b>TC<b>GGG</b>TTGCGCCTGGA
		</pre>
		<table>
			<tr>
				<td style="padding-right:1em;font-weight:bold">Homology Score</td>
				<td style="padding-right:1em;font-weight:bold">0.84</td>
			</tr>
			<tr>
				<td>Overlap score</td>
				<td>0.83</td>
			</tr>
			<tr>
				<td>Tetrad score</td>
				<td>1.00</td>
			</tr>
			<tr>
				<td>Loop score</td>
				<td>0.62</td>
			</tr>
			<tr>
				<td style="padding-right:1em">Total Length score</td>
				<td>0.71</td>
			</tr>
		</table>
		</p>
		
		</div>
	</xsl:template>
	
	
	<xsl:template name="help_homologyMap">
		<a name="help_homologyMap"/>
		<div class="messageInsert rounded-corners">
		<h1>Understanding the QGRS Homology Map</h1>
		<p>The QGRS Homology Map shows you a view of the <a href="#glossary_entry_Principle-mRNA">principal</a> (sequence 1) gene 
		sequence where the vertical bars indicate locations where <a href="#glossary_entry_G-quadruplex">G-quadruplexes</a> have been found 
		that have homologous <a href="#glossary_entry_G-quadruplex">G-quadruplexes</a> in the <a href="#glossary_entry_Comparison-mRNA">comparison</a> gene (sequence 2).  </p>
		The height of the bar indicates the degree of similarity (the <a href="#glossary_entry_HomologyScore">QGRS Homology Score</a>). 
		<h2>Avg / Max Series</h2> 
		<p>The blue section indicates the average <a href="#glossary_entry_HomologyScore">homology score</a> assigned to <a href="#glossary_entry_G-quadruplex">G-quadruplex</a> pairs at that particular 
		location within the sequence, while the orange section represents the maximum score of homologous 
		<a href="#glossary_entry_G-quadruplex">G-quadruplex</a> pairs found at that location.</p>
		<h2>Selecting a range</h2>
		You can zoom in on a particular range within the <a href="#glossary_entry_Principle-mRNA">principal mRNA</a> sequence by highlighting the region of interest.
		<p style="margin:0; text-align:center">
		<img src="{$imagePath}zooming.png" style="width:90%"/>
		</p>
		<h2>Filter Parameters</h2>
		<p>Use the filter sliders shown below to control which <a href="#glossary_entry_QGRS">QGRS</a> pairs are mapped and reported in the results table.
		</p>
		<p style="margin:0; text-align:center">
		<img src="{$imagePath}filter.png" style="width:90%"/>
		</p>
		</div>
	</xsl:template>
	
	<xsl:template name="help_homologyLegend">
		<a name="help_homologyLegend"/>
		<div class="messageInsert rounded-corners">
		<h1>QGRS Homology Map Details</h1>
		The height of the chart series indicates the degree of similarity (the <a href="#glossary_entry_HomologyScore">QGRS Homology Score</a>) between homologous <a href="#glossary_entry_QGRS">QGRS</a> instances found at the corresponding location.  The <b>blue section indicates the average <a href="#glossary_entry_HomologyScore">homology score</a> </b>assigned to <a href="#glossary_entry_G-quadruplex">G-quadruplex</a> pairs at that particular location within the sequence, while the <b>orange section represents the maximum score</b> of homologous <a href="#glossary_entry_G-quadruplex">G-quadruplex</a> pairs found at that location.
		
		</div>
	</xsl:template>
	
	<xsl:template name="help_sequenceViewer">
		<a name="help_sequenceViewer"/>
		<div class="messageInsert rounded-corners">
		<h1>Understanding the Sequence Viewer</h1>
		<p>The sequence viewer shows the actual base symbols of both the <a href="#glossary_entry_Principle-mRNA">principal</a> and <a href="#glossary_entry_Comparison-mRNA">comparison</a> mRNAs with <a href="#glossary_entry_Semi-GlobalAlignment">Semi-Global Alignment</a> applied.  The <a href="#glossary_entry_Principle-mRNA">principal</a> sequence is always shown above the <a href="#glossary_entry_Comparison-mRNA">comparison</a> sequence.  
		</p>
		<img src="{$imagePath}sequenceview.png" style="width:100%;"/>
		<p>This view is scrollable, so you can view any location - however it is best navigated by clicking on results in the table below it.  When clicking on a homologous <a href="#glossary_entry_QGRS">QGRS</a> pair, the sequence viewer will scroll to the location of the pair and highlight the corresponding <a href="#glossary_entry_QGRS">QGRS</a> instances in both sequences.
		</p>
		</div>
	</xsl:template>
	
	<xsl:template name="help_results">
		<a name="help_results"/>
		<div class="messageInsert rounded-corners">
		<h1>Understanding the Results Table</h1>
		The results table contains a listing of all the <a href="#glossary_entry_QGRS">QGRS</a> pairings across the <a href="#glossary_entry_Principle-mRNA">principal</a> and <a href="#glossary_entry_Comparison-mRNA">comparison</a> mRNA sequences that fall within the range (selected region) and 
		filter settings (minimum <a href="#glossary_entry_HomologyScore">QGRS Homology Score</a>, <a href="#glossary_entry_G-Tetrad">tetrad</a>, and <a href="#glossary_entry_G-Score">G-Score</a>) set in the QGRS Homology Map.  Clicking on a row will
		highlight the <a href="#glossary_entry_QGRS">QGRS</a> pair in the sequence view above, and automatically scroll it into view.
		<p style="text-align:center">
		<img src="{$imagePath}results.png" style="width:95%"/>
		</p>
		<p>The table columns provide the following information:</p>
		<p style="margin-left:210px; position:relative">
		<span style="width:200px;font-weight:bold;position:absolute;left:-210px">Pair ID</span>
		The Pair ID is a composite of two integers which uniquely identify the corresponding <a href="#glossary_entry_G-quadruplex">G-quadruplex</a> within the mRNA sequence.  The first
		integer refers to the ID for the <a href="#glossary_entry_QGRS">QGRS</a> in the <a href="#glossary_entry_Principle-mRNA">principal mRNA</a>, the second integer (after the -) refers to the ID of the <a href="#glossary_entry_QGRS">QGRS</a> in the 
		<a href="#glossary_entry_Comparison-mRNA">comparison mRNA</a>.
		</p>
		
		<p style="margin-left:210px; position:relative">
		<span style="width:200px;font-weight:bold;position:absolute;left:-210px">mRNA</span>
		Displays the mRNA name and species for <a href="#glossary_entry_Principle-mRNA">principal</a> and <a href="#glossary_entry_Comparison-mRNA">comparison</a> mRNA sequences.  If the sequences were entered as RAW data
		then this information is unavailable.
		</p>
		
		<p style="margin-left:210px; position:relative">
		<span style="width:200px;font-weight:bold;position:absolute;left:-210px">Region</span>
		Displays the region each <a href="#glossary_entry_QGRS">QGRS</a> was found within the mRNA - 5' UTR, 5' UTR/CDS, CDS, CDS/3' UTR, or 3' UTR.  If the sequences were entered as RAW data
		then this information is unavailable.
		</p>
		
		<p style="margin-left:210px; position:relative">
		<span style="width:200px;font-weight:bold;position:absolute;left:-210px">Position</span>
		Displays the position/index of the start and end of the <a href="#glossary_entry_QGRS">QGRS</a> within the given sequence (without the <a href="#glossary_entry_GapLoop">gaps</a> that are added during <a href="#glossary_entry_Semi-GlobalAlignment">alignment</a>)
		</p>
		
		<p style="margin-left:210px; position:relative">
		<span style="width:200px;font-weight:bold;position:absolute;left:-210px">Tetrads</span>
		Displays the number of <a href="#glossary_entry_G-Tetrad">tetrads</a> within the <a href="#glossary_entry_G-quadruplex">G-quadruplexes</a> shown.  2 -> GG, 3 -> GGG, 4 -> GGGG, etc.
		</p>
		
		<p style="margin-left:210px; position:relative">
		<span style="width:200px;font-weight:bold;position:absolute;left:-210px">G-Score</span>
		Displays the <a href="#glossary_entry_G-Score">G-Score</a> for the <a href="#glossary_entry_G-quadruplex">G-quadruplexes</a> shown, as described in the <a href="#help_gScore">help section</a>.
		</p>
		
		<p style="margin-left:210px; position:relative">
		<span style="width:200px;font-weight:bold;position:absolute;left:-210px">Homology Score</span>
		Displays the <a href="#glossary_entry_G-Score">G-Score</a> for the <a href="#glossary_entry_G-quadruplex">G-quadruplexes</a> shown, as described in the <a href="#help_homologyScore">help section</a>.
		</p>
		</div>
	</xsl:template>
	
	<xsl:template name="help_glossary">
		<a name="help_glossary"/>
		<div class="messageInsert rounded-corners">
		<h1>Glossary</h1>
		
		<xsl:for-each select="/qgrs/glossary/entry">
			<xsl:sort select="name"/> 
			<p style="margin-left:210px; position:relative"><a name="glossary_entry_{key}"><span style="width:200px;font-weight:bold;position:absolute;left:-210px"><xsl:value-of select="name"/></span></a>
			<xsl:value-of select="full"/>
			</p>
		</xsl:for-each>
		</div>
	</xsl:template>
	
	<xsl:template name="help_references">
		<a name="help_references"/>
		<div class="messageInsert rounded-corners">
		<h1>References</h1>
		<ul>
		<li>Kikin, O., D'Antonio, L. and Bagga, P. (2006) QGRS Mapper: a web-based server for predicting G-quadruplexes in nucleotide sequences Nucleic Acids Res. 34 (Web Server issue):W676-W682.</li>
		</ul>
		</div>
	</xsl:template>
	
				
	
	<xsl:template match="/">
				<xsl:choose>
					<xsl:when test="/qgrs/helpKey = 'help_quickstart'"> 
						<xsl:call-template name="help_quickstart"/>
					</xsl:when>
					<xsl:when test="/qgrs/helpKey = 'help_qgrs'"> 
						<xsl:call-template name="help_qgrs"/>
					</xsl:when>
					<xsl:when test="/qgrs/helpKey = 'help_homologyScore'"> 
						<xsl:call-template name="help_homologyScore"/>
					</xsl:when>
					<xsl:when test="/qgrs/helpKey = 'help_homologyLegend'"> 
						<xsl:call-template name="help_homologyLegend"/>
					</xsl:when>
					<xsl:when test="/qgrs/helpKey = 'help_gScore'"> 
						<xsl:call-template name="help_gScore"/>
					</xsl:when>
					<xsl:when test="/qgrs/helpKey = 'help_homologyMap'"> 
						<xsl:call-template name="help_homologyMap"/>
					</xsl:when>
					<xsl:when test="/qgrs/helpKey = 'help_sequenceViewer'"> 
						<xsl:call-template name="help_sequenceViewer"/>
					</xsl:when>
					<xsl:when test="/qgrs/helpKey = 'help_results'"> 
						<xsl:call-template name="help_results"/>
					</xsl:when>
					<xsl:when test="/qgrs/helpKey = 'help_glossary'"> 
						<xsl:call-template name="help_glossary"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text disable-output-escaping="yes">
							&lt;!doctype html> 
						</xsl:text>
						<html>
							<head>
								<xsl:call-template name="ie_meta"/>
								<xsl:call-template name="css_includes"/>
								<xsl:call-template name="javascript_includes"/>
							</head>
							<body>
								<xsl:call-template name="help_main"/>
							</body>
						</html>
					</xsl:otherwise>
				</xsl:choose>
	</xsl:template>
</xsl:stylesheet>