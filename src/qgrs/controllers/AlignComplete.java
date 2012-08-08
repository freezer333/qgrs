package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.input.QParam;
import qgrs.input.ResultViewParams;
import qgrs.job.AlignmentJob;
import qgrs.output.XmlResultProcessor;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.ErrorResponse;
import framework.web.response.PageResponse;
import framework.web.response.Response;
import framework.web.util.StringUtils;

public class AlignComplete extends AbstractController {

	public AlignComplete() {
		super();
		this.supportedUrls.add("/app/align-complete");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		String id = context.getString(QParam.JobId);
		if ( StringUtils.isDefined(id)) {
			AlignmentJob aj = (AlignmentJob) context.getRequest().getSession().getAttribute(id);
			if ( aj != null ) {
				Document pageXml = new Document();
			    Element root = new Element("qgrs");
			    root.setAttribute("jobId", aj.getId().toString());
			 
			    // Add the view parameters
				pageXml.addContent(root);
			    XmlResultProcessor resultProcessor = (XmlResultProcessor) aj.getResultProcessor();
			    ResultViewParams rvp = new ResultViewParams(context);
			    resultProcessor.setViewParams(rvp);
			    
			    Element resultElement = resultProcessor.getXmlElement();
			    rvp.setTotalRecords(resultProcessor.getTotalRecords());
			    
			    root.addContent(resultElement);
			    root.addContent(rvp.getXml(String.valueOf(resultProcessor.getPrincipleLastIndex())));
			    
			    return new PageResponse(new ModelView(XslViews.AlignmentComplete, pageXml));
			}
		}
		return new ErrorResponse(XslViews.Error, context.getResourceResolver(), "Unknown Job");
	}

}
