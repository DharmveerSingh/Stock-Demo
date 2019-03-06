package ca.gl.fileUploader.helper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.gl.fileUploader.config.MessageProducer;
import ca.gl.fileUploader.dao.StockRepository;
import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.service.AsyncService;

//@Component
/**
 * class to upload files
 * @author dharamveer.singh
 *
 */
public class FileUploader implements Runnable {
	private File file;
	private Logger log = LoggerFactory.getLogger(FileUploader.class);
	
	@Autowired
	private AsyncService asyncService;
	
	private MessageProducer kafkaProducer;

	public FileUploader(File file, StockRepository repo, MessageProducer kafkaProducer, AsyncService asyncService) throws IOException {
		this.file = file;
		this.kafkaProducer = kafkaProducer;
		this.asyncService= asyncService;

	}

	public void run() {

		System.out.println("Going to process: " + file);

		String ext = file.getName().substring(file.getName().lastIndexOf('.') + 1);

		switch (ext) {
		case "xlsx":
		case "xls":
			saveAsList(ext);
			// saveAsStream(file, ext);
			break;

		case "txt":
			saveAsStream(file, ext);
			break;
		case "csv":
			saveAsStream(file, ext);
			break;
			
		default:
			removeFile(file, ext);
			;
		}

	}

	private void removeFile(File file2, String ext) {
		FileReader reader = FileReaderFactory.getFileReader(file, ext);
		try {
			reader.readFileAsList();
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
		}
	}

	private void saveAsStream(File inputFile, String ext) {
		FileReader reader = FileReaderFactory.getFileReader(file, ext);
		log.info("Started reading file: {} with extension {}", file.getAbsolutePath(), ext);
		long startTime = System.currentTimeMillis();

		try (Stream<Stock> stream = reader.readFileAsStream();) {
			List<Stock>stockList=stream.parallel().map(s -> {
				kafkaProducer.sentStocks(s);
				return s;
			}).collect(Collectors.toList());
			long endTime = System.currentTimeMillis();
			
			if(asyncService!=null) {
				log.info("Calling saveStocksUpdateHis");
				asyncService.saveStocksUpdateHis(stockList);
				}
				else
					log.error("Asynch service is null");
				
			log.info("Finished reading file: {}, time taken {} milliseconds", file.getAbsolutePath(), (endTime - startTime));
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
		}
	}

	private void saveAsList(String ext) {

		FileReader reader = FileReaderFactory.getFileReader(file, ext);

		if (reader != null) {
			List<Stock> list = null;
			try {
				log.info("Started reading file: {} with extension {}", file.getAbsolutePath(), ext);
				long startTime = System.currentTimeMillis();
				list = reader.readFileAsList();
				list.stream().forEach(st -> {
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
				asyncService.saveStocksUpdateHis(list);
				}
				else
					log.error("Asynch service is null");
				log.info("Finished writing to database file: {} with extension {}", file.getAbsolutePath(), ext);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Data saved");
		}

	}
}
