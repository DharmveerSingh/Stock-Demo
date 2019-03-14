package ca.gl.fileUploader.service;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ca.gl.fileUploader.FileUploaderApplication;
import ca.gl.fileUploader.helper.FileUploader;

@Component
public class FileWatcherService {
	
	/** The log. */
	private Logger log = LoggerFactory.getLogger(FileWatcherService.class);

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private ExecutorService executor;
    
    /**
     * Creates a WatchService and registers the given directory
     */
    FileWatcherService(@Value("${input.basePath}") String basePath) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        int processors = Runtime.getRuntime().availableProcessors();
        this.executor= Executors.newFixedThreadPool(processors);
        walkAndRegisterDirectories(Paths.get(basePath));
    }
 
    /**
     * Register the given directory with the WatchService; This function will be called by FileVisitor
     */
    private void registerDirectory(Path dir) throws IOException
    {	
    	log.info("Inside registerDirectory with dir: {}"+ dir.getFileName());
    	//dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        WatchKey key = dir.register(watcher, ENTRY_CREATE);
        keys.put(key, dir);
    }
 
    /**
     * Register the given directory, and all its sub-directories, with the WatchService.
     */
    private void walkAndRegisterDirectories(final Path directory) throws IOException {
        // register directory 
    	log.info("Inside walkAndRegisterDirectories with path: {}", directory.getFileName());
    	registerDirectory(directory);
		/*
		 * Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
		 * 
		 * @Override public FileVisitResult preVisitDirectory(Path dir,
		 * BasicFileAttributes attrs) throws IOException { registerDirectory(dir);
		 * return FileVisitResult.CONTINUE; } });
		 */
    }
 
    /**
     * Process all events for keys queued to the watcher
     */
    public void processEvents() {
    	log.info("Inside process Events");
        for (;;) {
 
            // wait for key to be signaled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }
 
            Path dir = keys.get(key);
            if (dir == null) {
                log.error("WatchKey not recognized!!");
                continue;
            }
 
            for (WatchEvent<?> event : key.pollEvents()) {
                @SuppressWarnings("rawtypes")
                WatchEvent.Kind kind = event.kind();
 
                // Context for directory entry event is the file name of entry
                @SuppressWarnings("unchecked")
                Path name = ((WatchEvent<Path>)event).context();
                Path child = dir.resolve(name);
 
                // print out event
                log.info("{}: {}\n", event.kind().name(), child);
 
                if (kind == ENTRY_CREATE) {
                	FileUploader uploader=FileUploaderApplication.context.getBean(FileUploader.class);
                	uploader.setFile(child.toFile());
                	executor.execute(uploader);
					
                }
            }
 
            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
 
                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }
 
}