package ca.gl.srs.utility;

import static ca.gl.srs.constant.AppConstants.BLANK_STRING;

/**
 * The Class Utils.
 */
public final class Utils {

	/**
	 * Instantiates a new utils.
	 */
	private Utils() {
	}

	/**
	 * Method to check null or empty string.
	 *
	 * @param str the str
	 * @return true, if successful
	 */
	public static boolean emptyString(String str) {
		return (str != null && BLANK_STRING.equals(str.trim())) ? Boolean.TRUE : Boolean.FALSE;
	}
}
