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
				<script type="text/javascript">
					$(document).ready(function() {
						$("tbody tr td").click( function() {
							
							$(".selectedRow").removeClass("selectedRow");
							selectBases($(this).parent().attr("data-seq1start"), 
										$(this).parent().attr("data-seq1end"), 
										$(this).parent().attr("data-seq2start"), 
										$(this).parent().attr("data-seq2end"), 
										$(this).parent().attr("data-seq1tetrad"), 
										$(this).parent().attr("data-seq2tetrad"));
							$(this).parent().children().addClass("selectedRow");
							
						});
						
						$(".principleSection").each(function(){textToSpans($(this))});
						$(".comparisonSection").each(function(){textToSpans($(this))});
						
						var homologyCuttoff = <xsl:value-of select="qgrs/viewParams/similarityCutoff"/>;
						$("#similarityCutoffSlider").slider({
										value:homologyCuttoff.toFixed(2),
										min: 0,
										max: 1,
										step:0.05,
										slide: function( event, ui ) {
											$( "#similarityCutoff" ).val( ui.value );
											$( "#homologyScoreCutoffValue").html(ui.value.toFixed(2));
										},
										stop: function( event, ui ) {
											$( "#filterForm" ).submit();
										}
						});
						$("#gScoreCutoffSlider").slider({
										value:<xsl:value-of select="qgrs/viewParams/gScoreCutoff"/>,
										min: 13,
										max: 52,
										step:1,
										slide: function( event, ui ) {
											$( "#gScoreCutoff" ).val( ui.value );
											$( "#gScoreCutoffValue").html(ui.value.toFixed(0));
										},
										stop: function( event, ui ) {
											$( "#filterForm" ).submit();
										}
						});
						
						$("#tetradCutoffSlider").slider({
										value:<xsl:value-of select="qgrs/viewParams/tetradCutoff"/>,
										min: 2,
										max: 5,
										step:1,
										slide: function( event, ui ) {
											$( "#tetradCutoff" ).val( ui.value );
											$( "#tetradCutoffValue").html(ui.value.toFixed(0));
										},
										stop: function( event, ui ) {
											$( "#filterForm" ).submit();
										}
						});
						
						
						
						$(".pairTable tr td").eq("0").click();
						
						
					});
					
					function colorText(jqElement, index) {
						jqElement.children("span").eq(index).addClass("tetrad");
					}
					
					function colorRange(jqElement, start, end ) {
						jqElement.children("span").slice(start, end).addClass("selectedBase");
					}
					
					
					// Converts all the text in this span to individual span elements
					// with id="baseN"
					function textToSpans(jqElement) {
						var character = 0;
						var content = jqElement.html();
						jqElement.html("");
						
						for(i=0; i&lt;content.length; i++) {
							$("&lt;span/>").html(content.charAt(i)).appendTo(jqElement);
						}
					}
					
					
					
					
					function selectBases(start1, end1, start2, end2, tetrad1, tetrad2) {
						end1++;
						end2++;
						$(".selectedSection").removeClass("selectedSection");
						$(".selectedBase").removeClass("selectedBase");
						$(".tetrad").removeClass("tetrad");
						
						// Find the section start and end is in.
						var start1SectionNumber = Math.floor(start1 / 30);
						var end1SectionNumber = Math.floor(end1 / 30);
						var start2SectionNumber = Math.floor(start2 / 30);
						var end2SectionNumber = Math.floor(end2 / 30);
						
						for ( i = start1SectionNumber; i &lt;= end1SectionNumber; i++ ) {
							s = 0;
							e = 30;
							if ( i == start1SectionNumber ) s = start1%30;
							if ( i == end1SectionNumber ) e = end1%30;
							$(".sequenceSection").eq(i).addClass("selectedSection");
							
							var seq = $(".sequenceSection").eq(i).children(".principleSection");
							
							indexes = tetrad1.split('-');
							var len=indexes.length;
							for(var q=0; q &lt; len; q++) {
								j = indexes[q];
								if ( j >= i*30 &amp;&amp; j &lt; (i*30+30) ) {
									colorText(seq,j-i*30);
								}
							}
							colorRange(seq, s, e);
							
						}
						for ( i = start2SectionNumber; i &lt;= end2SectionNumber; i++ ) {
							s = 0;
							e = 30;
							if ( i == start2SectionNumber ) s = start2%30;
							if ( i == end2SectionNumber ) e = end2%30;
							$(".sequenceSection").eq(i).addClass("selectedSection");
							
							var seq = $(".sequenceSection").eq(i).children(".comparisonSection");
							indexes = tetrad2.split('-');
							var len=indexes.length;
							for(var q=0; q &lt; len; q++) {
								j = indexes[q];
								if ( j >= i*30 &amp;&amp; j &lt; (i*30+30) ) {
									colorText(seq,j-i*30);
								}
							}
							colorRange(seq, s, e);
						}
							
						
						var seqIndex = start1SectionNumber;
						if ( seqIndex > 0 ) seqIndex--;
						
						$(".sequenceContainer").scrollTo( $(".sequenceSection").eq(seqIndex), 800 );
						
						$(".checkBoxButtonLabel").addClass("roundedCorners");
					}

				</script>
			
				<!-- Chart/Map Creations -->
				<script type="text/javascript">
			
					function setBounds(start, end) {
						$("#seq1StartIndex").val(Math.round(start));
						$("#seq1EndIndex").val(Math.round(end));
						$("#filterForm").submit();
					}
					var chart;
					$(document).ready(function() {
						chart = new Highcharts.Chart({
							chart: {
								renderTo: 'container', 
								height: 200,
								defaultSeriesType: 'area',
								 events: {
            						selection: function(event) {
                						if (event.xAxis) {
                							setBounds(event.xAxis[0].min, event.xAxis[0].max);
                    						//alert('min: '+ event.xAxis[0].min +', max: '+ event.xAxis[0].max);
                						} 
            						}
        						}, 
								zoomType:'x' 
								
								
							},
							credits: { enabled:false}, 
							legend: {
        						enabled: true, 
        						floating: true, 
        						verticalAlign:"top", 
        						align:"right", 
        						borderWidth:0
    						},
							title: {
								text: ''
							},
							
							xAxis: {
								labels: {
									formatter: function() {
										return this.value;
									}
								}, 
								allowDecimals : false	, 
								title: {
									text: 'Nucleotide position in the principal mRNA'
								},
														
							},
							yAxis: {
								title: {
									text: 'Homology Score'
								},
								labels: {
									formatter: function() {
										return this.value;
									}
								}, 
								max : 1.25, 
								tickInterval : 0.25, 
								showLastLabel: false
							},
							tooltip: {
								formatter: function() {
									return Highcharts.numberFormat(this.y, 3);
								}
							},
							plotOptions: {
								area: {
									pointStart: 0,
									fillOpacity: 0.95,
									marker: {
										enabled: false,
										symbol: 'circle',
										radius: 2,
										states: {
											hover: {
												enabled: true
											}
										}
									}
								}
							},
							
							
							
							series: [{
         						name: 'Max',
         						color: '#FFCC80',
						         data: [<xsl:value-of select="/qgrs/alignmentResults/pairResult/homologyMap/max"/> ]
						      }, {
						         name: 'Avg',
						         data: [<xsl:value-of select="/qgrs/alignmentResults/pairResult/homologyMap/avg"/> ]
						      }]
						});
						
						chart.xAxis[0].setExtremes(<xsl:value-of select="qgrs/viewParams/seq1StartIndex"/>, <xsl:value-of select="qgrs/viewParams/seq1EndIndex"/>);
				
						$("#resetZoom").click( function() {
							setBounds(0, <xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/@length"/>);
						});
						
						
						$(".highcharts-legend").append("<p>here</p>");
					});
				
				</script>
				
			</head>
			<body>
				<xsl:call-template name="header">
					<xsl:with-param name="title">:  Homology Results</xsl:with-param>
					<xsl:with-param name="gotostart">true</xsl:with-param>
					<xsl:with-param name="gotodb">true</xsl:with-param>
					<xsl:with-param name="appname">Predictor</xsl:with-param>
				</xsl:call-template>
				
				<div id="helpDialog"></div>
				<div class="wrapper">
				<div class="container topContainer" id="inputContainer">
					<!-- <div style="float:right;margin-top:0.5em; font-size:8pt" title="Click to show/hide more details about the sequences analyzed" >
						<input type="checkbox" id="check" /><label class="checkBoxButtonLabel" for="check">Details</label>
					</div> -->
					
					
					<div style="width:90%;">
					<table style="padding-top:0.5em;width:100%;font-size:10pt;marin-bottom:0">
						<tr>
							<td style="vertical-align:top">
								<b>Principal mRNA</b>:  <xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/info/@geneSymbol"/>&#160;<i><xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/info/@species"/></i>
							</td>
							<td style="padding-left:0.5em;vertical-align:top">
								<b>Comparison mRNA</b>:  <xsl:value-of select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/@geneSymbol"/>&#160;<i><xsl:value-of select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/@species"/></i>
							</td>
							<td>
							<input type="checkbox" id="check" /><label class="checkBoxButtonLabel" for="check">Details</label>
							</td>
						</tr>
					</table>
					
					</div>		
					<div id="infoDetails" class="rounded-corners">
						<table id="infoTable" class="rounded-corners">
							<thead style="padding-left:0.5em;text-align:left">
								<th></th>
								<th>Principal mRNA Details</th>
								<th>Comparison mRNA Details</th>
							</thead>
							<tbody class="rounded-corners">
								<tr>
									<td class="partition" style="vertical-align:top">RefSeq mRNA Accession #</td>
									<td class="partition" ><xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/info/@accessionNumber"/></td>
									<td> <xsl:value-of select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/@accessionNumber"/></td>
								</tr>
								<tr>
									<td class="partition" >RefSeq mRNA GI #</td>
									<td class="partition" ><xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/info/@giNumber"/></td>
									<td><xsl:value-of select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/@giNumber"/></td>
								</tr>
								<tr>
									<td class="partition" >mRNA Description</td>
									<td class="partition" ><xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/info/@geneName"/></td>
									<td><xsl:value-of select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/@geneName"/></td>
								</tr>
								<tr>
									<td class="partition" >mRNA Length</td>
									<td class="partition" ><xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/info/mrnaLength"/></td>
									<td><xsl:value-of select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/mrnaLength"/></td>
								</tr>
								<tr>
									<td class="partition" >5' UTR position</td>
									<td class="partition" ><xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/info/utr5s"/></td>
									<td><xsl:value-of select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/utr5s"/></td>
								</tr>
								<tr>
									<td class="partition" >CDS position</td>
									<td class="partition" ><xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/info/cds"/></td>
									<td><xsl:value-of select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/cds"/></td>
								</tr>
								<tr>
									<td class="partition" >3' UTR position</td>
									<td class="partition" ><xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/info/utr3s"/></td>
									<td><xsl:value-of select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/utr3s"/></td>
								</tr>
								<tr>
									<td class="partition" >Poly-A Signals</td>
									<td class="partition" >
										<xsl:for-each select="/qgrs/alignmentResults/pairResult/principle/mrna/info/polyAsignal">
											<p>
												<xsl:value-of select="." />
											</p>
										</xsl:for-each>
									</td>
									<td>
										<xsl:for-each select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/polyAsignal">
											<p>
												<xsl:value-of select="." />
											</p>
										</xsl:for-each>
									</td>
								</tr>
								<tr>
									<td class="partition" >Poly-A Sites</td>
									<td class="partition" >
										<xsl:for-each select="/qgrs/alignmentResults/pairResult/principle/mrna/info/polyAsite">
											<p>
												<xsl:value-of select="." />
											</p>
										</xsl:for-each>
									</td>
									<td>
										<xsl:for-each select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/polyAsite">
											<p>
												<xsl:value-of select="." />
											</p>
										</xsl:for-each>
									</td>
								</tr>
								<tr>
									<td class="partition col1" >QGRS Mapped</td>
									<td class="partition col2" ><xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/@numQgrs"/></td>
									<td><xsl:value-of select="/qgrs/alignmentResults/pairResult/comparison/mrna/@numQgrs"/></td>
								</tr>
								
							</tbody>
						</table>
						<!-- <div>
							<h2>Cache Profile</h2>
							<table class="rounded-corners">
								<tbody class="rounded-corners">
								<tr>
									<td>Principal G-Quadruplex Identification</td>
									<td><xsl:value-of select="/qgrs/alignmentResults/cacheStatus/principleQuadruplexCached"/></td>
								</tr>
								<tr>
									<td>Comparison G-Quadruplex Identification</td>
									<td><xsl:value-of select="/qgrs/alignmentResults/cacheStatus/comparisonQuadruplexCached"/></td>
								</tr>
								<tr>
									<td>Sequence Alignment</td>
									<td><xsl:value-of select="/qgrs/alignmentResults/cacheStatus/alignmentCached"/></td>
								</tr>
								<tr>
									<td>Homology Results</td>
									<td><xsl:value-of select="/qgrs/alignmentResults/cacheStatus/homologyCached"/></td>
								</tr>
								</tbody>
							</table>
							<p>:</p>
						</div> -->
					</div>
					
				</div>
				<div class="container" style="padding:0px">
					<div class="ui-widget-header ui-corner-top" style="padding:0.25em">QGRS Homology Map  <span style="float:right" data-help-key="help_homologyMap" class="context_help ui-icon ui-icon-info"/></div>
					<div class="ui-widget-content ui-corner-bottom" style="position:relative;padding:1em">
							
						<div style="float:right; width:225px; text-align:right"  >
							<form id="filterForm" action="align-complete" method="get">
							<button title="Reset homology map to show entire sequence" href="#" id="resetZoom" type="button" style="font-size:8pt">Reset Zoom</button>
						
							<p style="margin:5px;padding:0">
								<label for="similarityCutoff" style="margin-bottom:0; font-size:8pt">Min QGRS Homology Score: <span id="homologyScoreCutoffValue"><xsl:value-of select='format-number(qgrs/viewParams/similarityCutoff, "0.00")'/></span></label>
								<input type="hidden" id="similarityCutoff" name="similarityCutoff" value="{qgrs/viewParams/similarityCutoff}"/>
								<div id="similarityCutoffSlider" ></div>
							</p>
							<p style="margin:5px;padding:0">
								<label for="tetradCutoff" style="font-size:8pt">Minimum # of Tetrads: <span id="tetradCutoffValue"><xsl:value-of select='qgrs/viewParams/tetradCutoff'/></span></label>
								<input type="hidden" id="tetradCutoff" name="tetradCutoff" value="{qgrs/viewParams/tetradCutoff}"/>
								<div id="tetradCutoffSlider" ></div>
							</p>
							<p style="margin:5px;padding:0">
								<label for="gScoreCutoff" style="font-size:8pt">Minimum G-Score: <span id="gScoreCutoffValue"><xsl:value-of select='format-number(qgrs/viewParams/gScoreCutoff, "#.#")'/></span></label>
								<input type="hidden" id="gScoreCutoff" name="gScoreCutoff" value="{qgrs/viewParams/gScoreCutoff}"/>
								<div id="gScoreCutoffSlider" ></div>
							</p>
							
							<!-- <button id="filterButton" style="float:right;margin-left:.25em;margin-top:1em" type="submit">Apply Filter</button> -->
								
							<input type="hidden" id="overlapFilter" name="overlapFilter" value="{qgrs/viewParams/overlapFilter}"/>
							<input type="hidden" id="seq1StartIndex" name="seq1StartIndex" value="{qgrs/viewParams/seq1StartIndex}"/>
							<input type="hidden" id="seq1EndIndex" name="seq1EndIndex" value="{qgrs/viewParams/seq1EndIndex}"/>
							<input type="hidden" id="seq2StartIndex" name="seq2StartIndex" value="{qgrs/viewParams/seq2StartIndex}"/>
							<input type="hidden" id="seq2EndIndex" name="seq2EndIndex" value="{qgrs/viewParams/seq2EndIndex}"/>
							<input type="hidden" name="pageNumber" id="pageNumber_filter" value="1"/>
							
						</form>
						</div>
						<p style="width:70%;position:absolute;z-index:1000;margin-right:225px;text-align:center; font-size:8pt;top:0px">
						Click and drag your mouse over a region to zoom in.  Use the sliders on the right to filter results. <a href="#" data-help-key="help_homologyMap" class="context_help">More information</a>
						</p>
						<div id="container" style="position:relative; width: 75%; height: 185px; margin: 0; margin-top:1em">
							
						</div>
					</div>
				</div>
				
				
				<div class="container" style="padding:0px">
					<div class="ui-widget-header ui-corner-top" style="padding:0.25em">
					<span style="float:right; margin-left:2em" data-help-key="help_sequenceViewer" class="context_help ui-icon ui-icon-info"/><span style="font-size:10pt;float:right"><b>Sequence Similarity</b>:   <xsl:value-of select="format-number(/qgrs/alignmentResults/pairResult/@alignmentPercent, '#.0%')"/></span>
					Sequence View <span style="margin-left:2em;font-size:10pt; font-weight:normal">Principal mRNA <a target ="_blank" href="{/qgrs/alignmentResults/pairResult/principle/mrna/info/ncbiLink}"><xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/info/@accessionNumber"/></a> x Comparison mRNA <a target ="_blank" href="{/qgrs/alignmentResults/pairResult/comparison/mrna/info/ncbiLink}"><xsl:value-of select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/@accessionNumber"/></a></span>
					</div>
					<div class="ui-widget-content ui-corner-bottom" style="padding:0em">
				
				<div style="background-color:white; padding-bottom:0.5px">
					<p style="margin:0; padding:0; font-size:8pt; text-align:right; margin-right:1em">Click on a pair result row in the table below to highlight and scroll to it in the sequence view.</p>
					<div class="sequenceContainer" style="height:100px; overflow:auto;">
					
					<table><tr>
					<xsl:for-each select="/qgrs/alignmentResults/pairResult/sequence/section">
						<td class="sequenceSection">
							<span class="noGapIndex"><xsl:value-of select="@principle_startDisplay"/>&#160;</span><br/>
							<span class="principleSection"><xsl:value-of select="principleSection"/></span><br/>
							<span class="matchSection"><xsl:value-of select="matchSection"/></span><br/>
							<span class="comparisonSection"><xsl:value-of select="comparisonSection"/></span><br/>
							<span class="noGapIndex"><xsl:value-of select="@comparison_startDisplay"/>&#160;</span>
						</td>
					</xsl:for-each>
					</tr></table>
					</div>
				</div> 
				</div>
				</div>
				
				
				<div class="container" style="padding:0px">
					<div class="ui-widget-header ui-corner-top" style="padding:0.25em">Homologous QGRS Pairs:   <xsl:value-of select="qgrs/viewParams/totalRecords"/> Results<span style="float:right; " data-help-key="help_results" class="context_help ui-icon ui-icon-info"/></div>
					<div class="ui-widget-content ui-corner-bottom" style="padding:0em">
					
				<div style="width:100%;">
					<table class="pairTable" style="width:100%" cellspacing="0" >
					<thead>
					<tr >
						<th class="idCol partition">Pair ID</th>
						<th class="geneCol">mRNA</th>
						<th class="regionCol">Region</th>
						<th class="positionCol">Position</th>
						<th class="tetradsCol">Tetrads</th>
						<th class="gScoreCol partition">G-Score</th>
						<th class="hScoreCol">QGRS Homology Score</th>
					</tr>
					</thead>
					</table>
				</div>
				<div id="pairTableContainer" style="width:100%;max-height:275px; overflow:auto;">
				<table class="pairTable" style="width:100%" cellspacing="0" >
					<tbody >
					<xsl:for-each select="/qgrs/alignmentResults/pairResult/gQuadSimResult/result">
						<tr title="Click to highlight in sequence viewer" data-seq1start="{@gq1_start_gap}" data-seq1end="{@gq1_end_gap}" data-seq2start="{@gq2_start_gap}" data-seq2end="{@gq2_end_gap}" data-seq1tetrad="{@gq1_glist}" data-seq2tetrad="{@gq2_glist}">
							<td  class="idCol partition"><xsl:value-of select="@num_gq1"/>-<xsl:value-of select="@num_gq2"/></td>
							
							<td class="geneCol">
							<xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/info/@geneSymbol"/>: <i><xsl:value-of select="/qgrs/alignmentResults/pairResult/principle/mrna/info/@species"/></i><br/>
							<xsl:value-of select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/@geneSymbol"/>: <i><xsl:value-of select="/qgrs/alignmentResults/pairResult/comparison/mrna/info/@species"/></i>
							</td>
							
							<td class="regionCol"><xsl:value-of select="@gq1_region"/><br/><xsl:value-of select="@gq2_region"/></td>
							<td class="positionCol"><xsl:value-of select="@gq1_startDisplay"/>-<xsl:value-of select="@gq1_endDisplay"/><br/><xsl:value-of select="@gq2_startDisplay"/>-<xsl:value-of select="@gq2_endDisplay"/></td>
							<td class="tetradsCol"><xsl:value-of select="@gq1_tetrad"/><br/><xsl:value-of select="@gq2_tetrad"/></td>
							<td class="gScoreCol partition" ><xsl:value-of select="@gq1_score"/><br/><xsl:value-of select="@gq2_score"/></td>
							
							<td class="hScoreCol"><xsl:value-of select="@score"/></td>
						</tr>
					</xsl:for-each>
					</tbody>
				</table>
				</div>
				</div>
				</div>
				
				
				</div>
				
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>