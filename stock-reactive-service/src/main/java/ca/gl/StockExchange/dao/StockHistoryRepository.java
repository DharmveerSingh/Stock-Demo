package ca.gl.StockExchange.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.fileUploader.model.StockHistory;


/**
 * The Interface StockHistoryRepository.
 */
@Repository
public interface StockHistoryRepository 
  extends ReactiveCrudRepository<StockHistory, String> {
  
}