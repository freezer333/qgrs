<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html"  indent="yes" />
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
					th { text-align:left;}
					table { border-collapse:collapse; margin-bottom:1em; width:100%; margin-left:auto; margin-right:auto;  }
					.principle, .comparison {font-style:italic}
				</style>
				
				<!-- Chart/Map Creations -->
				<script type="text/javascript">
					$(document).ready(function() {
					
						if ( $.browser.msie ) {
							$("<p>For best experience (especially when comparing longer sequences), consider viewing this application in Google Chrome, Mozilla Firefox, or Apple Safari</p>")
								.addClass("warning").addClass("rounded-bottom").addClass("shadow").css("display", "none").prependTo("body").delay(2000).slideDown(1000).delay(10000).slideUp();
						}
					
					});
				
				</script>
				
			</head>
			<body> ming was here
				<xsl:call-template name="header">
					<xsl:with-param name="title">: Sequence Input</xsl:with-param>
					<xsl:with-param name="gotodb">true</xsl:with-param>
					<xsl:with-param name="appname">Predictor</xsl:with-param>
				</xsl:call-template>xyz
				
				<xsl:if test="/qgrs/AlertMessage/@show='true'">
				<p class="alert"><xsl:value-of select="/qgrs/AlertMessage"/></p>
				</xsl:if>
				
				<div class="ui-widget " style="margin:1em;">
				<div class="ui-widget-header  rounded-top" style="padding:0.25em">
				Sequence Input
				</div>
				
				<div class="ui-widget-content rounded-bottom" style="font-size:smaller;">
					<table>
					<tr>
					<td style="width:600px;">
					<form id="startForm" action="align-start" name="align-start" method="POST" style="padding:1em;">
					
					<div id="inputContainer" >
						<div style="margin-bottom:2em;">
							<label style="float:left;width:10em" for="seq1">Principal mRNA</label>
									<span class="radioContainer" style="font-size:8pt">
										<input type="radio" id="seq1IdOption" value= "seq1IdOption" name="seq1Option" checked="checked"/><label for="seq1IdOption">RefSeq mRNA Accession/GI #</label>
										<input type="radio" id="seq1CharsOption" value= "seq1CharsOption" name="seq1Option" /><label for="seq1CharsOption">Raw / FASTA Sequence Input</label>
									</span>
									<br/>
									<input style="margin-top:0.5em;margin-left:12em" type="text" id="seq1" name="seq1"/>
									<textarea style="display:none;margin-top:0.5em;" rows="20" cols="70" id="seq1_chars" name="seq1_chars"></textarea>
						</div>
						<div style="margin-bottom:2em;">
									<label  style="float:left;width:10em" for="seq2">Comparison mRNA</label>	
									<span class="radioContainer" style="margin-bottom:1em; font-size:8pt">
										<input type="radio" id="seq2IdOption" value= "seq2IdOption" name="seq2Option" checked="checked"/><label for="seq2IdOption">RefSeq mRNA Accession/GI #</label>
										<input type="radio" id="seq2CharsOption" value= "seq2CharsOption"  name="seq2Option" /><label for="seq2CharsOption">Raw / FASTA Sequence Input</label>
										<!-- <input type="radio" id="seq2HomologOption" value= "seq2HomologOption" name="seq2Option" /><label for="seq2HomologOption">Homolog Search</label>
										 -->
									</span>
									<br/>
									<input style="margin-top:0.5em;margin-left:12em" type="text" id="seq2" name="seq2"/>
									<textarea style="display:none; margin-top:0.5em;" rows="20" cols="70" id="seq2_chars" name="seq2_chars"></textarea>
									<p id="seq2_homologs" class="ui-state-highlight ui-corner-all" style="display:none;  padding:1em;margin-top:0.5em;margin-left:12em" >Sorry, Homology Search is not yet available</p>
						</div>
						<div id="buttonBar"><button style="margin-left:12em;padding:0.5em" type="submit">Run Analysis</button></div>
						
						
					</div>
					<xsl:call-template name="viewParamHiddenInputs"/>
					<input type="hidden" name="pageNumber" id="pageNumber" value="1"/>
					<input type="hidden" name="pageSize" id="pageSize" value="100"/>
					
				</form>
				</td>
				<td style="vertical-align:top;">
				
					<div  style="padding-right:1em;text-align:left;font-size:9pt;">
						<p style="margin-bottom:0;">
								The goal of <b>QGRS-H Predictor</b> is to map and analyze phylogenetically conserved putative <b>Q</b>uadruplex forming '<b>G</b>'-<b>R</b>ich <b>S</b>equences (QGRS) in the mRNAs, ncRNAs and other nucleotide sequences -e.g. Promoter and Telomeric and gene flank regions. The putative G-quadruplexes are identified using the following motif:
						</p>
						<p style="margin:0; text-align:center">
						<img src="{$imagePath}motif.png"/>
						</p>
						<p style="margin-top:0;">
						Where x = # guanine tetrads in the G-quadruplex and y1, y2, y3 = length of gaps 
						</p>
						<p>
						QGRS-H Predictor web tool generates information on composition and distribution of putative homologous G-quadruplexes in semi-globally aligned nucleotide sequences based on published algorithms.  
						</p>
						<p>
						<b>Enter 2 sequences</b> in the input area to the left to run an analysis.  You may enter <a target="_blank" href="http://www.ncbi.nlm.nih.gov/RefSeq/">RefSeq mRNA Accession or GI#'s</a> and the application
						will automatically retrieve the sequence data from NCBI.  You may also enter either of the sequences in their raw or FASTA format if Accession or GI#'s are not applicable.  Alternatively, you may select one of the homologous pairs in the sample section below.
						
						<br/><br/><b style="color:#404C67">The program is limited to mRNA sequences less than 10,000 bases in length.  </b>
						
						</p>
						
						<p style="font-size:10pt;text-align:center">
							<a style="margin-right:3em" target="_help" href="help#quickstart">Quick-start tutorial</a><a style="margin-right:3em" target="_background" href="background">Project background</a>
						</p>
						<p>
						<b>Please cite</b>:
