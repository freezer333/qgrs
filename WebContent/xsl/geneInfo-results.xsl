<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" version="4.0" indent="yes" />
	<xsl:variable name="imagePath" select="'../images/'" />
	<xsl:variable name="cssPath" select="'../css/'" />
	<xsl:variable name="jsPath" select="'../javascript/'" />

	<xsl:include href="utils.xsl" />
	<xsl:include href="header.xsl" />

	<xsl:template match="/">

		<html>
			<head>
				<xsl:call-template name="css_includes" />
				<xsl:call-template name="javascript_includes" />
			</head>
			<body>
				<xsl:call-template name="header">
					<xsl:with-param name="title">
						Results
					</xsl:with-param>
				</xsl:call-template>


				<p>
					Gene Information for
					<xsl:value-of select="gene-info/@accessionNumber" />
				</p>
				<p>
					GI Number:
					<xsl:value-of select="gene-info/@giNumber" />
				</p>
				<p>
					<xsl:value-of select="gene-info/@geneName" />
				</p>
				<p>
					mRNA Length:
					<xsl:value-of select="gene-info/mrnaLength" />
				</p>
				<p>
					5' UTR Range:
					<xsl:value-of select="gene-info/utr5s" />
				</p>
				<p>
					CDS Range:
					<xsl:value-of select="gene-info/cds" />
				</p>
				<p>
					3' UTR Range:
					<xsl:value-of select="gene-info/utr3s" />
				</p>
				<p>polyA Signals:</p>
				<xsl:for-each select="gene-info/polyAsignal">
					<p>
						<xsl:value-of select="." />
					</p>
				</xsl:for-each>
				<p>polyA Sites:</p>
				<xsl:for-each select="gene-info/polyAsite">
					<p>
						<xsl:value-of select="." />
					</p>
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
