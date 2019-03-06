package ca.gl.StockExchange;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

/**
 * @author dharamveer.singh
 *
 */
@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

	@Value("${spring.couchbase.bucket.name}")
	private String bucket;
	
	@Value("${spring.couchbase.bucket.password}")
	private String password;

	@Value("${spring.couchbase.bootstrap-hosts}")
	private String host;
	
	@Value("${spring.couchbase.username}")
	private String username;
	
	@Override
	protected List<String> getBootstrapHosts() {
		return Arrays.asList(host);
	}

	@Override
	protected String getBucketName() {
		return bucket;
	}

	@Override
	protected String getBucketPassword() {
		return password;
	}

	protected String getUsername() {
		return username;
	}
}