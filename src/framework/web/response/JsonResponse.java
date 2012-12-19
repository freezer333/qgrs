package framework.web.response;

import org.codehaus.jackson.map.ObjectMapper;

import framework.web.AbstractWebContext;

public class JsonResponse implements Response {

	Object obj;
	
	public JsonResponse(Object o) {
		obj = o;
	}
	@Override
	public void respond(AbstractWebContext context) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		context.getResponse().setContentType("application/json");
		mapper.writeValue(context.getResponse().getOutputStream(), this.obj);
	}

}
