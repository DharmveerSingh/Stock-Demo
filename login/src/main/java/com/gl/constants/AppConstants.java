package com.gl.constants;

import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentSkipListSet;

import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.model.StockHistoryList;

// TODO: Auto-generated Javadoc
/**
 * The Class AppConstants.
 */
public class AppConstants {


	/** The Constant BASE_PATH. */
	public static final String BASE_PATH="D:\\stockData\\live\\";
	
	/** The Constant ARCHIVE_PATH. */
	public static final String ARCHIVE_PATH="D:\\stockData\\arch\\";
	
	/** The Constant LATEST. */
	public static final String LATEST="LATEST::";
	
	/** The Constant HISTORY. */
	public static final String HISTORY="HISTORY::";
	
	/** The Constant dateFormat. */
	public static final SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
	
	/** The Constant USER_PREFIX. */
	public static final String USER_PREFIX="USER::";
	
	/** The Constant BLANK_STRING. */
	public static final String BLANK_STRING="";
	
	/** The Constant ERROR_USER_EXIST. */
	public static final String ERROR_USER_EXIST="User already exist with given emailId";
	
	/** The Constant BLANK_EMAIL_ERROR. */
	public static final String BLANK_EMAIL_ERROR="Please provide email";

	/** The Constant BLANK_PASSWORD_ERROR. */
	public static final String BLANK_PASSWORD_ERROR = "Password must be non empty string";
	
	/** The Constant STOCK_PREFIX. */
	public static final String STOCK_PREFIX="STOCK::";
	
	/** The Constant PURCHASE_PREFIX. */
	public static final String PURCHASE_PREFIX="PURCHASE::";
	
	/** The Constant PARAM_OFFSET. */
	public static final String PARAM_OFFSET="offset";
	
	/** The Constant PARAM_PAGE_SIZE. */
	public static final String PARAM_PAGE_SIZE="pageSize";
	
	/** The Constant USER_STOCK_LIST. */
	public static final String USER_STOCK_LIST="userStockList";
	
	/** The Constant WELCOME. */
	public static final String WELCOME="Welcome";
	
	/** The Constant SPACE. */
	public static final String SPACE = " ";
	
	/** The Constant OPENING_BRACES. */
	public static final String OPENING_BRACES=" (";
	
	/** The Constant CLOSING_BRACES. */
	public static final String CLOSING_BRACES=")";
	
	/** The Constant USERNAME. */
	public static final String USERNAME="username";
	
	/** The Constant STOCK_LIST. */
	public static final String STOCK_LIST="stockList";
	
	/** The Constant USER_HOME_VIEW. */
	public static final String USER_HOME_VIEW="user/home";
	
	/** The Constant Welcome_MSG. */
	public static final String Welcome_MSG="Welcome, ";
	
}
