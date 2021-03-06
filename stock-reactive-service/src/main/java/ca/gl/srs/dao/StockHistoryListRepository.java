package ca.gl.srs.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.fus.model.StockHistoryList;


/**
 * The Interface StockHistoryListRepository.
 */
@Repository
public interface StockHistoryListRepository 
  extends ReactiveCrudRepository<StockHistoryList, String> {
	
}