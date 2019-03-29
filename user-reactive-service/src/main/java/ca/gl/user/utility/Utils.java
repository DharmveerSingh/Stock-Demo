package ca.gl.user.utility;

import static ca.gl.user.constant.AppConstants.BLANK_STRING;

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
		if (str != null && BLANK_STRING.equals(str.trim())) {
			return true;
		} else
			return false;
	}
}
