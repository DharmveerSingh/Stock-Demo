package com.gl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gl.model.User;

/**
 * @author dharamveer.singh
 *
 */
@Repository
public interface UserRepository 
  extends CrudRepository<User, String> {
  
	
}