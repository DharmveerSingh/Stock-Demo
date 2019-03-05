package ca.gl.fileUploader.helper;

import java.io.File;

public class FileReaderFactory {

	private FileReaderFactory() {
	};

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