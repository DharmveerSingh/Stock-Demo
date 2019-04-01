package ca.gl.srs.kafka.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import ca.gl.srs.kafka.producer.MessageProducer;
import ca.gl.srs.service.StockService;
import ca.gl.user.model.transaction.Transaction;

/**
 * Listen kafka message.
 *
 * @author dharamveer.singh
 */
@Component
public class KafkaResultListener {

	/** The stock service. */
	@Autowired
	private StockService stockService;
	
	/** The kafka. */
	@Autowired
	private MessageProducer kafka;
	
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(KafkaResultListener.class);
	
	/**
	 * Listen group buyResult.
	 *
	 * @param transaction the stock
	 */
	@KafkaListener(topics = "${message.topic.name.buy.stocks}", groupId = "buyStocks", containerFactory = "buyStocksKafkaListenerContainerFactory")
	public void listenBuyStockResult(Transaction transaction) {
		
		log.error("Recieved Kafka buy stocks as: {}",transaction);
		stockService.purchase(transaction).map(tr -> {
			log.info("Sending to topic buyResult: {}",transaction);
			kafka.sentBuyResult(transaction);
			return tr;
		}).subscribe();
	}
	
	/**
	 * Listen group buyResult.
	 *
	 * @param transaction the stock
	 */
	@KafkaListener(topics = "${message.topic.name.buy.rollback}", groupId = "buyRollback", containerFactory = "buyRollbackKafkaListenerContainerFactory")
	public void listenBuyRollbackResult(Transaction transaction) {
		log.error("Recieved Kafka rollback stocks as: {}",transaction);
		stockService.rollback(transaction);
	}
}