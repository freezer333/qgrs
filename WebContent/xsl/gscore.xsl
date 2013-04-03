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
					
				</script>
			</head>
			<body>
				<xsl:call-template name="header">
					<xsl:with-param name="title">:  GScore Calculator</xsl:with-param>
					<xsl:with-param name="gotopredictor">true</xsl:with-param>
					<xsl:with-param name="appname">DB</xsl:with-param>
				</xsl:call-template>
				<div class="container" style="padding:0px">
					<div class="ui-widget-header ui-corner-top" style="padding:0.5em">G-Score Testing</div>
					<div class="ui-widget-content ui-corner-bottom" style="padding:0em">
				
				
				<form action="gscore" method="post">
					<div style="padding:1em">
						<p><label for="seq">Enter sequence to identify and score QGRS:  </label><br/>
						<textarea style="width:70%;height:100px" name="seq" id="seq"><xsl:value-of select="qgrs/seq"/></textarea>
						</p>
						<p><input type="submit" value="Score QGRS"/></p>
					</div>
				</form>
				
				<table class="pairTable" style="width:100%" cellspacing="0" >
				<thead>
					<th>Start</th>
					<th>Num Tetrads</th>
					<th>Y1</th>
					<th>Y2</th>
					<th>Y3</th>
					<th>Length</th>
					<th>G-Score</th>
					<th>Nucleotide String</th>
				</thead>
				
				<tbody>
				<xsl:for-each select="qgrs/qgrs">
					<xsl:sort select="score" order="descending" data-type="number"/>
					<xsl:sort select="start" order="ascending" data-type="number"/>
					<xsl:sort select="x" order="descending" data-type="number"/>
					<xsl:sort select="y1" order="ascending" data-type="number"/>
					<xsl:sort select="y2" order="ascending" data-type="number"/>
					<xsl:sort select="y3" order="ascending" data-type="number"/>
					<tr>
						<td><xsl:value-of select="start"/></td>
						<td><xsl:value-of select="x"/></td>
						<td><xsl:value-of select="y1"/></td>
						<td><xsl:value-of select="y2"/></td>
						<td><xsl:value-of select="y3"/></td>
						<td><xsl:value-of select="length"/></td>
						<td><xsl:value-of select="score"/></td>
						<td>
							<b style="color:blue"><xsl:value-of select="nucleotides/ts"/></b>
							<xsl:value-of select="nucleotides/loop1"/>
							<b style="color:blue"><xsl:value-of select="nucleotides/ts"/></b>
							<xsl:value-of select="nucleotides/loop2"/>
							<b style="color:blue"><xsl:value-of select="nucleotides/ts"/></b>
							<xsl:value-of select="nucleotides/loop3"/>
							<b style="color:blue"><xsl:value-of select="nucleotides/ts"/></b>
						</td>
					</tr>
				</xsl:for-each>
				</tbody>
				</table>
				</div>
				</div>
				
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>