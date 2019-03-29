package com.gl.controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gl.constants.AppConstants;
import com.gl.feign.stock.StockServiceProxy;
import com.gl.feign.user.UserServiceProxy;
import com.gl.model.BuyStockForm;
import com.gl.model.UserStock;
import com.gl.model.UserStockList;
import com.gl.service.UserService;

import ca.gl.user.model.transaction.Transaction;
import reactor.core.publisher.Mono;

/**
 * The User Contoller.
 *
 * @author dharamveer.singh
 */
@Controller
@RequestMapping("/user")
public class UserStockController {

	/** The user service proxy. */
	@Autowired
	private UserServiceProxy userServiceProxy;

	/** The stock service proxy. */
	@Autowired
	private StockServiceProxy stockServiceProxy;

	/** The user service. */
	@Autowired
	private UserService userService;

	/** The log. */
	private Logger log = LoggerFactory.getLogger(UserStockController.class);

	/**
	 * Home.
	 *
	 * @param request   the request
	 * @param principal the principal
	 * @param session   the session
	 * @return the model and view
	 */
	@GetMapping(value = "/home")
	public ModelAndView home(HttpServletRequest request, Principal principal, HttpSession session) {
		log.info("Home: inside userhome for user: {}", principal.getName());
		return userService.getModelAndView(request, principal, session);
	}

	/**
	 * Purchase stocks.
	 *
	 * @param stock     the stock
	 * @param request   the request
	 * @param principal the principal
	 * @return the mono
	 */
	@PostMapping("/purchase")
	@ResponseBody
	public Mono<String> purchaseStocks(@RequestBody UserStock stock, HttpServletRequest request, Principal principal) {
		log.info("purchaseStocks: ID: {}, username: {}" , stock.getStockID() , principal.getName());
		stock.setUserId(principal.getName());
		stock.setUserStockId(stock.getStockID() + Calendar.getInstance().getTimeInMillis());
		UserStockList res = userServiceProxy.saveUserStock(stock);

		if (res.getError() == null) {
			log.info("Response returned by post: {}", res);
			return Mono.just("done");
		} else {
			log.info("Response returned by post: {}", res);
			return Mono.error(res.getError());
		}
	}

	/**
	 * Removes the purchase.
	 *
	 * @param stockId  the stock id
	 * @param username the username
	 * @param request the request
	 * @return the mono
	 */
	@GetMapping("/removePurchase")
	@ResponseBody
	public Mono<String> removePurchase(@RequestParam("userStockId") String stockId,
			@RequestParam("userName") String username, HttpServletRequest request) {
		log.info("removePurchase: username: {}, username: {}" , stockId , username);
	
		UserStockList res = userServiceProxy.removePurchase(stockId,AppConstants.PURCHASE_PREFIX + username);
		if (res.getError() == null)
			return Mono.just("done");
		else
			return Mono.error(res.getError());
	}
	 
	/**
	 * Buy stocks.
	 *
	 * @param principal the principal
	 * @param req the req
	 * @param form the form
	 * @return the string
	 */
	@PostMapping("/buyStocks")
	@ResponseBody
	  public String buyStocks(Principal principal, HttpServletRequest req, @RequestBody BuyStockForm form) {
		  ModelAndView mav= new ModelAndView();
		  log.info("buyStocks:stockId {}, units: {}, useremail:{}" , form.getId(),form.getCount() , principal.getName());  
		  Transaction transaction=userService.purchaseStocks(form.getId(), form.getName(), form.getCount(), AppConstants.USER_PREFIX+principal.getName());
		  mav.addObject("response", transaction);
		  mav.setViewName("/stock/transaction");
		  log.info("Saved transaction: {}",transaction);
		  return transaction.getId();
	  }
	
	/**
	 * Gets the stocks to buy.
	 *
	 * @return the stocks to buy
	 */
	@GetMapping("/buyStocksView")
	public ModelAndView getStocksToBuy() {
		log.info("inside getStocksToBuy");
		ModelAndView mav= new ModelAndView();
		mav.addObject("title", "Buy Stocks");
		List<ca.gl.user.model.transaction.Stock> stocks=	stockServiceProxy.getSalableStocks();
		mav.addObject("stocks", stocks);
		
		mav.setViewName("stock/salableStock");
		return mav;
	}
	
	/**
	 * View transaction.
	 *
	 * @param id the id
	 * @return the model and view
	 */
	@GetMapping("/viewTransaction")
	public ModelAndView viewTransaction(@RequestParam("transactionId")String id) {
		log.info("inside viewTransaction: transactionId: {}", id);
		ModelAndView mav= new ModelAndView();
		Transaction t=userServiceProxy.getTransaction(id);
		mav.addObject("respon", t);
		mav.setViewName("stock/transaction");
		return mav;
	}
	
	/**
	 * View transaction.
	 *
	 * @param id the id
	 * @return the model and view
	 */
	@GetMapping("/viewTransactions")
	public ModelAndView viewAllTransactions(Principal principal) {
		log.info("inside viewAllTransactions: user: {}", principal.getName());
		ModelAndView mav= new ModelAndView();
		List<Transaction> transaction=userServiceProxy.getAllTransactions(ca.gl.user.constant.AppConstants.USER_PREFIX+principal.getName());
		mav.addObject("response", transaction);
		mav.setViewName("stock/transactions");
		return mav;
	}

}
