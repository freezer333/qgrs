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
					<xsl:with-param name="title">:  mRNA Details</xsl:with-param>
					<xsl:with-param name="gotopredictor">true</xsl:with-param>
					<xsl:with-param name="gotodb">true</xsl:with-param>
					<xsl:with-param name="appname">DB</xsl:with-param>
				</xsl:call-template>
				<div class="container" style="padding:0px">
					<div class="ui-widget-header ui-corner-top" >mRNA Details (<xsl:value-of select="qgrs/mrna/info/@accessionNumber"/>)</div>
					<div class="ui-widget-content ui-corner-bottom" >
					
					<table id="geneDetailsTable" class="rounded-corners">
						<tbody class="rounded-corners">
							<tr>
								<td class="partition" style="vertical-align:top">RefSeq mRNA Accession #</td>
								<td><xsl:value-of select="/qgrs/mrna/info/@accessionNumber"/></td>
							</tr>
							<tr>
								<td class="partition" >RefSeq mRNA GI #</td>
								<td><xsl:value-of select="/qgrs/mrna/info/@giNumber"/></td>
							</tr>
							<tr>
								<td class="partition" >mRNA Description</td>
								<td><xsl:value-of select="/qgrs/mrna/info/@geneName"/></td>
							</tr>
							<tr>
								<td class="partition" >mRNA Length</td>
								<td><xsl:value-of select="/qgrs/mrna/info/mrnaLength"/></td>
							</tr>
							<tr>
								<td class="partition" >5' UTR position</td>
								<td><xsl:value-of select="/qgrs/mrna/info/utr5s"/></td>
							</tr>
							<tr>
								<td class="partition" >CDS position</td>
								<td><xsl:value-of select="/qgrs/mrna/info/cds"/></td>
							</tr>
							<tr>
								<td class="partition" >3' UTR position</td>
								<td><xsl:value-of select="/qgrs/mrna/info/utr3s"/></td>
							</tr>
							<tr>
								<td class="partition" >Poly-A Signals</td>
								<td>
									<xsl:for-each select="/qgrs/mrna/info/polyAsignal">
										<p>
											<xsl:value-of select="." />
										</p>
									</xsl:for-each>
								</td>
							</tr>
							<tr>
								<td class="partition" >Poly-A Sites</td>
								<td>
									<xsl:for-each select="/qgrs/mrna/info/polyAsite">
										<p>
											<xsl:value-of select="." />
										</p>
									</xsl:for-each>
								</td>
							</tr>
							
							
						</tbody>
						</table>
						
					</div>
					</div>
					
				
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>