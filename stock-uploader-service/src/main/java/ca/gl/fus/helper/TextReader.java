package ca.gl.fus.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ca.gl.fus.constant.AppConstants;
import ca.gl.fus.model.Stock;
import ca.gl.fus.utils.Utils;
import lombok.NoArgsConstructor;

/**
 * Class for reading text files.
 *
 * @author dharamveer.singh
 */
@Component
@Qualifier("textReader")

/**
 * Instantiates a new text reader.
 */
@NoArgsConstructor
@Scope("prototype")
public class TextReader implements FileReader {
	
	/** The utils. */
	@Autowired
	private Utils utils;
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
	 * @see ca.gl.fus.helper.FileReader#readFileAsList()
	 */
	@Override
	public List<Stock> readFileAsList() throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Text files will be read as stream only");
	}

	/* (non-Javadoc)
	 * @see ca.gl.fus.helper.FileReader#readFileAsStream()
	 */
	@Override
	public Stream<Stock> readFileAsStream() throws OperationNotSupportedException {
		Path path = utils.moveFileToArch(file.getName());
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

	/* (non-Javadoc)
	 * @see ca.gl.fus.helper.FileReader#setFile(java.io.File)
	 */
	@Override
	public void setFile(File file) {
		this.file=file;
		
	}

}
