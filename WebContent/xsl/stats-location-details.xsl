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
					<xsl:with-param name="title">:  Stats Details (Location x Series x Partition)</xsl:with-param>
					<xsl:with-param name="gotopredictor">true</xsl:with-param>
					<xsl:with-param name="appname">DB</xsl:with-param>
				</xsl:call-template>
				<div class="container" style="padding:0px">
					<div class="ui-widget-header ui-corner-top" style="padding:0.5em">Record Data</div>
					<div class="ui-widget-content ui-corner-bottom" style="background-color:#EEEEEE;padding:0em">
						<table class="pairTable" style="width:100%" cellspacing="0" >
							<tbody>
							<tr><td>Analysis</td><td><xsl:value-of select="/qgrs/result/record/analysis/description"/></td></tr>
							<tr><td>Date Executed</td><td><xsl:value-of select="/qgrs/result/record/analysis/date"/></td></tr>
							<tr><td>mRNA Partition</td><td><xsl:value-of select="/qgrs/result/record/partition/description"/></td></tr>
							<tr><td>QGRS criteria (series)</td><td><xsl:value-of select="/qgrs/result/record/series/description"/></td></tr>
							<tr><td>Location within mRNA</td><td><xsl:value-of select="/qgrs/result/record/location/label"/></td></tr>
							</tbody>
						</table>
					</div>
				</div>
				
				<div class="container" style="padding:0px">
					<div class="ui-widget-header ui-corner-top" style="padding:0.5em">Summary Statistics</div>
					<div class="ui-widget-content ui-corner-bottom" style="background-color:#EEEEEE;padding:0em">
						<table class="pairTable" style="width:100%" cellspacing="0" >
							<tbody>
							<tr><td>Total QGRS</td><td><xsl:value-of select="/qgrs/result/@total"/></td></tr>
							<tr><td>Average # of QGRS/mRNA</td><td><xsl:value-of select="/qgrs/result/@mean"/></td></tr>
							<tr><td>Standard Deviation - # of QGRS/mRNA</td><td><xsl:value-of select="/qgrs/result/@std"/></td></tr>
							<tr><td>Median # of QGRS/mRNA</td><td><xsl:value-of select="/qgrs/result/@median"/></td></tr>
							<tr><td>Average # of QGRS/mRNA (normalized)</td><td><xsl:value-of select="/qgrs/result/@n_mean"/></td></tr>
							<tr><td>Standard Deviation - # of QGRS/mRNA (normalized)</td><td><xsl:value-of select="/qgrs/result/@n_std"/></td></tr>
							<tr><td>Median # of QGRS/mRNA (normalized)</td><td><xsl:value-of select="/qgrs/result/@n_median"/></td></tr>
							<tr><td># mRNA valid at this location</td><td><xsl:value-of select="/qgrs/result/@numSamples"/></td></tr>
							<tr><td># mRNA with at least 1 QGRS</td><td><xsl:value-of select="/qgrs/result/@numSamplesWithQgrs"/></td></tr>
							<tr><td>% mRNA with at least 1 QGRS</td><td><xsl:value-of select="/qgrs/result/@percentSamplesWithQgrs"/></td></tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="container" style="padding:0px">
					<div class="ui-widget-header ui-corner-top" style="padding:0.5em;">Histogram</div>
					<p>Coming soon... histogram showing #QGRS on x-axis and #of genese with corresponding number of QGRS on the Y axis.  
					We would hope for a normal probability distribution, however evidence suggests otherwise....
					</p>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>