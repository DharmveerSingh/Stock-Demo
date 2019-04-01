package ca.gl.fus.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.fus.model.StockHistory;

/**
 * The Interface StockHistoryRepository.
 */
@Repository
public interface StockHistoryRepository extends ReactiveCrudRepository<StockHistory, String> {

}