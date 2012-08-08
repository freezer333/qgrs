
package framework.web.response;

import framework.web.AbstractWebContext;


public class HtmlResponse implements Response {
	String htmlContent;
	
	public HtmlResponse(String htmlContent) {
		this.htmlContent = htmlContent;
	}
	
	@Override
	public void respond(AbstractWebContext context) throws Exception {
		context.getResponse().setContentType("application/html");
		context.getResponse().getWriter().print(htmlContent);
	}

}
