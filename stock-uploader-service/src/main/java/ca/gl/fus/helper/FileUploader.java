package ca.gl.fus.helper;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ca.gl.fus.config.MessageProducer;
import ca.gl.fus.constant.AppConstants;
import ca.gl.fus.model.Stock;
import ca.gl.fus.service.AsyncService;

/**
 * class to upload files.
 *
 * @author dharamveer.singh
 */
@Component
@Scope("prototype")
public class FileUploader implements Runnable {
	
	/** The kafka producer. */
	@Autowired
	private MessageProducer kafkaProducer;
	
	/** The async service. */
	@Autowired
	private AsyncService asyncService;
	
	/** The file. */
	private File file;
	
	/** The log. */
	private Logger log = LoggerFactory.getLogger(FileUploader.class);
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		log.info("Going to process: {} with thread: {}", file , Thread.currentThread().getName());

		String extension = file.getName().substring(file.getName().lastIndexOf(AppConstants.DOT) + 1);

		switch (extension) {
		case "xlsx":
		case "xls":
			saveAsList(extension);
			break;

		case "txt":
		case "csv":
			saveAsStream(file, extension);
			break;
			
		default:
			removeFile(file, extension);
		}

	}

	/**
	 * Removes the file.
	 *
	 * @param file2 the file 2
	 * @param ext the ext
	 */
	private void removeFile(File file2, String ext) {
		FileReader reader = FileReaderFactory.getFileReader(file, ext);
		try {
			reader.readFileAsList();
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save as stream.
	 *
	 * @param inputFile the input file
	 * @param ext the ext
	 */
	private void saveAsStream(File inputFile, String ext) {
		FileReader reader = FileReaderFactory.getFileReader(file, ext);
		log.info("Started reading file: {} with extension {}", file.getAbsolutePath(), ext);
		long startTime = System.currentTimeMillis();

		try (Stream<Stock> stream = reader.readFileAsStream();) {
			List<Stock>stockList=stream.parallel().map(s -> {
				log.info("Sending to kafka: {}",s);
				kafkaProducer.sentStocks(s);
				return s;
			}).collect(Collectors.toList());
			long endTime = System.currentTimeMillis();
			
			if(asyncService!=null) {
				log.info("Calling saveStocksUpdateHis");
				asyncService.saveStocksUpdateHistory(stockList);
				}
				else
					log.error("Asynch service is null");
				
			log.info("Finished reading file: {}, time taken {} milliseconds", file.getAbsolutePath(), (endTime - startTime));
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save as list.
	 *
	 * @param ext the ext
	 */
	private void saveAsList(String ext) {

		FileReader reader = FileReaderFactory.getFileReader(file, ext);

		if (reader != null) {
			List<Stock> list = null;
			try {
				log.info("Started reading file: {} with extension {}", file.getAbsolutePath(), ext);
				long startTime = System.currentTimeMillis();
				list = reader.readFileAsList();
				list.stream().forEach(st -> {
					log.info("Sending to kafka: {}",st);
					kafkaProducer.sentStocks(st);
				});
				long endTime = System.currentTimeMillis();
				log.info("Finished with kafka with file: {}, time taken {} milliseconds", file.getAbsolutePath(), (endTime-startTime));
			} catch (OperationNotSupportedException e1) {
				e1.printStackTrace();
			}

			try {
				if(asyncService!=null) {
				log.info("Calling saveStocksUpdateHis");
				asyncService.saveStocksUpdateHistory(list);
				}
				else
					log.error("Asynch service is null");
				log.info("Finished writing to database file: {} with extension {}", file.getAbsolutePath(), ext);
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.info("Data saved");
		}
	}
	
	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
	public void setFile(File file) {
		this.file = file;
	}

}
