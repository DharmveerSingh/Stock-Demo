package ca.gl.fileUploader.dao;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.fileUploader.model.StockHistoryList;
import reactor.core.publisher.Flux;

/**
 * The Interface StockHistoryListRepository.
 */
@Repository
public interface StockHistoryListRepository extends ReactiveCrudRepository<StockHistoryList, String>{

	@Query("#{#n1ql.selectEntity} where  META().id LIKE \"REGULAR::HISTORY::%\"")
	Flux<StockHistoryList> getHistoryForAll();

}