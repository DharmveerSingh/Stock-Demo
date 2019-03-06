package ca.gl.fileUploader.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.utils.Utils;
import constant.AppConstants;

/**unknown file reader
 * @author dharamveer.singh
 *
 */
public class ETCReader implements FileReader {
	private File file;

	public ETCReader(File file) {
		this.file = file;
	}

	/* this method will move file to unprocessed folder when file type is unknown
	 * (non-Javadoc)
	 * @see ca.gl.fileUploader.helper.FileReader#readFileAsList()
	 */
	@Override
	public List<Stock> readFileAsList() throws OperationNotSupportedException {
		 Utils.moveFileToUnProcessed(file.getName());
		return new ArrayList<>();
	}

	/* Not supported
	 * (non-Javadoc)
	 * @see ca.gl.fileUploader.helper.FileReader#readFileAsStream()
	 */
	@Override
	public Stream<Stock> readFileAsStream() throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Not supported for unknown file types");
	}

}
