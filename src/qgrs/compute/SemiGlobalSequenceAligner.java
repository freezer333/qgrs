package qgrs.compute;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import qgrs.data.providers.AlignmentProvider;
import qgrs.data.providers.AlignmentProviderResult;
import qgrs.job.CancelFlag;
import qgrs.job.DefaultCancelFlag;
import qgrs.job.JobStage;
import qgrs.job.StatusHolder;



public class SemiGlobalSequenceAligner implements GeneralAligner {

	StatusHolder statusHolder = null;
	CancelFlag cancelFlag = new DefaultCancelFlag();
	AlignmentProvider provider;
	

	public SemiGlobalSequenceAligner(AlignmentProvider provider)  {
		this.provider = provider;
	}

	
	
	@Override
	public void align(GeneSequencePair pair, StatusHolder paramStatusHolder)
			throws Exception {
		
		AlignmentProviderResult result = provider.getAlignment(pair);
		if ( result == null ) {
			
			if (paramStatusHolder != null)
				paramStatusHolder.setStatus(JobStage.Alignment_Calc, 1, "Error occurred while performing alignment");
		}
		else {
			pair.setWasCached(!result.live);
			pair.getPrinciple().setGaps(result.principal);
			pair.getComparison().setGaps(result.comparison);
			if (statusHolder != null)
				statusHolder.setStatus(JobStage.Alignment_Calc, 1,
						"Alignment completed");
		}
		
	}
	
	

	@Override
	public void cleanup() {
		
	}
	@Override
	public void setCancelFlag(CancelFlag flag) {
		// TODO Auto-generated method stub

	}
}