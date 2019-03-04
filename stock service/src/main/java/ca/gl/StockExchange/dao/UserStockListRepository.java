package ca.gl.StockExchange.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.StockExchange.model.UserStockList;

@Repository
public interface UserStockListRepository 
  extends ReactiveCrudRepository<UserStockList, String> {
  
	
}