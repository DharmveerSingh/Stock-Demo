package ca.gl.fileUploader.reader;

import java.util.List;

import ca.gl.fileUploader.model.Stock;

/**
 * The Interface FileReader.
 */
public interface FileReader {

	/**
	 * Read file.
	 *
	 * @return the list
	 */
	List<Stock> readFile();
}