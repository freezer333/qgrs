package qgrs.data.providers;

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

import qgrs.compute.GeneSequencePair;

public abstract class AlignmentProvider {
	private final String RUN_URL = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/run";
	private final String POLL_URL = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/status/";
	private final String RESULTS_URL = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/result/";

	private final String[][] defaultParams = {	{ "matrix", "EDNAFULL" },
												{ "stype", "dna" },
												{ "email", "sfrees@ramapo.edu" },
												{ "gapopen", "50" },
												{ "gapext", "1" },
												{ "endopen", "10" },
												{ "endextend", "0.5" },
												{ "format", "markx3" } };
	
	List<NameValuePair> nameValuePairs;
	HttpClient httpclient;
	private String jobId;
	
	
	public void init() {
		httpclient = new DefaultHttpClient();
		nameValuePairs = new ArrayList<NameValuePair>(defaultParams.length+2);
		for (String[] p : this.defaultParams) {
			nameValuePairs.add(new BasicNameValuePair(p[0], p[1]));
		}
	}
	
	
	protected boolean allowLiveDownload() {
		return true;
	}
	
	protected AlignmentProviderResult getAlignmentFromNetwork(String pSequence, String cSequence) {
		init();
		try {
			startJob(pSequence, cSequence);
			if ( waitForCompletion())  {
				String result = getAlignmentResult();
				return buildResult(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.httpclient != null )  this.httpclient.getConnectionManager().shutdown();
		return null;
	}
	
	
	/**
	 * Parses the resulting FASTA formatted response and apply the 
	 * gapped strings to the GeneSequences.
	 * @param pair
	 * @param result
	 */
	private AlignmentProviderResult buildResult(String result) {
		String fasta1 = result.split(">")[1];
		String fasta2 = result.split(">")[2];
		fasta2 = fasta2.split("#")[0];
		fasta1 = fasta1.trim();
		fasta2 = fasta2.trim();
		
		String seq1 = fasta1.substring(fasta1.indexOf('\n')).trim();
		String seq2 = fasta2.substring(fasta2.indexOf('\n')).trim();
		
		return new AlignmentProviderResult(seq1, seq2, true);
	}
	
	/**
	 * Starts and emboss needleman alignment for the given pair
	 * and sets the jobID for this instance based on the response
	 * @param pair
	 * @throws Exception
	 */
	private void startJob(String pSequence, String cSequence) throws Exception {
		HttpPost post = this.buildRunPost(pSequence, cSequence);
		HttpResponse response = httpclient.execute(post);
	    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	    this.jobId = rd.readLine();
	}
	
	/**
	 * Constructs and HttpPost request for the given pair suitable for
	 * kicking off an emboss needleman alignment
	 * @param pair
	 * @return HttpPost for request
	 * @throws UnsupportedEncodingException
	 */
	private HttpPost buildRunPost(String pSequence, String cSequence) throws UnsupportedEncodingException {
		HttpPost post = new HttpPost(RUN_URL);
		nameValuePairs.add(new BasicNameValuePair("asequence", pSequence));
		nameValuePairs.add(new BasicNameValuePair("bsequence", cSequence));
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
		String pollUrl = POLL_URL + jobId;
		HttpGet get = new HttpGet(pollUrl);
		String status = "";
		int times =0;
		
		do {
			Thread.sleep(times * 250);
			times++;
			HttpResponse response = httpclient.execute(get);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			status = rd.readLine();
		} while ( times < 100 && status.equalsIgnoreCase("RUNNING")) ;
		
		return "FINISHED".equalsIgnoreCase(status);
	}
	
	
	/**
	 * Returns the alignment result content (FASTA) based on the job id.
	 * @return FASTA formatted response from emboss
	 * @throws Exception
	 */
	private String getAlignmentResult() throws Exception {
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
	
	
	
	protected abstract AlignmentProviderResult getAlignmentFromCache(GeneSequencePair pair) ;
	
	
	public AlignmentProviderResult getAlignment(GeneSequencePair pair) {
		/*System.out.println("Sequence alignment data requested");*/
		AlignmentProviderResult r = this.getAlignmentFromCache(pair);
		if( r != null ) {
			/*System.out.println("\t [Cached Version]");*/
		}
		else {
			if( this.allowLiveDownload() ) {
				r  = this.getAlignmentFromNetwork(pair.getPrinciple().getPureSequence(), pair.getComparison().getPureSequence());
			}
			else {
				System.out.println("\t Live download not available with this provider");
			}
		}
		
		return r;
	}
}
