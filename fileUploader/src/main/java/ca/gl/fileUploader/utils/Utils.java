package ca.gl.fileUploader.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.gl.fileUploader.constant.AppConstants;

/**
 * Utility class.
 *
 * @author dharamveer.singh
 */
@Component
public class Utils {
	
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(Utils.class);
	
	@Autowired
	private AppConstants appConstants;
	
	/**
	 * Move given file to archive directory.
	 *
	 * @param fileName the file name
	 * @return the path
	 */
	public Path moveFileToArch(String fileName) {
		Path temp = null;
		int attempt=0;
		do {
			try {
				temp = Files.move(Paths.get(appConstants.BASE_PATH + fileName), Paths.get(appConstants.ARCHIVE_PATH
						+ Calendar.getInstance().getTimeInMillis() + fileName.replaceAll(" ", "")));
				break;
			} catch (IOException e) {
				e.printStackTrace();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				log.info("**********************retrying file renaming {}", attempt);
				attempt++;
			} 
		} while (attempt<5);
		return temp;
	}


	/**
	 * Move file to un processed.
	 *
	 * @param fileName the file name
	 * @return the path
	 */
	public Path moveFileToUnProcessed(String fileName) {
		Path temp = null;
		try {
			temp = Files.move(Paths.get(appConstants.BASE_PATH + fileName), Paths.get(appConstants.UNPROCESSED_PATH
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
