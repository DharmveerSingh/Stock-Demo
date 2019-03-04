package com.gl.constants;

import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentSkipListSet;

import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.model.StockHistoryList;

public class AppConstants {


	public static final String BASE_PATH="D:\\stockData\\live\\";
	
	public static final String ARCHIVE_PATH="D:\\stockData\\arch\\";
	public static final String LATEST="LATEST::";
	public static final String HISTORY="HISTORY::";
	public static final SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
	public static final String USER_PREFIX="USER::";
	public static final String BLANK_STRING="";
	
	public static final String ERROR_USER_EXIST="User already exist with given emailId";
	
	public static final String BLANK_EMAIL_ERROR="Please provide email";

	public static final String BLANK_PASSWORD_ERROR = "Password must be non empty string";
	
	public static final String STOCK_PREFIX="STOCK::";
	
	public static final String PURCHASE_PREFIX="PURCHASE::";
	
	public static final String PARAM_OFFSET="offset";
	
	public static final String PARAM_PAGE_SIZE="pageSize";
	
	public static final String USER_STOCK_LIST="userStockList";
	
	public static final String WELCOME="Welcome";
	
	public static final String SPACE = " ";
	
	public static final String OPENING_BRACES=" (";
	
	public static final String CLOSING_BRACES=")";
	
	public static final String USERNAME="username";
	
	public static final String STOCK_LIST="stockList";
	
	public static final String USER_HOME_VIEW="user/home";
	
	public static final String Welcome_MSG="Welcome, ";
	
}
