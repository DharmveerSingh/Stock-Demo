package ca.gl.srs.responses.transaction;

import ca.gl.user.model.transaction.Stock;
import lombok.Data;

/**
 * Instantiates a new stock response.
 */
@Data
public class StockResponse {
 
 /** The stock. */
 private Stock stock;
 
 /** The is available. */
 private boolean isAvailable;
 
 /** The is reserved. */
 private boolean isReserved;
 
 /** The is error. */
 private boolean isError;

 /** The is CAS error. */
 private boolean isCASError;
 
 /** The throwable. */
 private Throwable throwable;
}
