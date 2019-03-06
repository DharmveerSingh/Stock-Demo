package com.gl.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import ca.gl.fileUploader.model.Stock;

/**
 * Listen kafka message and add it to cache
 * @author dharamveer.singh
 *
 */
@Component
public class MessageListener {

	public static final ConcurrentHashMap<String,Stock> CACHE=new ConcurrentHashMap<>();
	public static final List<String> STOCKNAMES= new ArrayList<>();
	
	@KafkaListener(topics = "${message.topic.name}", groupId = "foo", containerFactory = "greetingKafkaListenerContainerFactory")
	public void listenGroupFoo(Stock stock) {
		
		if(CACHE.put(stock.getStockID(), stock)==null)
		{
			STOCKNAMES.add(stock.getStockID());	
		}
	}
}