package qgrs.data.query.qgrs;

import java.sql.ResultSet;

import org.jdom.Element;

import qgrs.data.GQuadruplex;
import qgrs.data.query.SingleQgrsResult;

public class QgrsQueryResult {

	SingleQgrsResult data = new SingleQgrsResult();
	
	
	
	public QgrsQueryResult (ResultSet rs) {
		try {
			data.geneSymbol = rs.getString("GENE.GENESYMBOL");
			data.geneSpecies = rs.getString("GENE.SPECIES");
			data.qgrsId = rs.getString("QGRS.ID");
			data.qgrsSequence = rs.getString("QGRS.SEQUENCESLICE");
			data.qgrsGScore = String.valueOf(rs.getInt("QGRS.GSCORE"));
			data.qgrsTetrads = String.valueOf(rs.getInt("QGRS.NUMTETRADS"));
			
			// region needs to be set as the combo of the three regions
			data.qgrsRegion = GQuadruplex.getRegionString(
					rs.getBoolean("QGRS.IN5PRIME"), rs.getBoolean("QGRS.INCDS"), rs.getBoolean("QGRS.IN3PRIME"));
			
			// position is the start (in result set) through the sequence length
			int start = rs.getInt("QGRS.TETRAD1");
			data.qgrsPosition = (String.valueOf(start) + " - " + String.valueOf(start + data.qgrsSequence.length()));
			data.tetrad1 = rs.getInt("QGRS.TETRAD1");
			data.tetrad2 = rs.getInt("QGRS.TETRAD2");
			data.tetrad3 = rs.getInt("QGRS.TETRAD3");
			data.tetrad4 = rs.getInt("QGRS.TETRAD4");
			
			data.hCount = rs.getInt("HCOUNT");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Element getXmlElement() {
		return data.getXmlElement();
	}
}
