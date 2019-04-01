package ca.gl.fus.reader;

import java.util.List;

import ca.gl.fus.model.Stock;

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
