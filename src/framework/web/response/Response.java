
package framework.web.response;

import framework.web.AbstractWebContext;

public interface Response {
	void respond (AbstractWebContext context) throws Exception;
}
