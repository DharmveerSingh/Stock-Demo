package ca.gl.user.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface TransactionHistoryRepository.
 */
@Repository
public interface TransactionHistoryRepository extends ReactiveCrudRepository<TransactionHistoryRepository, String>{

}
