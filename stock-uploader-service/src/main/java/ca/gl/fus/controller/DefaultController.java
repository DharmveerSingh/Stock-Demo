package ca.gl.fus.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * this controller will make it available to zipkin as microservice.
 *
 * @author dharamveer.singh
 */
@RestController
public class DefaultController {

	/**
	 * Say hello.
	 *
	 * @return the string
	 */
	@GetMapping("/")
	public String sayHello() {
		
		return "Hello from File Uploader";
	}
}
