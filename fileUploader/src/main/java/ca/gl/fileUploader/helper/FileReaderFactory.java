package ca.gl.fileUploader.helper;

import java.io.File;

// TODO: Auto-generated Javadoc
/**
 * Factory of File reader object base on file type.
 *
 * @author dharamveer.singh
 */
public class FileReaderFactory {

	/**
	 * Instantiates a new file reader factory.
	 */
	private FileReaderFactory() {
	};

	/**
	 * Gets the file reader.
	 *
	 * @param file the file
	 * @param extension the extension
	 * @return the file reader
	 */
	public static FileReader getFileReader(File file, String extension) {

		FileReader reader = null;

		switch (extension) {
		case "xlsx":
		case "xls":
			reader = new ExcelReader(file);
			break;
			
		case "txt":
			reader= new TextReader(file);
			break;
			
		case "csv":
			reader= new CSVReader(file);
			break;
		default:
			reader= new ETCReader(file);
			;
		}
		return reader;
	}

	
}