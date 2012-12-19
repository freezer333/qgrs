package qgrs.model;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.jdom.Element;

import qgrs.input.QParam;
import framework.web.util.StringUtils;


public class DbCriteria extends HashMap<QParam, String> {

	public int getPageLimit() {
		return 50;
	}
	
	public Boolean readBoolean(String onoff) {
		return "on".equalsIgnoreCase(onoff);
		
	}
	
	private long startTimeNanoSeconds;
	public DbCriteria (QgrsWebContext context) {
		for ( QParam param : QParam.values() ) {
			if ( param.toString().startsWith("Db_")) {
				String value = context.getString(param);
				//System.out.println("Debug Db Criteria In:  " + param.getName() + " -> " + value);
				this.put(param, value == null ? param.getDefaultValue() : value);
			}
		}
		startTimeNanoSeconds = System.nanoTime();
	}
	
	
	
	public Element getXmlElement() {
		Element root = new Element("dbCriteria");
		for ( QParam param : this.keySet() ) {
			//System.out.println("Debug Db Criteria Out:  " + param.getName() + " -> " + this.get(param));
			root.addContent(new Element(param.getName()).setText(this.get(param)));
		}
		root.addContent(this.getSummaryElement());
		root.addContent(new Element("queryTime").setText(new DecimalFormat("0.000").format(((double)(System.nanoTime()-this.startTimeNanoSeconds))/1000000000) + " seconds"));
		return root;
	}
	
	
	private Element getSummaryElement() {
		Element root = new Element("dbSummary");
		Element qgrs1 = new Element("qgrs1");
		String retval = "";
		retval += "Principle QGRS:  ";
		if ( StringUtils.isDefined(this.get(QParam.Db_QgrsId1))) {
			retval += ("{id=" + this.get(QParam.Db_QgrsId1) + "}");
		}
		retval += (" Tetrads >= " + this.get(QParam.Db_MinTetrads1) + " | GScore >= " + this.get(QParam.Db_GScore1));
		retval += " | region =";
		int count = 0;
		if ( "on".equalsIgnoreCase(this.get(QParam.Db_Region13UTR)) ) {
			retval += " 3'UTR"; count++;
		}
		if ( "on".equalsIgnoreCase(this.get(QParam.Db_Region1CDS))) {
			if ( count > 0 ) retval += " or "; retval += " CDS"; count++;
		}
		if ( "on".equalsIgnoreCase(this.get(QParam.Db_Region15UTR))) {
			if ( count > 0 ) retval += " or "; retval += " 5'UTR"; count++;
		}
		if ( count == 0 ) {
			retval += " none selected";
		}
		if ( StringUtils.isDefined(this.get(QParam.Db_GeneId1))) {
			retval += (" [Gene Id:  " + this.get(QParam.Db_GeneId1) +"]");
		}
		if ( StringUtils.isDefined(this.get(QParam.Db_Species1))) {
			retval += (" [Species:  " + this.get(QParam.Db_Species1) +"]");
		}
		if ( StringUtils.isDefined(this.get(QParam.Db_Ontology1))) {
			retval += (" [Ontology Terms:  " + this.get(QParam.Db_Ontology1) +"]");
		}
		qgrs1.setText(retval);
		root.addContent(qgrs1);
		
		
		Element qgrsHomology = new Element("qgrsHomology");
	    retval = "";
		retval += "QGRS Homology Scores:  ";
		retval += ("Overlap >= " + this.get(QParam.Db_OverlapSimilarity) + " | Tetrad >= " 
				+ this.get(QParam.Db_TetradSimilarity) + " | Loop >= " + this.get(QParam.Db_LoopSimilarity) 
				+ " | Length >= " + this.get(QParam.Db_TotalLengthSimilarity) + " | Overall >= " + this.get(QParam.Db_OverallSimilarity) );
		retval += ("    [mRNA Alignment >= " + this.get(QParam.Db_MinAlignmentScore) + "%]");
		qgrsHomology.setText(retval);
		root.addContent(qgrsHomology);
		
		return root;
	}
}
