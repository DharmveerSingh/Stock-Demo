package ca.gl.fileUploader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ca.gl.fileUploader.dao.StockRepository;
import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.reader.FileReader;
import ca.gl.fileUploader.reader.FileReaderFactory;
import reactor.core.publisher.Flux;

// TODO: Auto-generated Javadoc
/**
 * this class will create a thread for each file which will be responsible for
 * processing file.
 *
 * @author dharamveer.singh
 */
@Component
@Scope("prototype")
public class FileUploader implements Runnable {
	
	/** The file path. */
	String filePath;

	/** The stock repository. */
	@Autowired
	private StockRepository stockRepository;

	/**
	 * Instantiates a new file uploader.
	 */
	public FileUploader() {
	}

	/** The repo. */
	private StockRepository repo;

	/**
	 * Constructor.
	 *
	 * @param file the file
	 * @param repo the repo
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public FileUploader(File file, StockRepository repo) throws IOException {
		this.repo = repo;
		this.filePath = file.getCanonicalPath();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		FileReader reader = FileReaderFactory.getFileReader(filePath);
		if (reader != null) {
			List<Stock> list = reader.readFile();
			try {
				Flux<Stock> saved = repo.saveAll(list);
				saved.doOnEach(stock -> System.out.println(stock))
						.doOnError(e -> System.out.println("Error while saving the data: " + e)).subscribe();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
