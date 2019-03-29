package com.gl.model;

import lombok.Data;

/**
 * Instantiates a new buy stock form.
 */
@Data
public class BuyStockForm {

	/** The id. */
	private String id;
	
	/** The price. */
	private Double price;
	
	/** The count. */
	private Integer count;
	
	/** The count. */
	private String name;
	
	/** The email. */
	private String email;
}
