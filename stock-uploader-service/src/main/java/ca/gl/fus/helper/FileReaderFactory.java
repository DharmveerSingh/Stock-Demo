package ca.gl.fus.helper;

import java.io.File;

import org.springframework.stereotype.Component;

import ca.gl.fus.FileUploaderApplication;

/**
 * Factory of File reader object base on file type.
 *
 * @author dharamveer.singh
 */
@Component
public class FileReaderFactory {

	/**
	 * Instantiates a new file reader factory.
	 */
	private FileReaderFactory() {
	};

	/**
	 * Gets the file reader.
	 *
	 * @param file      the file
	 * @param extension the extension
	 * @return the file reader
	 */
	public static FileReader getFileReader(File file, String extension) {

		FileReader reader = null;

		switch (extension) {
		case "xlsx":
		case "xls":
			reader = (FileReader) FileUploaderApplication.CONTEXT.getBean(ExcelReader.class);
			reader.setFile(file);
			break;

		case "txt":
			reader = (FileReader) FileUploaderApplication.CONTEXT.getBean(TextReader.class);
			reader.setFile(file);
			break;

		case "csv":
			reader = (FileReader) FileUploaderApplication.CONTEXT.getBean(CSVReader.class);
			reader.setFile(file);
			break;
		default:
			reader = (FileReader) FileUploaderApplication.CONTEXT.getBean(ETCReader.class);
			reader.setFile(file);
		}
		
		return reader;
	}

}