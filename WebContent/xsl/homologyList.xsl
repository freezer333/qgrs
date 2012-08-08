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
					<xsl:with-param name="title">:  Homologous QGRS Pairs Listing</xsl:with-param>
					<xsl:with-param name="gotopredictor">true</xsl:with-param>
					<xsl:with-param name="appname">DB</xsl:with-param>
				</xsl:call-template>
				<form id="navigationFilterForm" action="homology-list" method="GET">
					<xsl:call-template name="dbCriteria">
						<xsl:with-param name="browse">browse-qgrs-h</xsl:with-param>
					</xsl:call-template>
					
				</form>
				<div class="container" style="padding:0px">
					<xsl:call-template name="pager"/>
					<div class="ui-widget-header ui-corner-top" style="padding:0.5em">Homologous QGRS Pairs Listing (<xsl:value-of select="qgrs/dbCriteria/dbTotalResults"/> results)</div>
					<div class="ui-widget-content ui-corner-bottom" style="padding:0em">
				
				<table class="pairTable" style="width:100%" cellspacing="0" >
				<thead>
					<th>#</th>
					<th>QGRS ID</th>
					<th>mRNA</th>
					<th>Sequence</th>
					<th>Region</th>
					<th>nt. Position</th>
					<th>Tetrads</th>
					<th>G-Scores</th>
					<!-- 
					<th>Overlap <br/>Score</th>
					<th>Avg. Loop <br/>Length Score</th>
					<th>Tetrad <br/>Score</th>
					<th>Total Length <br/>Score</th>
					 -->
					<th>QGRS Homology <br/>Score</th>
					
				</thead>
				<tbody>
				<xsl:for-each select="qgrs/homology">
					<tr>
						<td><xsl:value-of select="position()"/></td>
						<td><xsl:value-of select="gq1Id"/><br/><xsl:value-of select="gq2Id"/></td>
						<td>
							<xsl:value-of select="principle/gene/geneSymbol"/>: <i> <xsl:value-of select="principle/gene/species"/></i>
							<br/>
							<xsl:value-of select="comparison/gene/geneSymbol"/>: <i> <xsl:value-of select="comparison/gene/species"/></i>
						</td>
						<td>
							<span 	class="qgrsSlice" 
									data-t1="{principle/quadruplex/tetrad1_normalized}" 
									data-t2="{principle/quadruplex/tetrad2_normalized}" 
									data-t3="{principle/quadruplex/tetrad3_normalized}" 
									data-t4="{principle/quadruplex/tetrad4_normalized}" 
									data-nt="{principle/quadruplex/numTetrads}">
										<xsl:value-of select="principle/quadruplex/sequenceSlice"/>
									</span>
							<br/>
							<span 	class="qgrsSlice" 
									data-t1="{comparison/quadruplex/tetrad1_normalized}" 
									data-t2="{comparison/quadruplex/tetrad2_normalized}" 
									data-t3="{comparison/quadruplex/tetrad3_normalized}" 
									data-t4="{comparison/quadruplex/tetrad4_normalized}" 
									data-nt="{comparison/quadruplex/numTetrads}">
										<xsl:value-of select="comparison/quadruplex/sequenceSlice"/>
									</span>
							
						</td>
						
						<td>
							<xsl:value-of select="principle/quadruplex/region"/><br/><xsl:value-of select="comparison/quadruplex/region"/>
						</td>
						<td>
							<xsl:value-of select="principle/quadruplex/position"/><br/><xsl:value-of select="comparison/quadruplex/position"/>
						</td>
						<td>
							<xsl:value-of select="principle/quadruplex/numTetrads"/><br/><xsl:value-of select="comparison/quadruplex/numTetrads"/>
						</td>
						<td>
							<xsl:value-of select="principle/quadruplex/score"/><br/><xsl:value-of select="comparison/quadruplex/score"/>
						</td>
						<!-- 
						<td><xsl:value-of select="format-number(overlapScore, '0.00')"/></td>
						<td><xsl:value-of select="format-number(tetradScore, '0.00')"/></td>
						<td><xsl:value-of select="format-number(avgLoopScore, '0.00')"/></td>
						<td><xsl:value-of select="format-number(totalLengthScore, '0.00')"/></td>
						 -->
						<td><xsl:value-of select="format-number(overallScore, '0.00')"/></td>
						
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