package ca.gl.fus;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import ca.gl.fus.service.FileWatcherService;

/**
 * Starting point of the application.
 *
 * @author dharamveer.singh
 */
@SpringBootApplication
@EnableScheduling
@EnableEurekaClient
public class FileUploaderApplication {

	/** The context. */
	public static ConfigurableApplicationContext CONTEXT;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		CONTEXT = SpringApplication.run(FileUploaderApplication.class, args);
		FileWatcherService fileWatcher = CONTEXT.getBean(FileWatcherService.class);
		fileWatcher.processEvents();
	}
}
