package ca.gl.fus.dao;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.fus.model.StockHistoryList;
import reactor.core.publisher.Flux;

/**
 * The Interface StockHistoryListRepository.
 */
@Repository
public interface StockHistoryListRepository extends ReactiveCrudRepository<StockHistoryList, String>{

	/**
	 * Gets the history for all.
	 *
	 * @return the history for all
	 */
	@Query("#{#n1ql.selectEntity} where  META().id LIKE \"REGULAR::HISTORY::%\"")
	Flux<StockHistoryList> getHistoryForAll();

}