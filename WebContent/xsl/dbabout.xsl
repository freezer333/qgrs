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
					<xsl:with-param name="title">:  About the QGRS-H Database</xsl:with-param>
					<xsl:with-param name="gotostart">false</xsl:with-param>
					<xsl:with-param name="appname"></xsl:with-param>
				</xsl:call-template>
				<div class="messageInsert rounded-corners">
					<h2>Credits</h2>
					<p><b>Co-Principle Investigators/Research Mentors:</b><br/>
					<a href="mailto:pbagga@ramapo.edu">Dr. Paramjeet Bagga</a>, Bioinformatics, Ramapo College of New Jersey <br/>
					<a href="mailto:sfrees@ramapo.edu">Dr. Scott Frees </a>, Computer Science, Ramapo College of New Jersey 
					</p>
					<p>
					<b>Undergraduate Students:</b><br/>
					Camille Menendez<br/>
					Matthew Crum<br/>
					</p>
				</div>
				<div class="messageInsert rounded-corners">
					<h2>Acknowledgements</h2>
					<p>
					We thank RCNJ Information Technology Services and Mike Skafidas for technical assistance with the web servers. This project was funded in part by a grant from the Provost Office of Ramapo College of New Jersey. 
					
					</p>
				</div>
				<div class="messageInsert rounded-corners">
					<h2>Purpose</h2>
					The goal of <b>QGRS-H DB</b> is to provide freely accessible large-scale curated data on evolutionarily conserved putative <b>Q</b>uadruplex forming '<b>G</b>'-<b>R</b>ich <b>S</b>equences (QGRS) mapped in eukaryotic protein coding transcriptomes.  
					<h2>About</h2>
					<p>QGRS-H DB contains comprehensive curated information on the composition and distribution of putative homologous G-quadruplexes in semi-globally aligned homologous mRNA sequences. Data stored in the QGRS-H DB are based on computational analysis of a large-scale RefSeq/GenBank nucleotide sequences dataset comprising of homologs identified with the help of NCBI Homologene database. Computations were performed with an indigenously developed and previously published software program QGRS-H Predictor. 
					</p>
					<p>
					Due to growing evidence for their role in important biological processes and as therapeutic targets, there has been a tremendous amount of interest in studying quadruplex forming G-rich sequences in the mammalian genomes and transcriptomes. This is evident from a surge in the relevant published literature in recent years. The QGRS-H DB is a unique web accessible database containing detailed information about mapped homologous G-quadruplex forming sequences in the context of 5'- and 3'-UTRs (untranslated regions) and CDS sections of aligned mRNA sequences. Identifying phylogenetically conserved motifs adds another dimension of validating the computational G-quadruplex predictions. QGRS-H DB reports QGRS Homology Scores which evaluate the predicted homologous G-quadruplex pairs for their evolutionary conservation. The user-interface provides many options for searching the database and displays a variety of data on composition and locations of QGRS relative to CDS in the individual as well as aligned homologous mRNA entries in highly interactive tables. QGRS-H DB web interface also allows direct interactions with our previously published QGRS-H Predictor software for visual comparison of homologous QGRS distribution patterns in the aligned mRNA sequences in a highly interactive 'Sequence View'. In this option the data table of Homologous QGRS Pairs as well as the Sequence View are intimately connected with the dynamically generated and highly interactive QGRS Homology Map. 
					</p>
					<p>QGRS-H DB can be helpful in performing large-scale transcriptome wide analysis of overall occurrence and significance of phylogenetically conserved putative G-quadruplexes as cis-regulatory elements in the untranslated and translated mRNA regions.
					</p>
				</div>
				
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>