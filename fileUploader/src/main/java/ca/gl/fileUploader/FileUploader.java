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

@Component
@Scope("prototype")
public class FileUploader implements Runnable {
	String filePath;

	@Autowired
	private StockRepository stockRepository;

	public FileUploader() {
	}

	private StockRepository repo;

	public FileUploader(File file, StockRepository repo) throws IOException {
		this.repo = repo;
		this.filePath = file.getCanonicalPath();
		System.out.println("Repo: " + stockRepository);

	}

	public void run() {
		System.out.println("Repo: " + stockRepository);

		System.out.println("Going to process: " + filePath);
		FileReader reader = FileReaderFactory.getFileReader(filePath);

		if (reader != null) {
			List<Stock> list = reader.readFile();

			System.out.println("Saving data");
			try {
				Flux<Stock> saved = repo.saveAll(list);
				saved.doOnEach(stock -> System.out.println(stock))
						.doOnError(e -> System.out.println("Error while saving the data: " + e)).subscribe();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Data saved");
		}
	}
}