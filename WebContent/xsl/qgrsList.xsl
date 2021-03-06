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
				
				
				<script language="javascript">
					$(document).ready(function() {
						$(".qgrs-h-link").click( 
							function(e) {
								$("#dbQgrsId1").val($(this).attr("data-qgrsId"));
								$("#browse-qgrs-h").click();
								$("#navigationFilterForm").attr("action", "homology-list");
								$("#navigationFilterForm").submit();
								var aBetterEventObject = jQuery.Event(e);
    							aBetterEventObject.preventDefault()
							}
						
						);
					});
					
					
				</script>
			</head>
			<body>
				<xsl:call-template name="header">
					<xsl:with-param name="title">:  QGRS Listing</xsl:with-param>
					<xsl:with-param name="gotopredictor">true</xsl:with-param>
					<xsl:with-param name="appname">DB</xsl:with-param>
				</xsl:call-template>
				<form id="navigationFilterForm" action="quadruplex-list" method="GET">
				<xsl:call-template name="dbCriteria">
						<xsl:with-param name="browse">
							<xsl:choose>
								<xsl:when test="qgrs/dbCriteria/dbFilterSide = 'comparison'">browse-qgrs-c</xsl:when>
								<xsl:otherwise>browse-qgrs-p</xsl:otherwise>
							</xsl:choose>
						</xsl:with-param>
				</xsl:call-template>
				</form>
				<div class="container" style="padding:0px">
					<xsl:call-template name="pager"/>
					<div class="ui-widget-header ui-corner-top" style="padding:0.5em">QGRS Instances (<xsl:value-of select="qgrs/dbCriteria/dbTotalResults"/> results)</div>
					<div class="ui-widget-content ui-corner-bottom" style="padding:0em">
				

				<table class="pairTable" style="width:100%" cellspacing="0" >
				<thead>
					<tr>
						<th>QGRS ID</th>
						<th>Gene</th>
						<th>Organism</th>
						<th>Sequence</th>
						<th>Region</th>
						<th>nt. Position</th>
						<th># Tetrads</th>
						<th>G-Score</th>
						<th># QGRS H</th>
					</tr>
				</thead>
				<tbody>
				<xsl:for-each select="qgrs/results/qgrs">
					<tr>
						<td><xsl:value-of select="qgrsId"/></td>
						<td><xsl:value-of select="geneSymbol"/></td>
						<td><i><xsl:value-of select="geneSpecies"/></i></td>
						<td>
							<span 	class="qgrsSlice" 
									data-t1="{tetrad1_normalized}" 
									data-t2="{tetrad2_normalized}" 
									data-t3="{tetrad3_normalized}" 
									data-t4="{tetrad4_normalized}" 
									data-nt="{qgrsTetrads}">
										<xsl:value-of select="qgrsSequence"/>
									</span>
							
						</td>
						<td><xsl:value-of select="qgrsRegion"/></td>
						<td><xsl:value-of select="qgrsPosition"/></td>
						<td><xsl:value-of select="qgrsTetrads"/></td>
						<td><xsl:value-of select="qgrsGScore"/></td>
						<td><a href="homology-list" class="qgrs-h-link" data-qgrsId="{qgrsId}"><xsl:value-of select="hCount"/></a></td>
						
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