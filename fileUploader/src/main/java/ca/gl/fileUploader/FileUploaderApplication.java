package ca.gl.fileUploader;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import ca.gl.fileUploader.service.FileWatcherService;

/**
 * Starting point of the application.
 *
 * @author dharamveer.singh
 */
@SpringBootApplication
@EnableScheduling
public class FileUploaderApplication {

	/** The context. */
	public static ConfigurableApplicationContext context;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		context = SpringApplication.run(FileUploaderApplication.class, args);
		FileWatcherService fileWatcher = context.getBean(FileWatcherService.class);
		fileWatcher.processEvents();

	}

}
