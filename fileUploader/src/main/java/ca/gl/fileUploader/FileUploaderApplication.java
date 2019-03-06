package ca.gl.fileUploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import ca.gl.fileUploader.config.MessageProducer;
import ca.gl.fileUploader.dao.StockRepository;
import ca.gl.fileUploader.service.AsyncService;
import ca.gl.fileUploader.service.FileCheckerService;

/**
 * Starting point of the application
 * 
 * @author dharamveer.singh
 *
 */
@SpringBootApplication
@EnableScheduling
public class FileUploaderApplication {

	public static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		context = SpringApplication.run(FileUploaderApplication.class, args);
		FileCheckerService fileChecker = context.getBean(FileCheckerService.class);
		MessageProducer kafkaProducer = context.getBean(MessageProducer.class);
		StockRepository repo = context.getBean(StockRepository.class);
		AsyncService asyncService = context.getBean(AsyncService.class);
		System.out.println(fileChecker);

		fileChecker.keepProcessing(repo, kafkaProducer, asyncService);
	}

	/**
	 * Message producer bean
	 * @return
	 */
	@Bean
	public MessageProducer messageProducer() {
		return new MessageProducer();
	}

}
