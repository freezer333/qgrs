package qgrs.compute;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

import qgrs.data.GeneSequence;
import qgrs.job.CancelFlag;
import qgrs.job.DefaultCancelFlag;
import qgrs.job.JobStage;
import qgrs.job.StatusHolder;

public class EmbossAligner implements GeneralAligner {

	StatusHolder statusHolder = null;
	CancelFlag cancelFlag = new DefaultCancelFlag();
	private final String RUN_URL = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/run";
	private final String POLL_URL = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/status/";
	private final String RESULTS_URL = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/result/";

	private final String[] matrix = { "matrix", "EDNAFULL" };
	private final String[] stype = { "stype", "dna" };
	private final String[] email = { "email", "sfrees@ramapo.edu" };
	private final String[] gapopen = { "gapopen", "10" };
	private final String[] gapext = { "gapext", "0.5" };
	private final String[] endopen = { "endopen", "10" };
	private final String[] endextend = { "endextend", "0.5" };
	private final String[] format = { "format", "markx3" };
	private final List<String[]> params;

	public EmbossAligner() {
		params = new LinkedList<String[]>();
		params.add(matrix);
		params.add(stype);
		params.add(gapopen);
		params.add(gapext);
		params.add(format);
		params.add(email);
		params.add(endopen);
		params.add(endextend);

	}

	@Override
	public void setCancelFlag(CancelFlag flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void align(GeneSequencePair pair, StatusHolder statusHolder)
			throws Exception {

		if (statusHolder != null)
			statusHolder.setStatus(JobStage.Alignment_Calc, 0,
					"sending to EMBOSS");
		params.add(new String [] {"asequence", pair.getPrinciple().getPureSequence()});
		params.add(new String [] {"bsequence", pair.getComparison().getPureSequence()});
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost post = this.buildPost();
		HttpResponse response = httpclient.execute(post);
	    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	     
	    String jobId = rd.readLine();
	    if (statusHolder != null)
			statusHolder.setStatus(JobStage.Alignment_Calc, 0.5,
					"Waiting for alignment job to complete");
		String pollUrl = POLL_URL + jobId;
		HttpGet get = new HttpGet(pollUrl);
		String status = "";
		do {
			response = httpclient.execute(get);
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			status = rd.readLine();
		} while ( status.equalsIgnoreCase("RUNNING")) ;
		
		if ( "FINISHED".equalsIgnoreCase(status) ) {
			if (statusHolder != null)
				statusHolder.setStatus(JobStage.Alignment_Calc, 0.75,
						"Processing alignment results");
			get = new HttpGet(this.RESULTS_URL + jobId + "/aln");
			response = httpclient.execute(get);
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String result = "";
			String line = "";
		    while ((line = rd.readLine()) != null) {
		    	result += (line + "\n");
		    }
		    
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
		else {
			if (statusHolder != null)
				statusHolder.setStatus(JobStage.Alignment_Calc, 1,
						"Error occurred while performing alignment");
		}
	}

	public HttpPost buildPost() throws UnsupportedEncodingException {

		HttpPost post = new HttpPost(RUN_URL);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				params.size());
		for (String[] p : this.params) {
			nameValuePairs.add(new BasicNameValuePair(p[0], p[1]));
		}
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return post;
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

}
