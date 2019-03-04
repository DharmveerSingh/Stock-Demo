package ca.gl.fileUploader.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ca.gl.fileUploader.config.MessageProducer;
import ca.gl.fileUploader.dao.StockRepository;
import ca.gl.fileUploader.helper.FileUploader;
import constant.AppConstants;

@Service
public class FileCheckerService {
	int processors = Runtime.getRuntime().availableProcessors();

	ExecutorService executor = Executors.newFixedThreadPool(processors);

	/**
	 * this method will be keep checking file and process them if found in input dir
	 * 
	 * @param repo
	 * @param kafkaProducer
	 * @param asyncService 
	 */
	public void keepProcessing(StockRepository repo, MessageProducer kafkaProducer, AsyncService asyncService) {
		File base = new File(AppConstants.BASE_PATH);

		System.out.println("Pool size is: " + processors);
		File[] files;
		while (true) {
			files = base.listFiles();
			if (files.length != 0) {
				// System.out.println("File found in base dir. Going to process");
				Arrays.stream(files).forEach(file -> {
					try {
						executor.execute(new FileUploader(file, repo, kafkaProducer,asyncService));
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
