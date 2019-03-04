package ca.gl.fileUploader.dao;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.fileUploader.model.Stock;
import reactor.core.publisher.Flux;

@Repository
public interface StockRepository extends ReactiveCrudRepository<Stock, String> {

	@Query("#{#n1ql.selectEntity} where  META().id LIKE \"LATEST%\"")
	Flux<Stock> findAllLatest();
}