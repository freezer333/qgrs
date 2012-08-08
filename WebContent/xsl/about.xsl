<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" version="5.0" indent="yes"/>
	<xsl:variable name="imagePath" select="'../images/'"/>
	<xsl:variable name="cssPath" select="'../css/'"/>
	<xsl:variable name="jsPath" select="'../javascript/'"/>
	
	<xsl:include href="utils.xsl"/>
	<xsl:include href="header.xsl"/>
	
	<xsl:template match="/">
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
				<xsl:call-template name="header">
					<xsl:with-param name="title">:  About the QGRS-H Predictor</xsl:with-param>
					<xsl:with-param name="gotostart">false</xsl:with-param>
					<xsl:with-param name="appname"></xsl:with-param>
				</xsl:call-template>
				<div class="messageInsert rounded-corners">
					<h2>Credits</h2>
					<p><b>Co-Principle Investigators/Research Mentors:</b><br/>
					<a href="mailto:pbagga@ramapo.edu">Dr. Paramjeet Bagga</a>, Bioinformatics, Ramapo College of New Jersey <br/>
					<a href="mailto:sfrees@ramapo.edu">Dr. Scott Frees </a>, Computer Science, Ramapo College of New Jersey 
					</p>
					<p>
					<b>Undergraduate Student:</b><br/>
					Camille Menendez<br/>
					</p>
					<p>
					<b>Please cite</b>:
					Menendez, C., Frees, S., and Bagga, P. (2012) QGRS-H Predictor: A Web Server
					for Predicting Homologous Quadruplex forming G-Rich Sequence Motifs in
					Nucleotide Sequences. <a href="http://nar.oxfordjournals.org/content/early/2012/05/10/nar.gks422.full?keytype=ref&amp;ijkey=90aQTvF26OeC8yJ" target="_blank">Nucleic Acids Res. doi: 10.1093/nar/gks422</a> 
					<a  style="margin-left:1em" href="http://nar.oxfordjournals.org/content/40/W1/W96.full">40: W96-W103</a>.
					</p>
				</div>
				<div class="messageInsert rounded-corners">
					<h2>Acknowledgements</h2>
					<p>
					We thank RCNJ Information Technology Services and Mike Skafidas for technical assistance with the web servers. 
					
					We acknowledge Matt Crum for help with extensive testing of the QGRS-H Predictor application and thank Vladislav Todorov for his technical assistance.
					
					We wish to thank Manuel Viotti of Cornell University, Viktor Vasilev of Boston University, and Gadareth Higgs of Yale University, for help with extensive testing of the QGRS-H Predictor.
					
					This project was funded in part by a grant from the Provost Office of Ramapo College of New Jersey.
					</p>
				</div>
				<div class="messageInsert rounded-corners">
					<h2>Purpose</h2>
					The goal of <b>QGRS-H Predictor</b> is to map and analyze phylogenetically conserved putative <b>Q</b>uadruplex forming <b>'G'-R</b>ich <b>S</b>equences (QGRS) in the mRNAs and other nucleotide sequences. 
					<h2>About</h2>
					<b>QGRS-H Predictor</b> web tool generates information on composition and distribution of putative <b>homologous</b> G-quadruplexes in semi-globally aligned nucleotide sequences.  The web server can download user-identified <b>RefSeq mRNA</b> sequences from <b>NCBI</b> for alignments followed by homologous QGRS prediction and analysis. Alternatively, the user can provide FASTA or RAW nucleotide sequence pairs for alignment and analysis. This web application is based on published algorithms for recognition and mapping of putative QGRS. 
					<br/><br/>
					Due to a growing evidence for their role in important biological processes and as therapeutic targets, there has been a tremendous amount of interest in studying quadruplex forming G-rich sequences in the mammalian genomes and transcriptomes. This is evident from a surge in the relevant published literature in recent years. The QGRS-H Predictor program is a unique web  tool for mapping homologous G-quadruplex forming sequences in the context of 5'- and 3'-UTRs (untranslated regions) and CDS sections of aligned mRNA sequences. Identifying phylogenetically conserved motifs adds another dimension of validating the computational G-quadruplex predictions. QGRS-H Predictor also generates <b>QGRS Homology Scores</b> which evaluate the predicted G-quadruplex homologous pairs for their evolutionary conservation. In addition to providing interactive tabular data on composition and locations of QGRS relative to CDS in the mRNA sequence, QGRS-H Predictor features visual comparison of homologous QGRS distribution patterns in the aligned mRNA sequences in a highly interactive 'Sequence View'.  The data table of Homologous QGRS Pairs as well as the Sequence View are intimately connected with the dynamically generated and highly interactive QGRS Homology Map. 
					<br/><br/>
					QGRS-H Predictor can also be used for mapping and analysis of phylogenetically conserved putative G-quadruplexes in the genomic sequences, e.g. promoter and telomeric regions, through FASTA/RAW sequence input.
				</div>
				
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>