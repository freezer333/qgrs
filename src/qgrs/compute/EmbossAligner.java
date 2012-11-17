package qgrs.compute;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import qgrs.job.CancelFlag;
import qgrs.job.DefaultCancelFlag;
import qgrs.job.JobStage;
import qgrs.job.StatusHolder;


/**
 * Utilizes the REST interface for emboss needleman semi-global alignment.
 * Uses the Apache HttpClient to issue requests
 * @author sfrees
 *
 */
public class EmbossAligner implements GeneralAligner {

	StatusHolder statusHolder = null;
	CancelFlag cancelFlag = new DefaultCancelFlag();
	
	private final String RUN_URL = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/run";
	private final String POLL_URL = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/status/";
	private final String RESULTS_URL = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/result/";

	private final String[][] defaultParams = {	{ "matrix", "EDNAFULL" },
												{ "stype", "dna" },
												{ "email", "sfrees@ramapo.edu" },
												{ "gapopen", "10" },
												{ "gapext", "0.5" },
												{ "endopen", "10" },
												{ "endextend", "0.5" },
												{ "format", "markx3" } };
	
	List<NameValuePair> nameValuePairs;
	HttpClient httpclient;
	private String jobId;

	public EmbossAligner() {
		httpclient = new DefaultHttpClient();
		nameValuePairs = new ArrayList<NameValuePair>(defaultParams.length+2);
		for (String[] p : this.defaultParams) {
			nameValuePairs.add(new BasicNameValuePair(p[0], p[1]));
		}
	}

	
	
	@Override
	public void align(GeneSequencePair pair, StatusHolder paramStatusHolder)
			throws Exception {

		startJob(pair);
	    if ( waitForCompletion())  {
	    	String result = getAlignmentResult();
	    	applyResult(pair, result);
		}
		else {
			if (paramStatusHolder != null)
				paramStatusHolder.setStatus(JobStage.Alignment_Calc, 1, "Error occurred while performing alignment");
		}
	}
	
	/**
	 * Starts and emboss needleman alignment for the given pair
	 * and sets the jobID for this instance based on the response
	 * @param pair
	 * @throws Exception
	 */
	private void startJob(GeneSequencePair pair) throws Exception {
		if (statusHolder != null)
			statusHolder.setStatus(JobStage.Alignment_Calc, 0,
					"sending to EMBOSS");
		
		HttpPost post = this.buildRunPost(pair);
		HttpResponse response = httpclient.execute(post);
	    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	    this.jobId = rd.readLine();
	    System.out.println("Emboss alignment " + jobId);
	}

	/**
	 * Constructs and HttpPost request for the given pair suitable for
	 * kicking off an emboss needleman alignment
	 * @param pair
	 * @return HttpPost for request
	 * @throws UnsupportedEncodingException
	 */
	private HttpPost buildRunPost(GeneSequencePair pair) throws UnsupportedEncodingException {
		HttpPost post = new HttpPost(RUN_URL);
		nameValuePairs.add(new BasicNameValuePair("asequence", pair.getPrinciple().getPureSequence()));
		nameValuePairs.add(new BasicNameValuePair("bsequence", pair.getComparison().getPureSequence()));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return post;
	}
	
	/**
	 * Polls emboss using the job ID to wait until the job is not running
	 * which could mean error or finished.  Returns true if finished (success)
	 * @return true if finished successfully
	 * @throws Exception
	 */
	private boolean waitForCompletion() throws Exception {
		if (statusHolder != null)
			statusHolder.setStatus(JobStage.Alignment_Calc, 0.5,
					"Waiting for alignment job to complete");
		String pollUrl = POLL_URL + jobId;
		HttpGet get = new HttpGet(pollUrl);
		String status = "";
		do {
			HttpResponse response = httpclient.execute(get);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			status = rd.readLine();
		} while ( status.equalsIgnoreCase("RUNNING")) ;
		
		return "FINISHED".equalsIgnoreCase(status);
	}
	
	/**
	 * Returns the alignment result content (FASTA) based on the job id.
	 * @return FASTA formatted response from emboss
	 * @throws Exception
	 */
	private String getAlignmentResult() throws Exception {
		if (statusHolder != null)
			statusHolder.setStatus(JobStage.Alignment_Calc, 0.75,
					"Processing alignment results");
		HttpGet get = new HttpGet(this.RESULTS_URL + jobId + "/aln");
		HttpResponse response = httpclient.execute(get);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String result = "";
		String line = "";
	    while ((line = rd.readLine()) != null) {
	    	result += (line + "\n");
	    }
	    return result;
	}
	
	/**
	 * Parses the resulting FASTA formatted response and apply the 
	 * gapped strings to the GeneSequences.
	 * @param pair
	 * @param result
	 */
	private void applyResult(GeneSequencePair pair, String result) {
		String fasta1 = result.split(">")[1];
		String fasta2 = result.split(">")[2];
		fasta2 = fasta2.split("#")[0];
		fasta1 = fasta1.trim();
		fasta2 = fasta2.trim();
		
		String seq1 = fasta1.substring(fasta1.indexOf('\n')).trim();
		String seq2 = fasta2.substring(fasta2.indexOf('\n')).trim();
		
		pair.getPrinciple().setGaps(seq1);
		pair.getComparison().setGaps(seq2);
		if (statusHolder != null)
			statusHolder.setStatus(JobStage.Alignment_Calc, 1,
					"Alignment completed");
	}

	@Override
	public void cleanup() {
		if (this.httpclient != null )  this.httpclient.getConnectionManager().shutdown();
	}
	@Override
	public void setCancelFlag(CancelFlag flag) {
		// TODO Auto-generated method stub

	}
}
