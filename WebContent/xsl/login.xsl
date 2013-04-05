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
					<xsl:with-param name="title">Login</xsl:with-param>
					<xsl:with-param name="gotostart">true</xsl:with-param>
					<xsl:with-param name="appname"></xsl:with-param>
				</xsl:call-template>
				<div class="messageInsert rounded-corners">
					<h1>Login for access to prelimiary / experimental data</h1>
					<p>The data and computational results on the requested page are prelimary, and to avoid confusing others, it is required that you are a member
					of the Ramapo College lab in order to access it.  As we validate our results, more will be publicaly available.  Thank you!</p>
					<form action="authenticate" method="post">
						<p>
						<label for="username" style="width:100px;float:left">Username:</label>
						<input type="text" id="username" name="username"/>
						</p>
						<p>
						<label for="password" style="width:100px;float:left">Password:</label>
						<input type="password" id="password" name="password"/>
						</p>
						<p><xsl:value-of select="/qgrs/message"/></p>
						
						<button type="submit">Login</button>
						
						
					</form>
					
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>