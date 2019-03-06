package ca.gl.fileUploader.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constant.AppConstants;

/**
 * Utility class
 * 
 * @author dharamveer.singh
 *
 */
public class Utils {
	private static final Logger log = LoggerFactory.getLogger(Utils.class);

	/**
	 * Move given file to archive directory
	 * 
	 * @param fileName
	 * @return
	 */
	public static Path moveFileToArch(String fileName) {
		Path temp = null;
		try {
			temp = Files.move(Paths.get(AppConstants.BASE_PATH + fileName), Paths.get(AppConstants.ARCHIVE_PATH
					+ Calendar.getInstance().getTimeInMillis() + fileName.replaceAll(" ", "")));

			if (temp != null) {
				log.debug("File renamed and moved successfully");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return temp;
	}

	/**
	 * Move files to unprocessed dir
	 * 
	 * @param fileName
	 * @return
	 */
	public static Path moveFileToUnProcessed(String fileName) {
		Path temp = null;
		try {
			temp = Files.move(Paths.get(AppConstants.BASE_PATH + fileName), Paths.get(AppConstants.UNPROCESSED_PATH
					+ Calendar.getInstance().getTimeInMillis() + fileName.replaceAll(" ", "")));

			if (temp != null) {
				log.debug("File renamed and moved to unprocessed successfully");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return temp;
	}
}
