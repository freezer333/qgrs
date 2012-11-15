package qgrs.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.biojavax.bio.db.ncbi.GenbankRichSequenceDB;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.io.FastaFormat;

import qgrs.data.GeneSequence;
import qgrs.db.GenbankRichSequenceTextDB;
import qgrs.input.AccessionNumberInputProvider;
import qgrs.input.InputProvider;
import qgrs.input.QGRSProgramInput;


/*
 * http://www.ebi.ac.uk/Tools/webservices/services/psa/emboss_needle_rest#resulttypes
 */
public class EmbossTest {
	private GeneSequence principle;
	private GeneSequence comparison;
	
	private final String RUN_URL = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/run";
	private final String POLL_URL = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/status/";
	private final String RESULTS_URL = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/result/";
	
	private final String [] matrix = {"matrix", "EDNAFULL"};
	private final String [] stype = {"stype", "dna"};
	private final String [] email = {"email", "sfrees@ramapo.edu"};
	private final String [] gapopen = {"gapopen", "10"};
	private final String [] gapext = {"gapext", "0.5"};
	private final String [] endopen = {"endopen", "10"};
	private final String [] endextend = {"endextend", "0.5"};
	private final String [] format = {"format", "markx3"};
	private final List<String []> params;
	
	public EmbossTest (GeneSequence seq1, GeneSequence seq2) {
		
		this.principle = seq1;
		this.comparison = seq2;
		
		params = new LinkedList<String[]>();
		params.add(matrix);
		params.add(stype);
		params.add(gapopen);
		params.add(gapext);
		params.add(format);
		params.add(email);
		params.add(endopen);
		params.add(endextend);
		params.add(new String [] {"asequence", this.principle.getPureSequence()});
		params.add(new String [] {"bsequence", this.comparison.getPureSequence()});
		
	}
	
	public boolean getAlignment() throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost post = this.buildPost();
		
	 
	    HttpResponse response = httpclient.execute(post);
	    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	     
	    String jobId = rd.readLine();
	   // System.out.println("jobId = " + jobId);
	      
		String pollUrl = POLL_URL + jobId;
		HttpGet get = new HttpGet(pollUrl);
		String status = "";
		do {
			response = httpclient.execute(get);
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			status = rd.readLine();
			/*System.out.println("Status:  " + status);*/
		} while ( status.equalsIgnoreCase("RUNNING")) ;
		
		if ( "FINISHED".equalsIgnoreCase(status) ) {
			get = new HttpGet(this.RESULTS_URL + jobId + "/aln");
			response = httpclient.execute(get);
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String result = "";
			String line = "";
		    while ((line = rd.readLine()) != null) {
		   // 	System.out.println(line);
		    	result += (line + "\n");
		    }
		    
			String fasta1 = result.split(">")[1];
			String fasta2 = result.split(">")[2];
			fasta2 = fasta2.split("#")[0];
			fasta1 = fasta1.trim();
			fasta2 = fasta2.trim();
			
			String seq1 = fasta1.substring(fasta1.indexOf('\n')).trim();
			String seq2 = fasta2.substring(fasta2.indexOf('\n')).trim();
			
			this.principle.setGaps(seq1);
			this.comparison.setGaps(seq2);
			return true;
			
		}
		else {
			return false;
		}
		
	}
	
	
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String p = "NM_006245.2";
		String c = "NM_009358.3";
		
		InputProvider provider = new AccessionNumberInputProvider(p, c);
		QGRSProgramInput input = provider.getInput();
		EmbossTest emboss = new EmbossTest(input.getPrinciple(), input.getComparisons().get(0));
		emboss.getAlignment();
		System.out.println(input.getPrinciple().getAlignedSequence());
		System.out.println(input.getComparisons().get(0).getAlignedSequence());
		
	}

}
