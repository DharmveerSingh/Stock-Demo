package ca.gl.srs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * The Class StockExchangeApplication.
 */
@EnableEurekaClient
@SpringBootApplication
public class StockExchangeApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(StockExchangeApplication.class, args);
	}
}

