package qgrs.compute.stat.qgrs.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import qgrs.compute.stat.GenePartition;
import qgrs.compute.stat.GenePartitioner;
import qgrs.compute.stat.Analysis;


public class OntologyGenePartitioner extends GenePartitioner {

	String[] Transcription_Factor_Genes = {"transcription factor complex", "transcription factor binding"};
	String[] Apoptosis_Genes = {"apoptotic process", "apoptotic signaling pathway", "induction of apoptosis", "execution phase of apoptosis", "negative regulation of apoptotic process", "positive regulation of apoptotic process"};
	String[] Epigenetics_Genes = {"DNA-methyltransferase", "methyl-CpG binding", "methyl-CpNpN binding", "DNA hypermethylation", "DNA hypomethylation"};
	String[] Brain_Development_Genes = {"brain segmentation", "brain morphogenesis", "central complex development", "forebrain development","hindbrain development","midbrain development"};
	String[] Positive_Regulation_Cell_Proliferation_Genes = {"positive regulation of cell proliferation"};
	String[] Negative_Regulation_Cell_Proliferation_Genes = {"negative regulation of cell proliferation"};
	String[] Regulation_of_Cell_Cycle_Genes = {"regulation of cell cycle"};
	
	public OntologyGenePartitioner(Analysis runner) {
		super(runner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<GenePartition> partition(Connection c) {
		HashSet<GenePartition> partitions = new HashSet<GenePartition>();

		partitions.add(getPartition(c, Transcription_Factor_Genes, "Transcription Factor Genes"));
		partitions.add(getOncogenePartition(c));
		partitions.add(getPartition(c, Apoptosis_Genes, "Apoptosis Genes"));
		partitions.add(getPartition(c, Epigenetics_Genes, "Epigentics Genes"));
		partitions.add(getPartition(c, Brain_Development_Genes, "Brain Development Genes"));
		partitions.add(getPartition(c, Positive_Regulation_Cell_Proliferation_Genes, "Positive regulation of cell proliferation Genes"));
		partitions.add(getPartition(c, Negative_Regulation_Cell_Proliferation_Genes, "Negative regulation of cell proliferation Genes"));
		partitions.add(getPartition(c, Regulation_of_Cell_Cycle_Genes, "Regulation of Cell Cycle Genes"));
		
		return partitions;
	}
	
	private GenePartition getOncogenePartition(Connection c){
		String q="SELECT * FROM GENE WHERE GENENAME LIKE '%oncogene%' AND SPECIES ='Homo sapiens'";
		
		GenePartition p = new GenePartition(this.runner, "Oncogenes");
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			while ( rs.next() ) {
					p.ids.add(rs.getString("accessionNumber"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}

	private GenePartition getPartition(Connection c, String[] list, String name) {
		String q = "SELECT GO.accessionNumber,GENE.species FROM GO JOIN GENE ON GENE.ACCESSIONNUMBER = GO.ACCESSIONNUMBER WHERE";
		for(int j=0;j<list.length;j++){
			if (j > 0){
				q += " OR ";
			}
			q +=" GOTERM = '" + list[j] + "'";
		}

		GenePartition p = new GenePartition(this.runner, name);
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			while ( rs.next() ) {
				if (rs.getString("species").equalsIgnoreCase("Homo sapiens")){
					p.ids.add(rs.getString("accessionNumber"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}




}
