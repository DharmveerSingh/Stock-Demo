package com.gl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gl.service.StockService;

import ca.gl.fileUploader.model.StockHistoryList;

/**
 * The stock controller.
 *
 * @author dharamveer.singh
 */
@Controller
@RequestMapping("/stock")
public class StockController {
	
	/** The log. */
	private Logger log = LoggerFactory.getLogger(StockController.class);
	
	/** The stock service. */
	@Autowired
	private StockService stockService;
	

	/**
	 * Gets the todays history.
	 *
	 * @param stockSymbol the stock symbol
	 * @return the todays history
	 */
	@GetMapping("/todaysHistory")
	public ModelAndView getTodaysHistory(@RequestParam("stockSymbol") String stockSymbol) {
		log.info("Request recived to show stock todays history with symbol: {}", stockSymbol);
		StockHistoryList model=stockService.getTodaysHistory(stockSymbol);
		ModelAndView mv= new ModelAndView();
		mv.addObject("response" , model);
		
		mv.addObject("subtitle", stockSymbol+"'s Today");
		mv.addObject("title", "Stock's Todays Report");
		mv.setViewName("stock/stockHistory");
		
		log.info("history list: "+ model.getStockList().size());

		return mv;
	}
	
	/**
	 * Gets the weeks history.
	 *
	 * @param stockSymbol the stock symbol
	 * @return the weeks history
	 */
	@GetMapping("/weeksHistory")
	public ModelAndView getWeeksHistory(@RequestParam("stockSymbol") String stockSymbol) {
		log.info("Request recived to show stock weekly history with symbol: {}", stockSymbol);
		StockHistoryList model=stockService.getWeeksHistory(stockSymbol);
		ModelAndView mv= new ModelAndView();
		mv.addObject("response" , model);
		mv.addObject("subtitle", stockSymbol+"'s over last week");
		mv.setViewName("stock/stockHistory");
		mv.addObject("title", "Stocks Weekly Report");
		log.info("history list: "+ model.getStockList().size());
	    
		return mv;
	}

}
