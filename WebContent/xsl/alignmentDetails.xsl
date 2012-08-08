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
			<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE"/>
			<head>
				<xsl:call-template name="ie_meta"/>
				<xsl:call-template name="css_includes"/>
				<xsl:call-template name="javascript_includes"/>
			</head>
			<body>
				<xsl:call-template name="header">
					<xsl:with-param name="title">:  Alignment list</xsl:with-param>
					<xsl:with-param name="gotopredictor">true</xsl:with-param>
					<xsl:with-param name="appname">Predictor</xsl:with-param>
				</xsl:call-template>
				<div class="container" style="padding:0px">
					<div class="ui-widget-header ui-corner-top" style="padding:0.25em">Gene Alignment Results</div>
					<div class="ui-widget-content ui-corner-bottom" style="padding:0em">
				
				<div class ="subcontainer">
					<h1>Principal</h1>
					<p><a href="quadruplex-list?geneId={/qgrs/principleSequenceId}">All QGRS in this sequence</a></p>

				</div>
				<div class ="subcontainer">
					<h1>Comparison</h1>
					<p><a href="quadruplex-list?geneId={/qgrs/comparisonSequenceId}">All QGRS in this sequence</a></p>
					
				</div>
				<div class ="subcontainer">
					<p>
					<a href="homology-list?alignmentId={/qgrs/alignmentId}">Show Homologous QGRS pairs in this sequence alignment</a>
					</p>
					<p>
					<a href="align-start?seq1Option=seq1IdOption&amp;seq2Option=seq2IdOption&amp;seq1={/qgrs/principleSequenceId}&amp;seq2={/qgrs/comparisonSequenceId}">Detailed Analysis</a>
					</p>
				</div>
				</div>
				</div>
				
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>