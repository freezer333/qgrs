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
					<xsl:with-param name="title">:  Stats Home</xsl:with-param>
					<xsl:with-param name="gotopredictor">true</xsl:with-param>
					<xsl:with-param name="appname">DB</xsl:with-param>
				</xsl:call-template>
				<div class="container" style="padding:0px">
					<div class="ui-widget-header ui-corner-top" style="padding:0.5em">Available Statistical Analyses</div>
					<div class="ui-widget-content ui-corner-bottom" style="padding:0em">
				
				<table class="pairTable" style="width:100%" cellspacing="0" >
				<thead>
					<th>Name</th>
				</thead>
				
				<tbody>
				<xsl:for-each select="qgrs/runner">
					<tr>
						<td><a href="statsexport?table={tableName}"><xsl:value-of select="description"/></a></td>
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