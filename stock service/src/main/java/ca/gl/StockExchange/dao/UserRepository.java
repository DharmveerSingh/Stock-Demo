package ca.gl.StockExchange.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.StockExchange.model.User;

/**
 * The Interface UserRepository.
 */
@Repository
public interface UserRepository 
  extends ReactiveCrudRepository<User, String> {
  
	
}