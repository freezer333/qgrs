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
				
				<script type="text/javascript">
					$(document).ready(function() {
						
					});
				</script>
				
			</head>
			<body>
				<xsl:call-template name="header">
					<xsl:with-param name="title"></xsl:with-param>
					<xsl:with-param name="gotopredictor">true</xsl:with-param>
					<xsl:with-param name="appname">DB</xsl:with-param>
				</xsl:call-template>
				
				<div class="container" style="padding:0em;margin:3em">
					<div class="ui-widget-header ui-corner-top" style="padding:0.25em">Welcome to the QGRS-H Database</div>
					
					<div class="ui-widget-content ui-corner-bottom" style="padding:1em">
						
						<p style="padding-top:2em;"><img src="{$imagePath}dbLogo.png" style="position:relative;top:-20px;float:left;;margin-top:0"/>
						The goal of QGRS-H DB is to provide freely accessible large-scale curated data on 
						evolutionarily conserved putative Quadruplex forming 'G'-Rich Sequences (QGRS) mapped in 
						eukaryotic protein coding transcriptomes. QGRS-H DB contains comprehensive information on 
						the composition and distribution of putative homologous G-quadruplexes in semi-globally 
						aligned homologous mRNA sequences. Computations were performed with an indigenously 
						developed and previously published software program called <a href="http://nar.oxfordjournals.org/content/early/2012/05/10/nar.gks422.full?keytype=ref&amp;ijkey=90aQTvF26OeC8yJ" target="_blank">QGRS-H Predictor</a>. 
						</p>
					<p style="font-size:large;font-weight:bold;text-align:center;clear:both">
					<a style="color:orange;" href="align-list">Search the Database</a></p>
					</div>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>