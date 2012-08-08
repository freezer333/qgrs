
package framework.web.response;

import org.apache.poi.ss.usermodel.Workbook;

import framework.web.AbstractWebContext;


public class ExcelResponse implements Response {

	Workbook wb;
	
	
	public ExcelResponse(Workbook wb) {
		this.wb = wb;
	}
	
	
	@Override
	public void respond(AbstractWebContext context) throws Exception {
		try {
		String fileType = "attachment;filename=" + "datadictionary" + ".xls";
		context.getResponse().setContentType("application/xls");
		context.getResponse().setHeader("Content-Disposition", fileType);
		wb.write(context.getResponse().getOutputStream());
		context.getResponse().getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
