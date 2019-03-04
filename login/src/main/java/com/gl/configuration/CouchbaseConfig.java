package com.gl.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

/**
 * @author dharamveer.singh
 *
 */
@Configuration
//@EnableCouchbaseRepositories(basePackages={"com.gl.*"})
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

	@Override
	protected List<String> getBootstrapHosts() {
		return Arrays.asList("localhost:8091");
	}

	@Override
	protected String getBucketName() {
		return "Stock";
	}

	@Override
	protected String getBucketPassword() {
		return "Password";
	}

	protected String getUsername() {
		return "Administrator";
	}
}