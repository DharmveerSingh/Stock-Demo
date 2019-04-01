package ca.gl.user.dao;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.user.model.transaction.Transaction;
import reactor.core.publisher.Flux;

/**
 * The Interface TransactionRepository.
 */
@Repository
public interface TransactionRepository extends ReactiveCrudRepository<Transaction, String>{

	@Query("#{#n1ql.selectEntity} where  META().id LIKE $1 order by created desc")
	Flux<Transaction> findAllByUserId(String userId);

}
