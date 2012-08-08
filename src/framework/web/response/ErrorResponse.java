
package framework.web.response;

import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.ResourceResolver;
import framework.web.util.XslTransformer;


public class ErrorResponse implements Response {

	final String friendlyMessage;
	final Throwable exeption;
	final ResourceResolver rr;
	final String xslFilename;


	public ErrorResponse(String xslFilename, ResourceResolver rr) {
		this.friendlyMessage = "Cause of error is unknown, please notify the site administrator.";
		this.exeption = null;
		this.rr = rr;
		this.xslFilename = xslFilename;
	}
	public ErrorResponse(String xslFilename, ResourceResolver rr, String friendlyMessage) {
		this.friendlyMessage = friendlyMessage;
		this.exeption = null;
		this.rr = rr;
		this.xslFilename = xslFilename;
	}
	public ErrorResponse(String xslFilename, ResourceResolver rr, String friendlyMessage, Throwable e) {
		this.friendlyMessage = friendlyMessage;
		this.exeption = e;
		this.rr = rr;
		this.xslFilename = xslFilename;
	}
	@Override
	public void respond(AbstractWebContext context) throws Exception {
		try {
			context.getResponse().setContentType("text/html");
			XslTransformer.transform(context, new ModelView(this.xslFilename, context.buildErrorPageXml(this.friendlyMessage, this.exeption)));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}


