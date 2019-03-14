package ca.gl.fileUploader.error;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

/**
 * Async exception handler.
 *
 * @author dharamveer.singh
 */
@Component
public class CustomAsyncExceptionHandler
implements AsyncUncaughtExceptionHandler {
	
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);
 
	/* (non-Javadoc)
	 * @see org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler#handleUncaughtException(java.lang.Throwable, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
  public void handleUncaughtException(
    Throwable throwable, Method method, Object... obj) {

      log.error("Exception message - {}" , throwable.getMessage());
      log.error("Method name - {}" , method.getName());
      for (Object param : obj) {
          log.error("Parameter value - {}" , param);
      }
  }

}