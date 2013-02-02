
package framework.web.response;

import org.apache.poi.ss.usermodel.Workbook;

import framework.web.AbstractWebContext;


public class ExcelResponse implements Response {
	public enum Type {xls, xlsx};
	Workbook wb;
	String filename;
	Type type;
	
	public ExcelResponse(String filename, Workbook wb, Type type) {
		this.wb = wb;
		this.filename = filename;
		this.type = type;
	}
	
	
	@Override
	public void respond(AbstractWebContext context) throws Exception {
		try {
		String fileType = "attachment;filename=" + filename + "." + type.toString();
		context.getResponse().setContentType("application/" + type.toString());
		context.getResponse().setHeader("Content-Disposition", fileType);
		wb.write(context.getResponse().getOutputStream());
		context.getResponse().getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
