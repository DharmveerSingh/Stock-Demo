package ca.gl.fileUploader.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.utils.Utils;
import constant.AppConstants;

// TODO: Auto-generated Javadoc
/**
 * Class for reading text files.
 *
 * @author dharamveer.singh
 */
public class TextReader implements FileReader {
	
	/** The log. */
	private Logger log = LoggerFactory.getLogger(TextReader.class);
	
	/** The file. */
	private File file;

	/**
	 * Instantiates a new text reader.
	 *
	 * @param file the file
	 */
	public TextReader(File file) {
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see ca.gl.fileUploader.helper.FileReader#readFileAsList()
	 */
	@Override
	public List<Stock> readFileAsList() throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Text files will be read as stream only");
	}

	/* (non-Javadoc)
	 * @see ca.gl.fileUploader.helper.FileReader#readFileAsStream()
	 */
	@Override
	public Stream<Stock> readFileAsStream() throws OperationNotSupportedException {
		Path path = Utils.moveFileToArch(file.getName());
		Stream<Stock> stream = null;
		try {
			long startTime = System.currentTimeMillis();
			stream = Files.lines(path).skip(1).map(line -> lineToStock(line));
			long endTime = System.currentTimeMillis();
			log.info("Finished reading file as stream: {}, time taken {} milliseconds", file.getAbsolutePath(), (endTime - startTime));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	/**
	 * Line to stock.
	 *
	 * @param line the line
	 * @return the stock
	 */
	private Stock lineToStock(String line) {
		Stock stock = new Stock();
		String[] arr = line.split("\\t+");
		String stockName;

		stockName = arr[0];
		stock.setStockID(AppConstants.LATEST + stockName);
		stock.setStockSymbol(stockName);
		stock.setPrevClose(Double.valueOf(arr[1]));
		stock.setPrice(Double.valueOf(arr[2]));
		stock.setPE(Double.valueOf(arr[3]));
		stock.setEPS(Double.valueOf(arr[4]));
		stock.setLow(Double.valueOf(arr[5]));
		stock.setHigh(Double.valueOf(arr[6]));
		stock.setVolume(Long.valueOf(arr[7]));
		stock.setWkLow(Double.valueOf(arr[8]));
		stock.setWkHigh(Double.valueOf(arr[9]));
		stock.setOpenPrice(Double.valueOf(arr[10]));

		return stock;
	}

}
