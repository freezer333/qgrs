package qgrs.data;


import org.biojavax.bio.BioEntry;
import org.biojavax.bio.db.ncbi.GenbankRichSequenceDB;

import qgrs.input.GenbankRichSequenceTextDB;

public class FeatureTest {


	public static void main(String[] args) {


		String id = "NM_000345.3";

		try {			
			GenbankRichSequenceDB  ncbi = null;

			ncbi = new GenbankRichSequenceTextDB();

			
			//	GeneSequence s = null;
			BioEntry rs = ncbi.getBioEntry(id);
			System.out.println(	rs.getAccession()); 
			
			
			/*		SequenceFeatureAdapter a = new SequenceFeatureAdapter(rs);
			a.printFeatures(rs, " "); */
			//		String sequence =rs.seqString();
			//		s = GeneSequence.buildFromRichSequence(sequence, rs);
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}

}
