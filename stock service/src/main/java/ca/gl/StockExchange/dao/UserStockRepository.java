package ca.gl.StockExchange.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.StockExchange.model.UserStock;

@Repository
public interface UserStockRepository 
  extends ReactiveCrudRepository<UserStock, String> {
  
	
}