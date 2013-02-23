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
	String[] Oncogenes = {"oncogene-induced senescence"};
	String[] Apoptosis_Genes = {"apoptotic process", "apoptotic signaling pathway", "induction of apoptosis", "execution phase of apoptosis", "negative regulation of apoptotic process", "positive regulation of apoptotic process"};
	String[] Epigenetics_Genes = {"DNA-methyltransferase", "methyl-CpG", "methyl-CpNpN", "DNA hypermethylation", "DNA hypomethylation"};
	String[] Brain_Development_Genes = {"brain segmentation", "brain morphogenesis", "central complex development", "forebrain development","hindbrain development","midbrain development"};
	
	public OntologyGenePartitioner(Analysis runner) {
		super(runner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<GenePartition> partition(Connection c) {
		HashSet<GenePartition> partitions = new HashSet<GenePartition>();

		partitions.add(getPartition(c, Transcription_Factor_Genes, "Transcription Factor Genes"));
		partitions.add(getPartition(c, Oncogenes, "Oncogenes"));
		partitions.add(getPartition(c, Apoptosis_Genes, "Apoptosis Genes"));
		partitions.add(getPartition(c, Epigenetics_Genes, "Epigentics Genes"));
		partitions.add(getPartition(c, Brain_Development_Genes, "Brain Development Genes"));
		
		return partitions;
	}

	private GenePartition getPartition(Connection c, String[] list, String name) {
		String q = "SELECT accessionNumber FROM GO WHERE";
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
				p.ids.add(rs.getString("accessionNumber"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}




}
