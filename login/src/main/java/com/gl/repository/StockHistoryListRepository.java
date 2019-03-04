package com.gl.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ca.gl.fileUploader.model.StockHistoryList;

@Repository
public interface StockHistoryListRepository extends ReactiveCrudRepository<StockHistoryList, String> {

}