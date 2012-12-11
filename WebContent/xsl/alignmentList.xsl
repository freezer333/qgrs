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
						$(".qgrs-h-link").click( 
						
							function(e) {
								$("#dbGeneId1").val($(this).attr("data-principle"));
								$("#dbGeneId2").val($(this).attr("data-comparison"));
								$("#browse-qgrs-h").click();
								$("#navigationFilterForm").attr("action", "homology-list");
								$("#alignmentId").val($(this).attr("data-alignmentId"));
								$("#navigationFilterForm").submit();
								var aBetterEventObject = jQuery.Event(e);
    							aBetterEventObject.preventDefault()
							}
						
						);
						
						$(".qgrs-p-link").click( 
						
							function(e) {
								$("#dbGeneId1").val($(this).attr("data-principle"));
								$("#browse-qgrs-p").click();
								$("#dbFilterSide").val("principle");
								$("#navigationFilterForm").attr("action", "quadruplex-list");
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
					<xsl:with-param name="title">:  mRNA Homolog Listing</xsl:with-param>
					<xsl:with-param name="gotopredictor">true</xsl:with-param>
					<xsl:with-param name="appname">DB</xsl:with-param>
				</xsl:call-template>
				
				<form id="navigationFilterForm" action="align-list" method="GET">
					<xsl:call-template name="dbCriteria">
						<xsl:with-param name="browse">browse-gene-h</xsl:with-param>
					
					</xsl:call-template>
				</form>
				<div class="container" style="padding:0px">
					<xsl:call-template name="pager"/>
					<div class="ui-widget-header ui-corner-top" style="padding:0.25em">mRNA Homolog Listing (<xsl:value-of select="qgrs/dbCriteria/dbTotalResults"/> results)</div>
					<div class="ui-widget-content ui-corner-bottom" style="padding:0em">
					
					<table class="pairTable" style="width:100%" cellspacing="0" >
						<thead>
						<tr >
							<th class="geneCol">mRNA ID</th>
							<th>Gene</th>
							<th>Organism</th>
							<th>Sequence Similarity</th>
							<th title="Number of QGRS found in the genes wich match the QGRS criteria above">QGRS Total</th>
							<th title="Number of Homologous QGRS pairs within this alignment that match the QGRS Homology criteria above">Homologous QGRS Pairs</th>
							<th></th>
						</tr>
						</thead>
						<tbody>
						<xsl:for-each select="qgrs/results/pair">
							<xsl:variable name="prin" select="gene[@side='principle']"/>
							<xsl:variable name="comp" select="gene[@side='comparison']"/>
							
							<tr>
								<td>
									<a href="geneDetails?id={$prin/geneId}"><xsl:value-of select="$prin/geneId"/></a>
									<br/>
									<a href="geneDetails?id={$comp/geneId}"><xsl:value-of select="$comp/geneId"/></a>
								</td>
								<td>
									<xsl:value-of select="$prin/geneSymbol"/>
									<br/>
									<xsl:value-of select="$comp/geneSymbol"/>
								</td>
								
								
								<td><i><xsl:value-of select="$prin/geneSpecies"/> 
										<br/> <xsl:value-of select="$comp/geneSpecies"/></i></td>
								<td><xsl:value-of select="alignmentScore"/></td>
								
								<td>
								<a href="quadruplex-list?geneId={$prin/geneId}" class="qgrs-p-link" data-principle="{$prin/geneId}">
								<xsl:value-of select="$prin/qgrsCount"/></a> 
								
								</td>
								
								
								<td><a href="homology-list?alignmentId={id}"  class="qgrs-h-link" data-alignmentId="{id}" data-principle="{$prin/geneId}" data-comparison="{$comp/geneId}"><xsl:value-of select="$prin/qgrsHCount"/></a></td>
								
								<td><a href="align-start?seq1Option=seq1IdOption&amp;seq2Option=seq2IdOption&amp;seq1={$prin/geneId}&amp;seq2={$comp/geneId}">Detailed Analysis</a></td>
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