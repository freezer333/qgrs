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
			<head>
				<xsl:call-template name="ie_meta"/>
				<xsl:call-template name="css_includes"/>
				<xsl:call-template name="javascript_includes"/>
			</head>
			<body>
				<xsl:call-template name="header">
					<xsl:with-param name="title">:  Active Job</xsl:with-param>
					<xsl:with-param name="gotostart">true</xsl:with-param>
					<xsl:with-param name="appname">Predictor</xsl:with-param>
				</xsl:call-template>
				<div class="messageInsert rounded-corners">
					<p>
					You have an active job running in your session.  Please wait for it to complete or <a href="align-cancel">cancel</a> before attempting to run another.
					</p>
				</div>
				
				
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>