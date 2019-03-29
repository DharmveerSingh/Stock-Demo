package com.gl.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.couchbase.client.core.ServiceNotAvailableException;

import feign.Retryer;

/**
 * The Class WebMvcConfig.
 *
 * @author dharamveer.singh
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Value("${feign.client.retryer.backoff}")
	long backoff;
	
	@Value("${feign.client.retryer.maxAttempts}")
	int maxAttempts;
	/**
	 * Password encoder.
	 *
	 * @return the bcrypt password encoder
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	
	
	 /**
 	 * Retryer.
 	 *
 	 * @return the retryer
 	 */
 	@Bean
	    public Retryer retryer() {
	        return new FiegnCustomRetryer(backoff, maxAttempts);
	    }
	 
	 /**
 	 * Cause.
 	 *
 	 * @return the throwable
 	 */
 	@Bean
	 public Throwable cause() {
		 return new ServiceNotAvailableException();
	 }
}