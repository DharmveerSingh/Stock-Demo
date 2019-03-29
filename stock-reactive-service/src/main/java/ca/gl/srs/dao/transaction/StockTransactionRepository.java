package ca.gl.srs.dao.transaction;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.user.model.transaction.StockTransaction;

/**
 * The Interface StockTransactionRepository.
 */
@Repository
public interface StockTransactionRepository extends ReactiveCrudRepository<StockTransaction, String>{
}
