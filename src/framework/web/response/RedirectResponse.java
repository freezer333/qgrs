
package framework.web.response;

import framework.web.AbstractWebContext;

public class RedirectResponse implements Response {

	final String target;
	
	public RedirectResponse(String target) {
		this.target = target;
	}
	@Override
	public void respond(AbstractWebContext context) throws Exception {
		context.getResponse().sendRedirect(this.target);
	}


}
