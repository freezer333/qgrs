package qgrs.db.query.qgrshomology;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.jdom.Element;

import qgrs.data.GQuadruplex;
import qgrs.db.query.SingleQgrsResult;

public class QgrsHomologyQueryResult {
	
	private SingleQgrsResult principal = new SingleQgrsResult();
	private SingleQgrsResult comparison = new SingleQgrsResult();
	private String score;
	static final DecimalFormat formatter = new DecimalFormat("0.00");
	
	public QgrsHomologyQueryResult(ResultSet rs) throws SQLException {
		
		principal.geneSymbol = rs.getString("P_GENESYMBOL");
		comparison.geneSymbol = rs.getString("C_GENESYMBOL");
		
		principal.geneSpecies = rs.getString("P_SPECIES");
		comparison.geneSpecies = rs.getString("C_SPECIES");
		
		principal.qgrsId = rs.getString("GQ1ID");
		comparison.qgrsId = rs.getString("GQ2ID");
		
		principal.qgrsSequence = rs.getString("qgrs1seq");
		comparison.qgrsSequence = rs.getString("qgrs2seq");
		
		principal.qgrsGScore = String.valueOf(rs.getInt("P_GSCORE"));
		comparison.qgrsGScore = String.valueOf(rs.getInt("C_GSCORE"));
		
		principal.qgrsTetrads = String.valueOf(rs.getInt("P_TETRADS"));
		comparison.qgrsTetrads = String.valueOf(rs.getInt("C_TETRADS"));
		
		// region needs to be set as the combo of the three regions
		principal.qgrsRegion = GQuadruplex.getRegionString(
				rs.getBoolean("p_in5UTR"), rs.getBoolean("p_inCDS"), rs.getBoolean("p_in3UTR"));
		
		comparison.qgrsRegion = GQuadruplex.getRegionString(
				rs.getBoolean("c_in5UTR"), rs.getBoolean("c_inCDS"), rs.getBoolean("c_in3UTR"));
			
		// position is the start (in result set) through the sequence length
		int start = rs.getInt("qgrs1Position");
		principal.qgrsPosition = (String.valueOf(start) + " - " + String.valueOf(start + principal.qgrsSequence.length()));
		
		start = rs.getInt("qgrs2Position");
		comparison.qgrsPosition = (String.valueOf(start) + " - " + String.valueOf(start + comparison.qgrsSequence.length()));
		
		principal.tetrad1 = rs.getInt("qgrs1Tetrad1");
		principal.tetrad2 = rs.getInt("qgrs1Tetrad2");
		principal.tetrad3 = rs.getInt("qgrs1Tetrad3");
		principal.tetrad4 = rs.getInt("qgrs1Tetrad4");
		
		comparison.tetrad1 = rs.getInt("qgrs2Tetrad1");
		comparison.tetrad2 = rs.getInt("qgrs2Tetrad2");
		comparison.tetrad3 = rs.getInt("qgrs2Tetrad3");
		comparison.tetrad4 = rs.getInt("qgrs2Tetrad4");
		
		score = formatter.format(rs.getDouble("OVERALLSCORE"));
	}
	
	public Element getXmlElement() {
		Element root = new Element("pair");
		root.addContent(principal.getXmlElement().setAttribute("side", "principal"));
		root.addContent(comparison.getXmlElement().setAttribute("side", "comparison"));
		root.addContent(new Element("score").setText(score));
		return root;
	}
	
}
