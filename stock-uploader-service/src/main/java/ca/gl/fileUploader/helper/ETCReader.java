package ca.gl.fileUploader.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.naming.OperationNotSupportedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.utils.Utils;
import lombok.NoArgsConstructor;

/**
 * unknown file reader.
 *
 * @author dharamveer.singh
 */
@Component
@Qualifier("etcReader")
@Scope("prototype")
@NoArgsConstructor
public class ETCReader implements FileReader {
	
	/** The file. */
	private File file;
	
	@Autowired
	private Utils utils;
	/**
	 * Instantiates a new ETC reader.
	 *
	 * @param file the file
	 */
	public ETCReader(File file) {
		this.file = file;
	}

	/* this method will move file to unprocessed folder when file type is unknown
	 * (non-Javadoc)
	 * @see ca.gl.fileUploader.helper.FileReader#readFileAsList()
	 */
	@Override
	public List<Stock> readFileAsList() throws OperationNotSupportedException {
		utils.moveFileToUnProcessed(file.getName());
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

	@Override
	public void setFile(File file) {
		this.file=file;
	}

}