Menendez, C., Frees, S., and Bagga, P. (2012) QGRS-H Predictor: A Web Server
for Predicting Homologous Quadruplex forming G-Rich Sequence Motifs in
Nucleotide Sequences. <a href="http://nar.oxfordjournals.org/content/early/2012/05/10/nar.gks422.full?keytype=ref&amp;ijkey=90aQTvF26OeC8yJ" target="_blank">Nucleic Acids Res. doi: 10.1093/nar/gks422</a> <a style="margin-left:1em" href="http://nar.oxfordjournals.org/content/40/W1/W96.full">40: W96-W103</a>
</p>
					</div>
					</td>
				</tr>
				</table>
				</div>
				
				</div>
				<p style="margin-left:4em">or</p>
				<div class="ui-widget" style="margin:1em;">
				<div class="ui-widget-header  ui-corner-top" style="padding:0.25em">
				Select a Sample Pairing
				</div>
				<div class="ui-widget-content  ui-corner-bottom" style="font-size:smaller">
				<table class="pairTable" >
					<thead>
						<th></th>
						<th>mRNA Name</th>
						<th>Species (Principal)</th>
						<th>Species (Comparison)</th>
						<th>Description</th>
					</thead>
					<tbody>
						<xsl:for-each select="/qgrs/pairs/pair">
							<tr>
								<td width="125"><button class="runPairButton">Run Analysis</button></td>
								<td width="100" class="homologPair"><xsl:value-of select="symbol"/></td>
								<td width="150" class="principle" data-accession="{principle/@accession}"><xsl:value-of select="principle/@species"/></td>
								<td width="150" class="comparison" data-accession="{comparison/@accession}"><xsl:value-of select="comparison/@species"/></td>
								<td class="description">
									<xsl:value-of select="description"/>
									<xsl:if test="count(ref) &gt; 0">
										(<a target="_blank" href="{ref/@link}"><xsl:value-of select="ref"/></a>)
									</xsl:if>
								</td>
							</tr>
						</xsl:for-each>
						
					</tbody>
				</table>
				</div>
				</div>
				
				
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>