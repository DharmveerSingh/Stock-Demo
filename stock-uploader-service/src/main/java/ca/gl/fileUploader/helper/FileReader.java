package ca.gl.fileUploader.helper;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import javax.naming.OperationNotSupportedException;

import ca.gl.fileUploader.model.Stock;

/**
 * File reader interface.
 *
 * @author dharamveer.singh
 */
public interface FileReader {

	/**
	 * Read file as list.
	 *
	 * @return the list
	 * @throws OperationNotSupportedException the operation not supported exception
	 */
	List<Stock> readFileAsList() throws OperationNotSupportedException;
	
	/**
	 * Read file as stream.
	 *
	 * @return the stream
	 * @throws OperationNotSupportedException the operation not supported exception
	 */
	Stream<Stock> readFileAsStream() throws OperationNotSupportedException;
	
	
	public void setFile(File file);
}
