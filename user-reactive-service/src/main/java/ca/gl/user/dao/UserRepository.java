package ca.gl.user.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.user.model.User;

/**
 * The Interface UserRepository.
 */
@Repository
public interface UserRepository 
  extends ReactiveCrudRepository<User, String> {
  
	
}