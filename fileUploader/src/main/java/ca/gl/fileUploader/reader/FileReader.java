package ca.gl.fileUploader.reader;

import java.util.List;

import ca.gl.fileUploader.model.Stock;

public interface FileReader {

	List<Stock> readFile();
}
