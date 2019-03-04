package ca.gl.StockExchange.utility;

 import static ca.gl.StockExchange.constant.AppConstants.BLANK_STRING;
public final class Utils {
	private Utils() {}
	
	public static boolean emptyString(String str) {
		if (str != null && BLANK_STRING.equals(str.trim())) {
			return true;
		} else
			return false;
	}
}
