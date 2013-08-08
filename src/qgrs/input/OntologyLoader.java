package qgrs.input;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import qgrs.data.OntologyData;

public class OntologyLoader {

	// URL's for Entrez/NCBI
	private final String geneIdLookupBase = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=gene&term=";
	private final String detailsUrlBase = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&retmode=xml&id=";
	
	// Xpaths for pulling data out of XML resonses
	private final String geneIdXpath = "/eSearchResult/IdList/Id[1]";
	private final String functionXpath = "//Gene-commentary_label[.='Function']";
	private final String processXpath = "//Gene-commentary_label[.='Process']";
	private final String componentXpath = "//Gene-commentary_label[.='Component']";
	private final String nameXpath = "../Gene-commentary_comment/Gene-commentary/Gene-commentary_source/Other-source/Other-source_anchor";
	
	// using apache http client to issue GET requests to NCBI
	private HttpClient httpclient;
	
	
	
	public static void main(String[] args) throws Exception {
		String [] tests = {"NM_003403.3", "NM_001101672.1"};
		OntologyLoader loader = null;
		try {
			loader = new OntologyLoader();
			
			for ( String accession : tests ) {
				System.out.println("***********************************");
				System.out.println("Accession Number:  " + accession);
			
				
				OntologyData oData = loader.getOntologyData(accession);
				System.out.println("=============================\nFunctions\n=============================");
				for ( String s : oData.functions ) {
					System.out.println(s);
				}
				System.out.println("=============================\nComponents\n=============================");
				for ( String s : oData.components ) {
					System.out.println(s);
				}
				System.out.println("=============================\nProcesses\n=============================");
				for ( String s : oData.processes ) {
					System.out.println(s);
				}
			}
		} 
		finally {
			if ( loader != null ) loader.shutdown();
		}

	}
	
	public OntologyLoader () {
		try {
			httpclient = new DefaultHttpClient();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public OntologyData getOntologyData(String accessionNumber) {
		OntologyData oData = new OntologyData();
		try {
			String geneId = getGeneId(accessionNumber);
			if ( geneId == null ) {
				System.out.println("Accession number -> " + accessionNumber + " could not be located via entrez");
				return null;
			}
			fillOntologyData(oData, geneId);
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		
		return oData;
	}
	
	private void fillOntologyData(OntologyData oData, String geneId) throws Exception {
		String detailsUrl = this.detailsUrlBase + geneId;
		String resultString = getData(detailsUrl);
		
		Reader in = new StringReader(resultString);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		
		Element element = (Element) XPath.selectSingleNode(doc, functionXpath);
		fillList(oData.functions, element);
		
		element = (Element) XPath.selectSingleNode(doc, processXpath);
		fillList(oData.processes, element);
		
		element = (Element) XPath.selectSingleNode(doc, componentXpath);
		fillList(oData.components, element);
	}
	
	private void fillList(Collection<String> list, Element element) throws JDOMException {
		List nodeList = XPath.selectNodes(element, nameXpath);													  
		for ( Object on : nodeList ){
			Element e = (Element) on;
			list.add(e.getText());
		}
	}
	
	private String getGeneId(String accessionNumber) throws Exception{
		String geneIdLookupUrl = geneIdLookupBase + accessionNumber;
		String searchResult = getData(geneIdLookupUrl);
		
		Reader in = new StringReader(searchResult);

		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		
		Element idElement = (Element) XPath.selectSingleNode(doc, geneIdXpath);
		
		if ( idElement == null ) {
			if ( accessionNumber.contains(".")) {
				return getGeneId(accessionNumber.subSequence(0, accessionNumber.indexOf('.')).toString());
			}
			else {
				return null;
			}
		}
		return idElement.getText();
	}
	
	private String getData(String url) throws Exception{
        HttpGet httpget = new HttpGet(url);
        //System.out.println("executing request " + httpget.getURI());
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        return responseBody;  
	}
	
	public void shutdown(){
		try {
			if ( httpclient != null ) httpclient.getConnectionManager().shutdown();
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	
	
	
	
}


