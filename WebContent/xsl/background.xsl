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
					<xsl:with-param name="title">:  Background Information</xsl:with-param>
					<xsl:with-param name="gotostart">false</xsl:with-param>
					<xsl:with-param name="appname"></xsl:with-param>
				</xsl:call-template>
				<div class="messageInsert rounded-corners">
					<h2>What is a G-quadruplex?</h2>
					<p>
The ability of G-rich single stranded nucleic acid molecules to form three-dimensional quadruplex structures is well documented. The G-quadruplex structure is composed of stacked G-tetrads, which are square co-planar arrays of four guanine bases each. These interesting structures may be formed by repeated folding of a single nucleic acid molecule or by interaction of two or four strands and are generally very stable due to cyclic Hoogsteen hydrogen bonding between the four guanines within each tetrad. 
					</p>
					<div style="text-align:center">
					<img src="{$imagePath}qgrs.png" style="width:30%;"/>
					<p style="text-align:center; font-size:smaller">
					SLC4a3 G-quadruplex: 
					5’ UGGGCAGGGCGGGUGGGA 3’
					Predicted intramolecular G-quadruplex
					formed by a ‘G’-Rich Sequence (QGRS)
					found near alternatively spliced site of
					cardiac isoform SLC4a3 mouse
					transcript.
					</p>
					</div>
					<h2>Biological Roles of G-quadruplexes</h2>
					<p>
	Due to a growing evidence for their role in important biological processes, human disease and as therapeutic targets, there has been a tremendous interest in studying G-quadruplex forming sequences in genomes and transcriptomes. Naturally occurring G-quadruplex sequence motifs have been reported in telomeric, promoters and other regions of mammalian genomes. Lately, there has been much interest in the potential roles of RNA G-quadruplexes as cis-regulatory elements of post-transcriptional gene expression. ‘G’ rich sequences (GRS) capable of forming G-quadruplexes, have been implicated in a variety of biological activities such as: mRNA stability, transcription pausing, FMRP binding, translation initiation as well as repression. 
					</p>
					<h2>Predicting Phylogenetically Conserved G-quadruplexes</h2>
					<p>
					Several computational programs have been developed to predict putative G-quadruplexes 
					in nucleotide sequences. However, large-scale computational genomics studies on G-quadruplexes 
					have difficulty validating their predictions without laboriously testing them one by one in wet 
					labs. <b>QGRS-H DB</b> provides comprehensive curated information on the composition and 
					distribution of putative homologous <b>Q</b>uadruplex forming '<b>G</b>'-<b>R</b>ich <b>S</b>equences (<b>QGRS</b>) in the context of 5'- and 3'-untranslated regions, and CDS sections of aligned homologous mRNAs. Identifying evolutionarily conserved G-quadruplex motifs helps validate computational predictions.  QGRS-H DB can be helpful in performing large-scale transcriptome wide analysis of overall occurrence and significance of conserved G-quadruplexes as cis-regulatory elements in the untranslated and translated mRNA regions.
					</p>
				</div>
				
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>