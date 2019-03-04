package constant;

import java.text.SimpleDateFormat;

public final class AppConstants {

	// @Value("input.basePath")
	public static String BASE_PATH = "D:\\stockData\\live\\";

	// @Value("input.archivePath")
	public static String ARCHIVE_PATH = "D:\\stockData\\arch\\";
	public static String LATEST = "LATEST::";
	
	
	/** this constant will be used by scheduler to save daily history EOD */
	public static String DAILY_HISTORY = "DAILY::HISTORY::";
	
	/** this constant will be used by async to save regular history with each file read*/
	public static String REGULAR_HISTORY = "REGULAR::HISTORY::";
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
}
