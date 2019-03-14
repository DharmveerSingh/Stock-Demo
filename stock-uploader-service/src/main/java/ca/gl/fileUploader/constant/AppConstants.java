package ca.gl.fileUploader.constant;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The Class AppConstants.
 */
@Component
public final class AppConstants {

	/** The base path. */
	@Value(value = "${input.basePath}")
	public String BASE_PATH;// = "D:\\stockData\\live\\";

	/** The archive path. */
	@Value(value = "${input.archivePath}")
	public String ARCHIVE_PATH;// = "D:\\stockData\\arch\\";
	
	/** The unprocessed path. */
	@Value(value = "${input.unprocessedPath}")
	public String UNPROCESSED_PATH;// = "D:\\stockData\\unprocessed\\";
	
	/** The latest. */
	public static String LATEST = "LATEST::";
	
	
	/**  this constant will be used by scheduler to save daily history EOD. */
	public static String DAILY_HISTORY = "DAILY::HISTORY::";
	
	/**  this constant will be used by async to save regular history with each file read. */
	public static String REGULAR_HISTORY = "REGULAR::HISTORY::";
	
	/** The Constant dateFormat. */
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	/** The Constant DOT. */
	public static final char DOT = '.';
}
