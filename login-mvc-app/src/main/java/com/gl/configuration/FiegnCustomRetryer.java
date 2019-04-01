package com.gl.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import feign.RetryableException;
import feign.Retryer;

/**
 * The Class FiegnCustomRetryer.
 */
public class FiegnCustomRetryer implements Retryer {

	/** The max attempts. */
	private final int maxAttempts;

	/** The backoff. */
	private final long backoff;

	/** The attempt. */
	int attempt;
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(FiegnCustomRetryer.class);

	/**
	 * Instantiates a new fiegn custom retryer.
	 */
	public FiegnCustomRetryer() {
		//default
		this(2000, 5);
	}

	/**
	 * Instantiates a new fiegn custom retryer.
	 *
	 * @param backoff     the backoff
	 * @param maxAttempts the max attempts
	 */
	public FiegnCustomRetryer(long backoff, int maxAttempts) {
		this.backoff = backoff;
		this.maxAttempts = maxAttempts;
		this.attempt = 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see feign.Retryer#continueOrPropagate(feign.RetryableException)
	 */
	@Override
	public void continueOrPropagate(RetryableException e) {
		if (attempt++ >= maxAttempts) {
			log.error("Feign error: throwing error after retrying: attempt: {}", attempt);
			throw e;
		}

		try {
			log.error("Feign error: backing off: attempt: {}", attempt);
			Thread.sleep(backoff);
		} catch (InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Retryer clone() {
		return new FiegnCustomRetryer(backoff, maxAttempts);
	}

}
