package ca.gl.fileUploader.helper;

import java.util.List;
import java.util.stream.Stream;

import javax.naming.OperationNotSupportedException;

import ca.gl.fileUploader.model.Stock;

public interface FileReader {

	List<Stock> readFileAsList() throws OperationNotSupportedException;
	
	Stream<Stock> readFileAsStream() throws OperationNotSupportedException;
}
