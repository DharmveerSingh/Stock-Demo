package ca.gl.fileUploader.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import ca.gl.fileUploader.error.CustomAsyncExceptionHandler;

// TODO: Auto-generated Javadoc
/**
 * Async configuration class.
 *
 * @author dharamveer.singh
 */
@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.annotation.AsyncConfigurer#getAsyncUncaughtExceptionHandler()
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
	    return new CustomAsyncExceptionHandler();
	}
	
	 /* (non-Javadoc)
 	 * @see org.springframework.scheduling.annotation.AsyncConfigurer#getAsyncExecutor()
 	 */
 	@Override
	    public Executor getAsyncExecutor() {
		 ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		 executor.setThreadGroupName("Async-Service-Group");
		 executor.setThreadNamePrefix("Async-Thread-");
	        executor.initialize();
		 
	        return executor;
	    }
}