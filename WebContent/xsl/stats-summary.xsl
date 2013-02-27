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
					<xsl:with-param name="title">:  Stats Summary</xsl:with-param>
					<xsl:with-param name="gotopredictor">true</xsl:with-param>
					<xsl:with-param name="appname">DB</xsl:with-param>
				</xsl:call-template>
				<div class="container" style="padding:0px">
					<div class="ui-widget-header ui-corner-top" style="padding:0.5em"><xsl:value-of select="qgrs/analysis/id"/></div>
					<div class="ui-widget-content ui-corner-bottom" style="background-color:#EEEEEE;padding:0em">
						<p>Analysis Description:  <xsl:value-of select="qgrs/analysis/description"/></p>
					
					
					<xsl:for-each select="qgrs/analysis/partitions/partition">
						<xsl:variable name="partition"><xsl:value-of select="partitionId"/></xsl:variable>
						<div class="analysisPartition">
						<h2>Partition ID:  <xsl:value-of select="partitionId"/></h2>
						<p>Description:  <xsl:value-of select="description"/></p>
						<p>Number of mRNA samples:  <xsl:value-of select="numSamples"/></p>
						
						<table class="analysisTable">
							<caption>Totals</caption>
							<thead>
								<tr>
								<th>-</th>
								<xsl:for-each select="/qgrs/analysis/series/series">
									<th><xsl:value-of select="description"/></th>
								</xsl:for-each>
								</tr>
							</thead>
							<tbody>
								<xsl:for-each select="/qgrs/analysis/locations/location">
									<xsl:sort select="id"/>
									<tr>
										<xsl:variable name="loc"> <xsl:value-of select="id"/></xsl:variable>
										<td class="analysisXLabel"><xsl:value-of select="label"/></td>
										<xsl:for-each select="/qgrs/analysis/series/series">
											<xsl:variable name="series"><xsl:value-of select="seriesId"/></xsl:variable>
											<td>
												<xsl:value-of select="/qgrs/analysis/results/result[@partitionId=$partition][@seriesId=$series][@locationId=$loc]/@total"/>
											</td>
										</xsl:for-each>
									</tr>
								</xsl:for-each>
							</tbody>
						</table>
						<table class="analysisTable">
							<caption>Means</caption>
							<thead>
								<th>-</th>
								<xsl:for-each select="/qgrs/analysis/series/series">
									<th><xsl:value-of select="description"/></th>
								</xsl:for-each>
							</thead>
							<tbody>
								<xsl:for-each select="/qgrs/analysis/locations/location">
									<xsl:sort select="id"/>
									<tr>
										<xsl:variable name="loc"> <xsl:value-of select="id"/></xsl:variable>
										<td class="analysisXLabel"><xsl:value-of select="label"/></td>
										<xsl:for-each select="/qgrs/analysis/series/series">
											<xsl:variable name="series"><xsl:value-of select="seriesId"/></xsl:variable>
											<td>
												<xsl:value-of select="/qgrs/analysis/results/result[@partitionId=$partition][@seriesId=$series][@locationId=$loc]/@mean"/>
											</td>
										</xsl:for-each>
									</tr>
								</xsl:for-each>
							</tbody>
						</table>
						<table class="analysisTable">
							<caption>Medians</caption>
							<thead>
								<th>-</th>
								<xsl:for-each select="/qgrs/analysis/series/series">
									<th><xsl:value-of select="description"/></th>
								</xsl:for-each>
							</thead>
							<tbody>
								<xsl:for-each select="/qgrs/analysis/locations/location">
									<xsl:sort select="id"/>
									<tr>
										<xsl:variable name="loc"> <xsl:value-of select="id"/></xsl:variable>
										<td class="analysisXLabel"><xsl:value-of select="label"/></td>
										<xsl:for-each select="/qgrs/analysis/series/series">
											<xsl:variable name="series"><xsl:value-of select="seriesId"/></xsl:variable>
											<td>
												<xsl:value-of select="/qgrs/analysis/results/result[@partitionId=$partition][@seriesId=$series][@locationId=$loc]/@median"/>
											</td>
										</xsl:for-each>
									</tr>
								</xsl:for-each>
							</tbody>
						</table>
						</div>
					</xsl:for-each>
					
					
					</div>
				</div>
				
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>