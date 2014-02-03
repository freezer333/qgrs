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
					<xsl:with-param name="title"></xsl:with-param>
					<xsl:with-param name="gotodb">true</xsl:with-param>
					<xsl:with-param name="appname">DB</xsl:with-param>
				</xsl:call-template>
				<div class="container" style="padding:0px">
					<div class="ui-widget-header ui-corner-top" ><p style="margin:0.5em">Live Statistics from MongoDb set</p></div>
					<div class="ui-widget-content ui-corner-bottom" >
					
					<table id="statsTable">
						<tbody>
							<tr>
								<td style="width:40%;vertical-align:top;text-align:right"># of Human mRNA Analyzed</td>
								<td><xsl:value-of select="format-number(/qgrs/counts/@principal, '###,###,###,###')"/></td>
								<td></td>
							</tr>
							<tr>
								<td style="width:40%;vertical-align:top;text-align:right" ># of U-Rich instances found</td>
								<td><xsl:value-of select="format-number(/qgrs/counts/@urich, '###,###,###,###')"/></td>
								<td></td>
							</tr>
						</tbody>
						</table>
					</div>
					</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>