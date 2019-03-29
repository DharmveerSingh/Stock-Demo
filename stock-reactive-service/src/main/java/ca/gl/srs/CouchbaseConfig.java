package ca.gl.srs;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

/**
 * The Class CouchbaseConfig.
 *
 * @author dharamveer.singh
 */
@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

	/** The bucket. */
	@Value("${spring.couchbase.bucket.name}")
	private String bucket;
	
	/** The password. */
	@Value("${spring.couchbase.bucket.password}")
	private String password;

	/** The host. */
	@Value("${spring.couchbase.bootstrap-hosts}")
	private String host;
	
	/** The username. */
	@Value("${spring.couchbase.username}")
	private String username;
	
	/* (non-Javadoc)
	 * @see org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration#getBootstrapHosts()
	 */
	@Override
	protected List<String> getBootstrapHosts() {
		return Arrays.asList(host);
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration#getBucketName()
	 */
	@Override
	protected String getBucketName() {
		return bucket;
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration#getBucketPassword()
	 */
	@Override
	protected String getBucketPassword() {
		return password;
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration#getUsername()
	 */
	@Override
	protected String getUsername() {
		return username;
	}
}