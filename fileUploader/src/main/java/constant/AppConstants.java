package constant;

import java.text.SimpleDateFormat;

/**
 * The Class AppConstants.
 */
public final class AppConstants {

	/** The base path. */
	// @Value("input.basePath")
	public static String BASE_PATH = "D:\\stockData\\live\\";

	/** The archive path. */
	// @Value("input.archivePath")
	public static String ARCHIVE_PATH = "D:\\stockData\\arch\\";
	
	/** The unprocessed path. */
	public static String UNPROCESSED_PATH = "D:\\stockData\\unprocessed\\";
	
	/** The latest. */
	public static String LATEST = "LATEST::";
	
	
	/**  this constant will be used by scheduler to save daily history EOD. */
	public static String DAILY_HISTORY = "DAILY::HISTORY::";
	
	/**  this constant will be used by async to save regular history with each file read. */
	public static String REGULAR_HISTORY = "REGULAR::HISTORY::";
	
	/** The Constant dateFormat. */
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
}
