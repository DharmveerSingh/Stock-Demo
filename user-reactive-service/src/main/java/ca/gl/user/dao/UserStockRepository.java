package ca.gl.user.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.user.model.UserStock;

/**
 * The Interface UserStockRepository.
 */
@Repository
public interface UserStockRepository 
  extends ReactiveCrudRepository<UserStock, String> {
  
	
}