<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
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
				<style type="text/css">
					#progressTitle {
						background-color:#FDF6D1;
						border-style:solid;
						border-width:1px;
						border-color:  #F9E577;
						padding:0.5em;
				</style>
				<script language="JavaScript">
					
					var lastProgress;
					var lastProgressDiv;
					
					
					var pollingId;
					$(document).ready(function() {
						pollingId = setInterval(poll, 500);
					});
					
					
					function displayProgress(thisProgress) {
						<![CDATA[
						if ( lastProgress == null  || lastProgress.stage != thisProgress.stage ) {
							// Starting new stage.
							if ( lastProgressDiv != null ) {
								lastProgressDiv.children("#progressbarWrapper").children(".progressbar").progressbar( "value" , 100);
								if ( lastProgress.showPercentComplete ) {
									lastProgressDiv.children("p").children(".progressnumber").html(" 100%  ");
								}
							}
							
							var percent = "";
							var progress = "";
							if ( thisProgress.showPercentComplete ) {
								percent = "<span style='float:right' class='progressnumber'>  0%  </span>";
								progress = "<div id='progressbarWrapper' style='margin:0;padding-bottom:0.5em'><div class='progressbar' style='font-size:1px;height:15px'></div></div>";
							}
							var status = (thisProgress.stage == "Complete") ? " - Preparing output" : thisProgress.status;
							lastProgressDiv = 
								$("<div class='progressDiv'><p>" + thisProgress.stage + percent +
								"<span style='font-size:small'>" + "   " +status+ 
								"</span></p>" + progress + "</div>").appendTo('#progressContainer');
						
							
							lastProgressDiv.addClass("container");
							lastProgressDiv.addClass("topContainer");
							lastProgressDiv.addClass("rounded-corners");
							lastProgressDiv.addClass("shadow");
							lastProgressDiv.children("#progressbarWrapper").children(".progressbar").progressbar({value: 0});
							lastProgress = thisProgress;
						}
						else {
							lastProgressDiv.children("#progressbarWrapper").children(".progressbar").progressbar( "value" , parseFloat(thisProgress.percentComplete));
							if ( lastProgress.showPercentComplete ) {
								lastProgressDiv.children("p").children(".progressnumber").html("  " + parseFloat(thisProgress.percentComplete) + "%  ");
							}
						}
						]]>
					}
					
					function poll() {
						
						$.ajax({
 						 	url: "align-poll?jobId=<xsl:value-of select="qgrs/@jobId"/>",
  							cache: false,
  							dataType: "json",
  							success: function(json) {
  								displayProgress(json);
   								if ( json.stage == "Complete" ) {
    								clearInterval(pollingId);
    								window.location.replace("align-complete?jobId=<xsl:value-of select="qgrs/@jobId"/>");
    							}
    							
    							else if (  json.stage.substring(0, 5) == "Error") {
    								clearInterval(pollingId);
    								window.location.replace("align-error?jobId=<xsl:value-of select="qgrs/@jobId"/>");
    							}
						}});
						
					}
				</script>
			</head>
			<body>
				<xsl:call-template name="header">
					<xsl:with-param name="title">:  Analysis in Progress</xsl:with-param>
					<xsl:with-param name="appname">Predictor</xsl:with-param>
				</xsl:call-template>
				<div id="progressContainer" style="width:50%;margin-left:1em;">
					<div id="progressTitle" class="container rounded-corners shadow">Your sequence analysis has started.  <a href="align-cancel?jobId={qgrs/@jobId}">cancel</a></div>
				
				</div>
				
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>