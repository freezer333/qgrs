<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:template name="header">
		<xsl:param name="title"></xsl:param>
		<xsl:param name="gotodb">false</xsl:param>
		<xsl:param name="gotostart">false</xsl:param>
		<xsl:param name="gotopredictor">false</xsl:param>
		<xsl:param name="appname"></xsl:param>
		<div id="heading" class="ui-widget-header">
			<p style="font-size:10pt;padding-right:1em;margin-top:5px; float:right;vertical-align:middle">
				<xsl:if test="$gotostart='true'">
					<a href="start" style="text-decoration:none;color:white;padding-right:2em">new input</a>
				</xsl:if>
				<xsl:if test="$gotodb='true'">
					<a href="dbhome" style="vertical-align:middle;text-decoration:none;color:white;padding-right:2em">
						<img style="vertical-align:middle" src="{$imagePath}logoDB.png"/>QGRS-H DB</a>
				</xsl:if>
				<xsl:if test="$gotopredictor='true'">
					<a href="start" style="vertical-align:middle;text-decoration:none;color:white;padding-right:2em">
						<img style="border-width:0;vertical-align:middle" src="{$imagePath}logo.png"/>QGRS-H Predictor</a>
					
					<a style="vertical-align:middle;text-decoration:none;color:white;padding-right:2em" href="dbStats">database statistics</a>
				</xsl:if>
				
				<a target="_blank" href="help" style="text-decoration:none;color:white;padding-right:2em">help</a>
				<xsl:choose>
					<xsl:when test="$appname='DB'">
						<a target="_blank" href="dbabout" style="text-decoration:none;color:white;padding-right:2em">about</a>	
					</xsl:when>
					<xsl:when test="$appname='Predictor'">
						<a target="_blank" href="about" style="text-decoration:none;color:white;padding-right:2em">about</a>
					</xsl:when>
				</xsl:choose>
				
			</p>
			
			<xsl:choose>
				<xsl:when test="$gotopredictor='true'">
					<img id="heading-logo" src="{$imagePath}logoDB.png"/>
				</xsl:when>
				<xsl:otherwise>
					<img id="heading-logo" src="{$imagePath}logo.png"/>
				</xsl:otherwise>
			</xsl:choose>
			<p style="margin-top:5px">QGRS-H <xsl:value-of select="$appname"/><xsl:value-of select="$title"/></p>
		</div>
	</xsl:template>
</xsl:stylesheet>