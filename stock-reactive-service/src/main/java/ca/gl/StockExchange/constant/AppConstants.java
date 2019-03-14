package ca.gl.StockExchange.constant;

import java.text.SimpleDateFormat;

/**
 * The Class AppConstants.
 */
public final class AppConstants {

	/** The Constant BASE_PATH. */
	// @Value("input.basePath")
	public static final String BASE_PATH = "D:\\stockData\\live\\";

	/** The Constant ARCHIVE_PATH. */
	// @Value("input.archivePath")
	public static final String ARCHIVE_PATH = "D:\\stockData\\arch\\";
	
	/** The Constant LATEST. */
	public static final String LATEST = "LATEST::";
	
	/** The Constant HISTORY. */
	public static final String HISTORY = "HISTORY::";
	
	/** The Constant dateFormat. */
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	/** The Constant USER_PREFIX. */
	public static final String USER_PREFIX = "USER::";
	
	/** The Constant BLANK_STRING. */
	public static final String BLANK_STRING = "";

	/** The Constant ERROR_USER_EXIST. */
	public static final String ERROR_USER_EXIST = "User already exist with given emailId";

	/** The Constant BLANK_EMAIL_ERROR. */
	public static final String BLANK_EMAIL_ERROR = "Please provide email";

	/** The Constant BLANK_PASSWORD_ERROR. */
	public static final String BLANK_PASSWORD_ERROR = "Password must be non empty string";

	/** The Constant STOCK_PREFIX. */
	public static final String STOCK_PREFIX = "STOCK::";

	/** The Constant PURCHASE_PREFIX. */
	public static final String PURCHASE_PREFIX = "PURCHASE::";
}
