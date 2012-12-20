<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" version="4.0" indent="yes"/>
	<xsl:template name="css_includes">
		<link rel="stylesheet" type="text/css" href="{$cssPath}qgrs.css" />
		<link rel="shortcut icon" href="{$imagePath}favicon.png" />
	</xsl:template>
	
	
	<xsl:template name="ie_meta">
		 <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
	</xsl:template>
	
	<xsl:template name="footer">
		<div class="footer">
			Footer
		</div>
	</xsl:template>
	
	<xsl:template name="javascript_includes">
		<link rel="stylesheet" type="text/css" href="{$jsPath}/plugins/ui.spinner.css" />
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
		<script type="text/javascript" src="{$jsPath}jquery-ui-1.8.16.custom.min.js"></script>
		<script src="{$jsPath}charts/js/highcharts.js"></script>
		<script src="{$jsPath}qgrs.js"></script>
		<script src="{$jsPath}plugins/jquery.scrollTo-1.4.2-min.js"></script>
		<script type="text/javascript" src="{$jsPath}/multiselect/jquery.multiselect.min.js"></script>
		<link rel="stylesheet" href="{$cssPath}custom-theme/jquery-ui-1.8.16.custom.css" type="text/css" />
		<link rel="stylesheet" href="{$jsPath}/multiselect/jquery.multiselect.css" type="text/css" />	
		
		<script  type="text/javascript" src="{$jsPath}plugins/ui.spinner.min.js"></script>
	</xsl:template>
	
	<xsl:template name="viewParamHiddenInputs">
			<input type="hidden" id="similarityCutoff" name="similarityCutoff" value="{qgrs/viewParams/similarityCutoff}"/>
			<input type="hidden" id="gScoreCutoff" name="gScoreCutoff" value="{qgrs/viewParams/gScoreCutoff}"/>
			<input type="hidden" id="overlapFilter" name="overlapFilter" value="{qgrs/viewParams/overlapFilter}"/>
			<input type="hidden" id="seq1StartIndex" name="seq1StartIndex" value="0"/>
			<input type="hidden" id="seq1EndIndex" name="seq1EndIndex" value="MAX"/>
			<input type="hidden" id="seq2StartIndex" name="seq2StartIndex" value="0"/>
			<input type="hidden" id="seq2EndIndex" name="seq2EndIndex" value="MAX"/>
			<input type="hidden" id="tetradCutoff" name="tetradCutoff" value="{qgrs/viewParams/tetradCutoff}"/>
	</xsl:template>
	
	<xsl:template name="pager">
		<div style="float:right;padding-right:1em;padding-top:5px;vertical-align:middle" class="pager">
			<button class="pageNavButton" id="firstPageButton" title="First page">&lt;&lt;</button>
			<button class="pageNavButton" id="previousPageButton" title="Previous page">&lt;</button> 
			<span style="padding-top:5px">Page 
			<xsl:value-of select="qgrs/dbCriteria/dbPageNumber"/> of <xsl:value-of select="qgrs/dbCriteria/dbTotalPages"/>
			</span>
			<button class="pageNavButton" id="nextPageButton" title="Next page">&gt;</button>
			<button class="pageNavButton" id="lastPageButton" title="Last page">&gt;&gt;</button> 
		</div>
	</xsl:template>
	
	<xsl:template name="dbCriteria">
		<xsl:param name="browse">browse-gene-h</xsl:param>
		<script type="text/javascript">
			$(document).ready(function() {
			
				
				$("#dbRegion13UTRC").change( function() {
					$('#dbRegion13UTRC').is(":checked") ? $('#dbRegion13UTR').val("on") : $('#dbRegion13UTR').val("false");							
				});
				$("#dbRegion1CDSC").change( function() {
					$('#dbRegion1CDSC').is(":checked") ? $('#dbRegion1CDS').val("on") : $('#dbRegion1CDS').val("false");							
				});
				$("#dbRegion15UTRC").change( function() {
					$('#dbRegion15UTRC').is(":checked") ? $('#dbRegion15UTR').val("on") : $('#dbRegion15UTR').val("false");							
				});
				
				<!-- $("#dbRegion23UTRC").change( function() {
					$('#dbRegion23UTRC').is(":checked") ? $('#dbRegion23UTR').val("on") : $('#dbRegion23UTR').val("false");							
				});
				$("#dbRegion2CDSC").change( function() {
					$('#dbRegion2CDSC').is(":checked") ? $('#dbRegion2CDS').val("on") : $('#dbRegion2CDS').val("false");							
				});
				$("#dbRegion25UTRC").change( function() {
					$('#dbRegion25UTRC').is(":checked") ? $('#dbRegion25UTR').val("on") : $('#dbRegion25UTR').val("false");							
				}); -->
				
				$("#browse-qgrs-p").click(  function() {
					$("#dbFilterSide").val("principle");
					$("#navigationFilterForm").attr("action", "quadruplex-list");
					$("#dbPageNumber").val(1);
					$("#navigationFilterForm").submit();
				});
				<!-- $("#browse-qgrs-c").click(  function() {
					$("#dbFilterSide").val("comparison");
					$("#navigationFilterForm").attr("action", "quadruplex-list");
					$("#dbPageNumber").val(1);
					$("#navigationFilterForm").submit();
				}); -->
				
				$("#browse-qgrs-h").click(  function() {
					$("#navigationFilterForm").attr("action", "homology-list");
					$("#dbPageNumber").val(1);
					$("#navigationFilterForm").submit();
				});
				$("#browse-gene-h").click(  function() {
					$("#navigationFilterForm").attr("action", "align-list");
					$("#dbPageNumber").val(1);
					$("#navigationFilterForm").submit();
				});
				
				$("#browse-gene-p").click(  function() {
					$("#dbFilterSide").val("principle");
					$("#navigationFilterForm").attr("action", "gene-list");
					$("#dbPageNumber").val(1);
					$("#navigationFilterForm").submit();
				});
				
				<!-- $("#browse-gene-c").click(  function() {
					$("#dbFilterSide").val("comparison");
					$("#navigationFilterForm").attr("action", "gene-list");
					$("#dbPageNumber").val(1);
					$("#navigationFilterForm").submit();
				});
				 -->
				$("#clearAll").click( function()  {
					$("#dbGeneId1").val("");
					<!-- $("#dbGeneId2").val(""); -->
					$("#dbGeneSymbol1").val("");
					<!-- $("#dbGeneSymbol2").val(""); -->
					$("#dbQgrsId1").val("");
					<!-- $("#dbQgrsId2").val(""); -->
					$("#dbSpecies1").val("Homo sapiens");
					$("#dbSpecies2").val("");
					$("#dbTetrads1Slider").slider( "value" , 2 );
					$("#dbTetrads1Value").text(2);
					$("#dbMinTetrads1").val(2);
					<!-- $("#dbTetrads2Slider").slider( "value" , 2 );
					$("#dbTetrads2Value").text(2);
					$("#dbMinTetrads2").val(2); -->

					$("#dbGScore1Slider").slider( "value" , 35 );
					$("#dbGScore1Value").text(35);
					$("#dbGScore1").val(35);
					<!-- $("#dbGScore2Slider").slider( "value" , 35 );
					$("#dbGScore2Value").text(35);
					$("#dbGScore2").val(35); -->
					
					$("#dbOverallSimilaritySlider").slider( "value" , .9 );
					$("#dbOverallSimilarityValue").text("90%");
					$("#dbOverallSimilarity").val(.9);
					
					$("#dbMinAlignmentScoreSlider").slider( "value" , 60 );
					$("#dbMinAlignmentScoreValue").text("60%");
					$("#dbMinAlignmentScore").val(60);
					
					$("#dbRegion15UTRC").attr("checked","checked").button( "refresh" );
					$("#dbRegion15UTR").val("on");
					
					<!-- $("#dbRegion25UTRC").attr("checked","checked").button( "refresh" );
					$("#dbRegion25UTR").val("on"); -->
					
					$("#dbRegion13UTRC").attr("checked","checked").button( "refresh" );
					$("#dbRegion13UTR").val("on");
					
					<!-- $("#dbRegion23UTRC").attr("checked","checked").button( "refresh" );
					$("#dbRegion23UTR").val("on"); -->
					
					$("#dbRegion1CDSC").attr("checked","checked").button( "refresh" );
					$("#dbRegion1CDS").val("on");
					
					<!-- $("#dbRegion2CDSC").attr("checked","checked").button( "refresh" );
					$("#dbRegion2CDS").val("on"); -->
					
				});
				$(".clearButton").click ( function() {
					$("#" + $(this).attr("data-for")).val("");
				});
				$(".clearButton").hide().css("cursor", "pointer").css("width", "20px").css("height", "20px").css("float", "right");
				$(".clearButton").parent().mouseenter( function() { 
					if ( $("#" + $(this).children(".clearButton").attr("data-for")).val().length > 0 ) {
						$(this).children(".clearButton").fadeIn('fast');
					}
				});
				$(".clearButton").parent().mouseleave( function() { 
					$(this).children(".clearButton").fadeOut('fast');
				});
				$(".clearButton").parent().css("width", "200px").css("height", "20px");
				$(".clearButton").each ( function() {
					$("#"+$(this).attr("data-for")).css("width", "165px");
				});
				
				$("#refreshButton").click( function () {
					$('form:first').submit();
					
				});
				
				$('form:first').submit( function () {
					$("#status").text(" loading... please wait");
					$("#loadingScreen").dialog({
						modal: true, draggable:false, resizable:false
					});
					$(".ui-dialog-titlebar").hide();
				});
				
				
				
				
			});
		</script>		
		<div class="container" style="padding:0px">
			<div class="ui-widget-header ui-corner-top">
				<p style="padding:0.5em;margin:0">Search Criteria
				<a href="javascript:void(0)" style="margin-left:2em;font-size:small;margin-right:0.5em;" id="checkCriteria">Show Details</a>
				</p>
			</div>
			
			<input type="hidden" name="dbPageNumber" id="dbPageNumber" value="{qgrs/dbCriteria/dbPageNumber}"/>
			<input type="hidden" name="dbTotalPages" id="dbTotalPages" value="{qgrs/dbCriteria/dbTotalPages}"/>
			<input type="hidden" name="alignmentId" id="alignmentId"/>
			<input type="hidden" name="dbFilterState" id="dbFilterState" value="{qgrs/dbCriteria/dbFilterState}"/>
			<input type="hidden" name="dbFilterSide" id="dbFilterSide" value="{qgrs/dbCriteria/dbFilterSide}"/>
			
			<div id="loadingScreen" style="display:none;padding:3em;">
				<p>Loading... please wait.</p>
			</div>
			
			<div id="criteriaDetails" class="ui-widget-content ui-corner-bottom " style="display:{qgrs/dbCriteria/dbFilterState};padding:0em;overflow: auto;width: 100%">
				<div style="float:right;font-size:xx-small">
				Load Time:  <xsl:value-of select="/qgrs/dbCriteria/queryTime"/>
			</div>
				<div class="filterCriteriaSubPanel ui-corner-all" >
				<table style="width:100%">
					<caption style="width:100%">mRNA Criteria</caption>
					<tbody>
					<tr>
						<td>
							<label title="Seperate accession numbers using semicolons, blank field allows all genes.">mRNA ID:</label>
						</td>
						<td>	
							<img class="clearButton" style="display:none" data-for="dbGeneId1" src="{$imagePath}clear.png"/>
							<input type="text" id="dbGeneId1" name="dbGeneId1" value="{qgrs/dbCriteria/dbGeneId1}"/>
						</td>
						
					</tr>
					<tr>
						<td>
							<label>Gene Symbol:</label>
						</td>
						<td>
							<img class="clearButton" style="display:none" data-for="dbGeneSymbol1" src="{$imagePath}clear.png"/>
							<input type="text" id="dbGeneSymbol1" name="dbGeneSymbol1" value="{qgrs/dbCriteria/dbGeneSymbol1}"/>
						</td>
						
					</tr>
					<tr>
						<td>
							<label>Organism:</label>
						</td>
						<td>
							<select id="dbSpecies1" name="dbSpecies1" >
								<option value="Homo sapiens">Homo sapiens</option>
							</select>
							<span style="margin-left:0.25em;margin-right:0.25em">x</span>
							<select id="dbSpecies2" name="dbSpecies2" >
							<xsl:for-each select="qgrs/speciesList/species">
								<xsl:choose>
									<xsl:when test="@value=/qgrs/dbCriteria/dbSpecies2">
										<option value="{@value}" selected="selected"><xsl:value-of select="@display"/></option>
									</xsl:when>
									<xsl:otherwise>
										<option value="{@value}"><xsl:value-of select="@display"/></option>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
							</select>
						</td>
						
					</tr>
					<tr>
						<td>
							<label for="dbMinAlignmentScore">Min. Sequence<br/>Similarity: </label>
						</td>
						<td>
							<span id="dbMinAlignmentScoreValue" style="float:right"><xsl:value-of select="qgrs/dbCriteria/dbMinAlignmentScore"/>%</span>
							<input type="hidden" id="dbMinAlignmentScore" name="dbMinAlignmentScore" value="{qgrs/dbCriteria/dbMinAlignmentScore}"/>
							<div style="margin-right:4em;">
								<div id="dbMinAlignmentScoreSlider" class="sliderContainer"/>
							</div>
						</td>
						
					</tr>
					</tbody>
				</table>
				</div>
			
			<div class="filterCriteriaSubPanel ui-corner-all">
					<table style="width:100%">
					<caption style="width:100%">QGRS Criteria</caption>
					
					<tbody>
					<tr>
						<td>
							<label>QGRS ID</label>
						</td>
						<td>
							<img class="clearButton" style="display:none" data-for="dbQgrsId1" src="{$imagePath}clear.png"/>
							<input style="width:100%" type="text" id="dbQgrsId1" name="dbQgrsId1" value="{qgrs/dbCriteria/dbQgrsId1}"/>
						</td>
						
					</tr>
					
					<tr>
						<td>
							<label>Min. # Tetrads:</label>
						</td>
						<td>
							<span id="dbTetrads1Value" style="float:right"><xsl:value-of select="qgrs/dbCriteria/dbMinTetrads1"/></span>
							<input type="hidden" id="dbMinTetrads1" name="dbMinTetrads1" value="{qgrs/dbCriteria/dbMinTetrads1}"/>
							<div style="margin-right:2em" >
								<div id="dbTetrads1Slider" class="sliderContainer"/>
							</div>
						</td>
						
					</tr>
					<tr>
						<td>
							<label for="dbGScore1">Min. G-Score: </label>
						</td>
						<td>
							<span id="dbGScore1Value" style="float:right"><xsl:value-of select="qgrs/dbCriteria/dbGScore1"/></span>
							<input type="hidden" id="dbGScore1" name="dbGScore1" value="{qgrs/dbCriteria/dbGScore1}"/>
							<div style="margin-right:2em" >
								<div id="dbGScore1Slider" class="sliderContainer"/>
							</div>
						</td>
						
					</tr>
					<tr>
						<td>
							<label>In Region:</label>
						</td>
						<td>
							<input type="hidden" id="dbRegion13UTR" name="dbRegion13UTR" class="dbCheckboxBacking" value="{qgrs/dbCriteria/dbRegion13UTR}"/>
							<input type="hidden" id="dbRegion1CDS" name="dbRegion1CDS" class="dbCheckboxBacking"  value="{qgrs/dbCriteria/dbRegion1CDS}"/>
							<input type="hidden" id="dbRegion15UTR" name="dbRegion15UTR" class="dbCheckboxBacking"  value="{qgrs/dbCriteria/dbRegion15UTR}"/>
							<div class="regionButtonSet">
								
								<xsl:element name="input">
									<xsl:attribute name="type">checkbox</xsl:attribute>
									<xsl:attribute name="id">dbRegion15UTRC</xsl:attribute>
									<xsl:attribute name="name">dbRegion15UTRC</xsl:attribute>
									<xsl:if test="qgrs/dbCriteria/dbRegion15UTR = 'on'">
										<xsl:attribute name="checked">checked</xsl:attribute> 
									</xsl:if>
								</xsl:element>
								<label for="dbRegion15UTRC">5' UTR</label>
								
								<xsl:element name="input">
									<xsl:attribute name="type">checkbox</xsl:attribute>
									<xsl:attribute name="id">dbRegion1CDSC</xsl:attribute>
									<xsl:attribute name="name">dbRegion1CDSC</xsl:attribute>
									<xsl:if test="qgrs/dbCriteria/dbRegion1CDS = 'on'">
										<xsl:attribute name="checked">checked</xsl:attribute> 
									</xsl:if>
								</xsl:element>
								<label for="dbRegion1CDSC">CDS</label>
								
								
								<xsl:element name="input">
									<xsl:attribute name="type">checkbox</xsl:attribute>
									<xsl:attribute name="id">dbRegion13UTRC</xsl:attribute>
									<xsl:attribute name="name">dbRegion13UTRC</xsl:attribute>
									<xsl:if test="qgrs/dbCriteria/dbRegion13UTR = 'on'">
										<xsl:attribute name="checked">checked</xsl:attribute> 
									</xsl:if>
								</xsl:element>
								<label for="dbRegion13UTRC">3' UTR</label>
								
							</div>
						</td>
						
				</tr>
				
				<tr>
					<td><label>Min. Homology<br/> Threshold:</label></td>
					<td>
						<input type="hidden" id="dbOverallSimilarity" name="dbOverallSimilarity" value="{qgrs/dbCriteria/dbOverallSimilarity}"/>
						<span style="float:right" id="dbOverallSimilarityValue"><xsl:value-of select="format-number(qgrs/dbCriteria/dbOverallSimilarity, '#.00')"/></span>
						<div style="margin-right:4em;" >
							<div id="dbOverallSimilaritySlider" class="sliderContainer"/>
						</div>
					</td>
				</tr>
				
				
					</tbody>
					</table>
				</div>
				<div style="clear:both;display:block;margin:0.0em;padding:0.5em;">
	            	<span id="refreshButton" style="padding:0;vertal-align:middle">Search<img title="Refresh / Apply Filter" src="{$imagePath}/refresh.png" style="margin-left:0.5em;padding-bottom:7px;vertical-align:middle;cursor:pointer;width:35px; height:35px"/></span>
	            	<a href="javascript:void(0)" id="clearAll">reset search parameters</a>
	                <span style="padding-top:0.2em; color:orange" id="status"/>
	            </div>
				
			</div>
			
		</div>
		<div id="browseBar" class="container ui-widget-content ui-corner-all" style="padding:0.25em">
		<span class="radioContainer" style="font-size:8pt">
			<label><b>Browse:  </b></label>
			
			<xsl:element name="input">
				<xsl:attribute name="type">radio</xsl:attribute>
				<xsl:attribute name="name">browse</xsl:attribute>
				<xsl:attribute name="id">browse-gene-p</xsl:attribute>
				<xsl:attribute name="value">browse-gene-p</xsl:attribute>
				<xsl:if test="$browse = 'browse-gene-p'">
					<xsl:attribute name="checked">checked</xsl:attribute> 
				</xsl:if>
				
			</xsl:element>	
			<label for="browse-gene-p">mRNA (Human)</label>
			
			
							
			<xsl:element name="input">
				<xsl:attribute name="type">radio</xsl:attribute>
				<xsl:attribute name="name">browse</xsl:attribute>
				<xsl:attribute name="id">browse-gene-h</xsl:attribute>
				<xsl:attribute name="value">browse-gene-h</xsl:attribute>
				<xsl:if test="$browse = 'browse-gene-h'">
					<xsl:attribute name="checked">checked</xsl:attribute> 
				</xsl:if>
			</xsl:element>	
			<label for="browse-gene-h">mRNA Homologs</label>
			
			<xsl:element name="input">
				<xsl:attribute name="type">radio</xsl:attribute>
				<xsl:attribute name="name">browse</xsl:attribute>
				<xsl:attribute name="id">browse-qgrs-p</xsl:attribute>
				<xsl:attribute name="value">browse-qgrs-p</xsl:attribute>
				<xsl:if test="$browse = 'browse-qgrs-p'">
					<xsl:attribute name="checked">checked</xsl:attribute> 
				</xsl:if>
			</xsl:element>
			<label for="browse-qgrs-p">QGRS (Human)</label>
			
			
			
			<xsl:element name="input">
				<xsl:attribute name="type">radio</xsl:attribute>
				<xsl:attribute name="name">browse</xsl:attribute>
				<xsl:attribute name="id">browse-qgrs-h</xsl:attribute>
				<xsl:attribute name="value">browse-qgrs-h</xsl:attribute>
				<xsl:if test="$browse = 'browse-qgrs-h'">
					<xsl:attribute name="checked">checked</xsl:attribute> 
				</xsl:if>
			</xsl:element>
			<label for="browse-qgrs-h">QGRS Homologous Pairs</label>
			
			
		</span>
		</div>
	</xsl:template>
	<xsl:template name="viewParamInputs">
			<p>
			<span>
				<label for="similarityCutoff">Homology Score Cutoff</label>
				<input type="text" id="similarityCutoff" name="similarityCutoff" value="{qgrs/viewParams/similarityCutoff}"/>
			</span>
			<span>
				<label for="gScoreCutoff">G-Score Cutoff</label>
				<input type="text" id="gScoreCutoff" name="gScoreCutoff" value="{qgrs/viewParams/gScoreCutoff}"/>
			</span>
			<span>
				<label for="overlapFilter">Show top </label>
				<input type="text" id="overlapFilter" name="overlapFilter" value="{qgrs/viewParams/overlapFilter}"/>
				<span> QGRS alignments for each pair</span>
			</span>
			</p>
			<p>		
			<span>
				<label for="seq1StartIndex">Sequence 1 starting index</label>
				<input type="text" id="seq1StartIndex" name="seq1StartIndex" value="{qgrs/viewParams/seq1StartIndex}"/>
			</span>
					
			<span>
				<label for="seq1EndIndex">Sequence 1 ending index</label>
				<input type="text" id="seq1EndIndex" name="seq1EndIndex" value="{qgrs/viewParams/seq1EndIndex}"/>
			</span>
			</p>	
			<p>
			<span>
				<label for="seq2StartIndex">Sequence 2 starting index</label>
				<input type="text" id="seq2StartIndex" name="seq2StartIndex" value="{qgrs/viewParams/seq2StartIndex}"/>
			</span>
					
			<span>
				<label for="seq2EndIndex">Sequence 2 ending index</label>
				<input type="text" id="seq2EndIndex" name="seq2EndIndex" value="{qgrs/viewParams/seq2EndIndex}"/>
			</span>
				</p>
			
		
	</xsl:template>
</xsl:stylesheet>
