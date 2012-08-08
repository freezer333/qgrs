package framework.web.response;

import framework.web.AbstractWebContext;

public class PdfResponse implements Response {
	byte [] pdf;
	
	public PdfResponse(byte [] pdf) {
		this.pdf = pdf;
	}
	@Override
	public void respond(AbstractWebContext context) throws Exception {
		context.getResponse().setHeader("Content-Disposition", "pdf");
		context.getResponse().setContentType("application/pdf");
		context.getResponse().getOutputStream().write(pdf);
		context.getResponse().flushBuffer();

	}

}
