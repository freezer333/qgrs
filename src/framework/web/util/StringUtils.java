package framework.web.util;

public class StringUtils {
	public static boolean isDefined(String s) {
		return s != null && s.trim().length() > 0 ;
	}
}
