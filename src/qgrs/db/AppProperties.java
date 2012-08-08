	package qgrs.db;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import framework.web.ResourceResolver;
import framework.web.ResourceType;
import framework.web.util.StringUtils;

public class AppProperties {

	
	public static String getConnectionString(ResourceResolver r) {
		try {
			SAXBuilder sb = new SAXBuilder();
		
			Document propsDoc = sb.build(r.getResourceFile(ResourceType.xml, "props.xml"));
			Element propsRoot = propsDoc.getRootElement();
			Element dbElement = propsRoot.getChild("db");
			if ( dbElement != null ) {
				if ( StringUtils.isDefined(dbElement.getText())) {
					return dbElement.getText();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean dropTables(ResourceResolver r) {
		try {
			SAXBuilder sb = new SAXBuilder();
		
			Document propsDoc = sb.build(r.getResourceFile(ResourceType.xml, "props.xml"));
			Element propsRoot = propsDoc.getRootElement();
			Element dbElement = propsRoot.getChild("db_drop");
			if ( dbElement != null ) {
				if ( StringUtils.isDefined(dbElement.getText())) {
					return Boolean.parseBoolean(dbElement.getText());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
