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
					$(document).ready( function() {
						$(".qgrs-count-link").click( 
							function(e) {
								var side = $("#dbFilterSide").val();
								if ( side == 'comparison' ) {
									$("#dbGeneId2").val($(this).attr("data-accession"));
								}
								else {
									$("#dbGeneId1").val($(this).attr("data-accession"));
								}
								$("#navigationFilterForm").attr("action", "quadruplex-list");
								$("#navigationFilterForm").submit();
							}
						);
						
						$(".mrnah-count-link").click( 
							function(e) {
								var side = $("#dbFilterSide").val();
								if ( side == 'comparison' ) {
									$("#dbGeneId2").val($(this).attr("data-accession"));
									$("#dbGeneId1").val("");
									$("#dbGeneSymbol1").val("");
									$("#dbSpecies1").val("");
								}
								else {
									$("#dbGeneId1").val($(this).attr("data-accession"));
									$("#dbGeneId2").val("");
									$("#dbGeneSymbol2").val("");
									$("#dbSpecies2").val("");
								}
								$("#navigationFilterForm").attr("action", "align-list");
								$("#navigationFilterForm").submit();
							}
						);
					});
						
				</script>
			</head>
			<body>
				<xsl:call-template name="header">
					<xsl:with-param name="title">:  Gene list</xsl:with-param>
					<xsl:with-param name="gotopredictor">true</xsl:with-param>
					<xsl:with-param name="appname">DB</xsl:with-param>
				</xsl:call-template>
				<form id="navigationFilterForm" action="gene-list" method="GET">
					<xsl:call-template name="dbCriteria">
						<xsl:with-param name="browse">
							<xsl:choose>
								<xsl:when test="qgrs/dbCriteria/dbFilterSide = 'comparison'">browse-gene-c</xsl:when>
								<xsl:otherwise>browse-gene-p</xsl:otherwise>
							</xsl:choose>
						</xsl:with-param>
					</xsl:call-template>
					
				</form>
				
				<div class="container" style="padding:0px">
					<xsl:call-template name="pager"/>
					<div class="ui-widget-header ui-corner-top" style="padding:0.5em">mRNA Listing (<xsl:value-of select="qgrs/dbCriteria/dbTotalResults"/> results)</div>
					<div class="ui-widget-content ui-corner-bottom" style="padding:0em">
				
				<table class="pairTable" style="width:100%" cellspacing="0" >
				<thead>
					<th>mRNA ID</th>
					<th>Gene</th>
					<th>Organism</th>
					<th title="QGRS found within this gene (matching criteria above)">QGRS Count</th>
					<th title="Number of mRNA Homologs that contain Homologous QGRS pairs (matching criteria above) with this gene">mRNA Homologs</th>
				</thead>
				
				<tbody>
				<xsl:for-each select="qgrs/gene">
					<tr>
						<td><a href="geneDetails?id={@accessionNumber}"><xsl:value-of select="@accessionNumber"/></a></td>
						<td><xsl:value-of select="geneSymbol"/></td>
						<td><i><xsl:value-of select="species"/></i></td>
						<td><a href="javascript:void(0)" class="qgrs-count-link" data-accession="{@accessionNumber}"><xsl:value-of select="qgrsCount"/></a></td>
						<td><a href="javascript:void(0)" class="mrnah-count-link" data-accession="{@accessionNumber}"><xsl:value-of select="mrnaHomologueCount"/></a></td>
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