package ca.gl.srs.dao.transaction;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.user.model.transaction.Stock;
import reactor.core.publisher.Flux;

/**
 * The Interface TransactionStockRepository.
 */
@Repository
public interface TransactionStockRepository extends ReactiveCrudRepository<Stock, String>{
	
	/**
	 * Find all salable.
	 *
	 * @return the flux
	 */
	@Query("#{#n1ql.selectEntity} where  META().id LIKE 'SALABLE::%'")
	Flux<Stock> findAllSalable();

}
