package framework.web.response;


import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.util.XslTransformer;

public class PageResponse implements Response {

	final private ModelView modelView;
	
	public PageResponse (ModelView mv) {
		this.modelView = mv;
	}
	
	@Override
	public void respond(AbstractWebContext context)  throws Exception{
		try {
			context.getResponse().setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
			context.getResponse().setHeader("Pragma", "no-cache"); 
			context.getResponse().setDateHeader("Expires", 0); 
			context.getResponse().setContentType("text/html");
			XslTransformer.transform(context, modelView);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
