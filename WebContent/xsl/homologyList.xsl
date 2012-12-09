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
					<th>QGRS Homology <br/>Score</th>
					
				</thead>
				<tbody>
				<xsl:for-each select="qgrs/pairs/pair">
					<xsl:variable name="prin"  select="qgrs[@side='principal']"/>
					<xsl:variable name="comp" select="qgrs[@side='comparison']"/>
					<tr>
						<td><xsl:value-of select="position()"/></td>
						<td><xsl:value-of select="$prin/qgrsId"/><br/><xsl:value-of select="$comp/qgrsId"/></td>
						<td>
							<xsl:value-of select="$prin/geneSymbol"/>: <i> <xsl:value-of select="$prin/geneSpecies"/></i>
							<br/>*
							<xsl:value-of select="$comp/geneSymbol"/>: <i> <xsl:value-of select="$comp/geneSpecies"/></i>
						</td>
						<td>
							<span 	class="qgrsSlice" 
									data-t1="{principle/quadruplex/tetrad1_normalized}" 
									data-t2="{principle/quadruplex/tetrad2_normalized}" 
									data-t3="{principle/quadruplex/tetrad3_normalized}" 
									data-t4="{principle/quadruplex/tetrad4_normalized}" 
									data-nt="{principle/quadruplex/numTetrads}">
										<xsl:value-of select="$prin/qgrsSequence"/>
									</span>
							<br/>
							<span 	class="qgrsSlice" 
									data-t1="{comparison/quadruplex/tetrad1_normalized}" 
									data-t2="{comparison/quadruplex/tetrad2_normalized}" 
									data-t3="{comparison/quadruplex/tetrad3_normalized}" 
									data-t4="{comparison/quadruplex/tetrad4_normalized}" 
									data-nt="{comparison/quadruplex/numTetrads}">
										<xsl:value-of select="$comp/qgrsSequence"/>
									</span>
							
						</td>
						
						<td>
							<xsl:value-of select="$prin/qgrsRegion"/><br/><xsl:value-of select="$comp/qgrsRegion"/>
						</td>
						<td>
							<xsl:value-of select="$prin/qgrsPosition"/><br/><xsl:value-of select="$comp/qgrsPosition"/>
						</td>
						<td>
							<xsl:value-of select="$prin/qgrsTetrads"/><br/><xsl:value-of select="$comp/qgrsTetrads"/>
						</td>
						<td>
							<xsl:value-of select="$prin/qgrsGScore"/><br/><xsl:value-of select="$comp/qgrsGScore"/>
						</td>
						<td><xsl:value-of select="format-number(score, '0.00')"/></td>
						
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