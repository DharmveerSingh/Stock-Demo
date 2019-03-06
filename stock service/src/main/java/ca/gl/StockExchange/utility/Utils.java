package ca.gl.StockExchange.utility;

 import static ca.gl.StockExchange.constant.AppConstants.BLANK_STRING;
public final class Utils {
	private Utils() {}
	
	/**Method to check null or empty string
	 * @param str
	 * @return
	 */
	public static boolean emptyString(String str) {
		if (str != null && BLANK_STRING.equals(str.trim())) {
			return true;
		} else
			return false;
	}
}
