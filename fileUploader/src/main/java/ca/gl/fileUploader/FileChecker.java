package ca.gl.fileUploader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import ca.gl.fileUploader.dao.StockRepository;
import constant.AppConstants;

/**Class file Checker
 * @author dharamveer.singh
 *
 */
@Service
public class FileChecker {
	int processors = Runtime.getRuntime().availableProcessors();
    
	ExecutorService executor= Executors.newFixedThreadPool(processors);

	/**this method will keep looking for files in input directory
	 * @param repo
	 */
	public void keepProcessing(StockRepository repo) {
		File base =new File(AppConstants.BASE_PATH);
		
		
		System.out.println("Pool size is: "+ processors);
		File[] files;
		while(true) {
			files=base.listFiles();
			if(files.length !=0) {
				Arrays.stream(files).forEach(file ->  {
					try {
						executor.execute(new FileUploader(file, repo));
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				
			}
			else {
				System.out.println("No file found. ");
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

}
