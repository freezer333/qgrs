<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html"  indent="yes" />
	<xsl:variable name="imagePath" select="'../images/'"/>
	<xsl:variable name="cssPath" select="'../css/'"/>
	<xsl:variable name="jsPath" select="'../javascript/'"/>
	
	<xsl:include href="utils.xsl"/>
	<xsl:include href="header.xsl"/>
	
	<xsl:template match="/">
		<html>
			<head>
				<xsl:call-template name="css_includes"/>
				<xsl:call-template name="javascript_includes"/>
			</head>
			<body>
				<xsl:call-template name="header">
					<xsl:with-param name="title">Input</xsl:with-param>
				</xsl:call-template>
				
				<form action="gene-output" name="gene-output">
					<p>Please enter accession number for the sequences you wish to examine:</p>
					<p>
					<label for="seq1">Sequence 1</label>
					<input type="text" id="seq1" name="seq1"/>
					</p>
					<input type="submit" value="Get Gene Info"/>
				</form>
				<p>Some useful accession numbers for now...</p>
				<p>HUMAN_ACADM:  187960097</p>
				<p>CHIMP_UNCONFIRMED_ACADM:  114557330</p>
				<p>CHIMP_CONFIRMED_ACADM:  160961496</p>
				<p>HUMAN_MECP2:  160707948</p>
				<p>MOUSE_MECP2:  126517481</p>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
