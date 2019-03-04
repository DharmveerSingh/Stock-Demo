package ca.gl.StockExchange.constant;

import java.text.SimpleDateFormat;

public final class AppConstants {

	// @Value("input.basePath")
	public static final String BASE_PATH = "D:\\stockData\\live\\";

	// @Value("input.archivePath")
	public static final String ARCHIVE_PATH = "D:\\stockData\\arch\\";
	public static final String LATEST = "LATEST::";
	public static final String HISTORY = "HISTORY::";
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	public static final String USER_PREFIX = "USER::";
	public static final String BLANK_STRING = "";

	public static final String ERROR_USER_EXIST = "User already exist with given emailId";

	public static final String BLANK_EMAIL_ERROR = "Please provide email";

	public static final String BLANK_PASSWORD_ERROR = "Password must be non empty string";

	public static final String STOCK_PREFIX = "STOCK::";

	public static final String PURCHASE_PREFIX = "PURCHASE::";
}
